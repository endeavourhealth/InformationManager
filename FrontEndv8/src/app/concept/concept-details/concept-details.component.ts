import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {LoggerService, MessageBoxDialogComponent} from 'dds-angular8';
import {MatDialog} from '@angular/material/dialog';
import {ParentHierarchyDialogComponent} from '../parent-hierarchy-dialog/parent-hierarchy-dialog.component';
import {ChildHierarchyDialogComponent} from '../child-hierarchy-dialog/child-hierarchy-dialog.component';
import {Namespace} from '../../models/Namespace';
import {Definition} from '../../models/Definition';
import {Supertype} from '../../models/Supertype';
import {Property} from '../../models/Property';
import {ExpressionEditDialogComponent} from '../expression-edit-dialog/expression-edit-dialog.component';
import {ConceptEditorDialogComponent} from '../concept-editor-dialog/concept-editor-dialog.component';

@Component({
  selector: 'app-concept-details',
  templateUrl: './concept-details.component.html',
  styleUrls: ['./concept-details.component.scss']
})
export class ConceptDetailsComponent implements OnInit {
  namespaces: Namespace[];
  concept: Concept;
  definitions: Definition[];
  axioms: string[];

  constructor(private route: ActivatedRoute,
              private conceptService: ConceptService,
              private log: LoggerService,
              private dialog: MatDialog
              ) { }

  ngOnInit() {
    this.route.params.subscribe(
      (params) => this.loadConcept(params['id'])
    );
    this.conceptService.getAxioms().subscribe(
      (result) => this.axioms = result,
      (error) => this.log.error(error)
    );
    this.conceptService.getNamespaces().subscribe(
      (result) => this.namespaces = result,
        (error) => this.log.error(error)
    )
  }

  loadConcept(conceptId: string) {
    this.conceptService.getConcept(conceptId)
      .subscribe(
        (result) => this.concept = result,
        (error) => this.log.error(error)
      );
    this.conceptService.getDefinition(conceptId)
      .subscribe(
        (result) => this.definitions = result,
        (error) => this.log.error(error)
      );
  }

  getNamespace(): string {
    if (!this.concept || !this.namespaces)
      return 'Loading...';

    let prefix = this.concept.iri.substr(0, this.concept.iri.indexOf(':'));
    let namespace = this.namespaces.find(ns => ns.prefix === prefix);

    return namespace ? namespace.name : 'Unknown';
  }

  getName(conceptId : string) : string {
    if (!conceptId)
      return '';
    else
      return this.conceptService.getName(conceptId);
  }

  editConcept() {
    ConceptEditorDialogComponent.open(this.dialog, this.concept).subscribe(
      (result) => {},
      (error) => this.log.error(error)
    );
  }

  addDefinition(axiom: string) {
    let def = new Definition();
    def.axiom = axiom;
    this.editDefintionExpression(def, {} as Supertype);
  }

  editDefintionExpression(definition: Definition, expression: Supertype | Property) {
    ExpressionEditDialogComponent.open(this.dialog, definition, expression).subscribe(
      (result) => {},
      (error) => this.log.error(error)
    );
  }

  deleteAxiom(axiom: string) {

/*    MessageBoxDialogComponent.open(this.dialog, 'Delete axiom', 'Are you sure that you want to delete the "' + axiom + '" axiom and all its associated definitions?', 'Delete axiom', 'Cancel', true).subscribe(
      (result) => {},
      (error) => this.log.error(error)
    );*/


     // MessageBoxDialogComponent.open(this.dialog, 'Adult check', 'Is the patient classed as an adult?', 'Over 18', 'Under 18').subscribe();

    // MessageBoxDialogComponent.open(this.dialog, 'Changes saved', 'The changes will take effect the next time the server is restarted.', 'OK').subscribe();
  }

  parentHierarchy() {
    this.dialog.open(ParentHierarchyDialogComponent, {disableClose: true, data: this.concept}).afterClosed().subscribe(
      () => {},
      (error) => this.log.error(error)
    )
  }

  childHierarchy() {
    this.dialog.open(ChildHierarchyDialogComponent, {disableClose: true, data: this.concept}).afterClosed().subscribe(
      () => {},
      (error) => this.log.error(error)
    )
  }


}
