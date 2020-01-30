import { Component, OnInit } from '@angular/core';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConceptPickerDialogComponent} from '../concept-picker-dialog/concept-picker-dialog.component';
import {Concept} from '../../models/Concept';
import {Definition} from '../../models/definitionTypes/Definition';

@Component({
  selector: 'app-definition-edit-dialog',
  templateUrl: './definition-edit-dialog.component.html',
  styleUrls: ['./definition-edit-dialog.component.scss']
})
export class DefinitionEditDialogComponent implements OnInit {
  static open(dialog: MatDialog, axiomToken: string, definition?: any) {
    let dialogRef = dialog.open(DefinitionEditDialogComponent, {disableClose: true, autoFocus: true});
    dialogRef.componentInstance.axiomToken = axiomToken;
    if (definition) {
      // Break down definition
      dialogRef.componentInstance.concept = definition.concept ? definition.concept : definition.property;
      dialogRef.componentInstance.value = definition.object ? definition.object : definition.value;
    } else
      dialogRef.componentInstance.createMode = true;

    return dialogRef.afterClosed();
  }

  createMode: boolean = false;
  axiomToken: string;

  concept: string;
  conceptType: string;
  conceptValueType: string;
  value: string;

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<DefinitionEditDialogComponent>) {
  }

  ngOnInit() {
    this.getConceptType();
  }


  getConceptType() {
    this.conceptValueType = null;

    // Check concept type (property, property type, cardinality, etc)
    if (this.concept) {
      this.conceptService.getAncestors(this.concept).subscribe(
        (result) => this.setConceptType(result),
        (error) => this.log.error(error)
      );
    }
  }

  getName(conceptId : string) : string {
    if (!conceptId)
      return '';
    else
      return this.conceptService.getName(conceptId);
  }

  promptConcept() {
    const axiomSupertypes = {
      // Initial axioms
      SubClassOf: ['cm:TypeClass', 'cm:Property', 'cm:DataType'],
      SubPropertyOf: ['cm:Property'],
      InversePropertyOf: ['cm:Property'],

      // Additional axioms
      EquivalentTo: null,
      MappedTo: ['Type'],
      ReplacedBy: ['Type'],
      Replaced: ['Type'],
      ChildOf: null,
      MemberOf: ['Collection?'],
      PropertyDomain: ['Class'],
      PropertyRange: ['DataType'],
      Key: ['Property'],
      PropertyChain: null
    };

    ConceptPickerDialogComponent.open(this.dialog, this.concept, axiomSupertypes[this.axiomToken]).subscribe(
      (result) => result ? this.setConcept(result) : {},
      (error) => this.log.error(error)
    );
  }

  setConcept(concept) {
    this.concept = concept;
    this.getConceptType();
  }


  setConceptType(ancestors: Concept[]) {
    const baseTypes = ['cm:TypeClass', 'cm:Property', 'cm:DataType']; // Priority order

    let matches = baseTypes.filter(t => ancestors.findIndex(a => a.iri == t) > -1);

    this.conceptType = matches[0];

    if (this.conceptType == 'cm:Property') {
      // Get concepts value range
      this.conceptValueType = this.concept + '.PropertyRange';
    }
  }

  promptObject(expression: any) {
  }

  ok() {
    if (this.createMode) {
      // Create new
      if (this.conceptType === 'cm:TypeClass')
        this.dialogRef.close({concept: this.concept});
      else if (this.conceptType === 'cm:Property') {
        if (this.conceptValueType === 'cm:ObjectProperty')
          this.dialogRef.close({property: this.concept, object: this.value});
        else
          this.dialogRef.close({property: this.concept, value: this.value});
      }
    } else {
      // Repopulate
    }
  }

  close() {
    this.dialogRef.close();
  }

}
