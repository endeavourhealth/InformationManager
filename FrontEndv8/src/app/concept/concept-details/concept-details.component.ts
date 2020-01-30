import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {MatDialog} from '@angular/material/dialog';
import {ParentHierarchyDialogComponent} from '../parent-hierarchy-dialog/parent-hierarchy-dialog.component';
import {ChildHierarchyDialogComponent} from '../child-hierarchy-dialog/child-hierarchy-dialog.component';
import {Namespace} from '../../models/Namespace';
import {DefinitionEditDialogComponent} from '../definition-edit-dialog/definition-edit-dialog.component';
import {ConceptEditorDialogComponent} from '../concept-editor-dialog/concept-editor-dialog.component';
import {ConceptAxiom} from '../../models/ConceptAxiom';
import {Axiom} from '../../models/Axiom';

@Component({
  selector: 'app-concept-details',
  templateUrl: './concept-details.component.html',
  styleUrls: ['./concept-details.component.scss']
})
export class ConceptDetailsComponent implements OnInit {
  namespaces: Namespace[];
  concept: Concept;
  conceptAxioms: ConceptAxiom[];
  axioms: Axiom[];

  constructor(private route: ActivatedRoute,
              private conceptService: ConceptService,
              private log: LoggerService,
              private dialog: MatDialog
  ) {
  }

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

  // General data load methods
  loadConcept(conceptId: string) {
    this.conceptService.getConcept(conceptId)
      .subscribe(
        (result) => this.concept = result,
        (error) => this.log.error(error)
      );
    this.conceptService.getConceptAxioms(conceptId)
      .subscribe(
        (result) => this.conceptAxioms = result,
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

  getName(conceptId: string): string {
    if (!conceptId)
      return '';
    else
      return this.conceptService.getName(conceptId);
  }

  getAxioms() {
    if (this.conceptAxioms == null || this.axioms == null)
      return [];

    // TODO: Finish filtering (cant add SubProperty if already SubClass, etc)
    if (this.conceptAxioms.length == 0)
      return this.axioms.filter(a => a.initial);

    return this.axioms;
  }

  // Concept details (iri, name, etc) editing
  editConcept() {
    ConceptEditorDialogComponent.open(this.dialog, this.concept).subscribe(
      (result) => result ? this.updateConcept(result) : {},
      (error) => this.log.error(error)
    );
  }

  updateConcept(concept: Concept) {
    this.conceptService.updateConcept(concept).subscribe(
      (result) => this.concept = concept,
      (error) => this.log.error(error)
    );
  }

  // Axiom/definition edit methods
  addAxiom(axiomToken: string) {
    DefinitionEditDialogComponent.open(this.dialog, axiomToken).subscribe(
      (result) => {
        if (result)
          this.conceptAxioms.push({token: axiomToken, definitions: [result]})
      },
      (error) => this.log.error(error)
    );
  }

  addAxiomDefinition(axiomToken: string, definitionList: any[]) {
    DefinitionEditDialogComponent.open(this.dialog, axiomToken).subscribe(
      (result) => {
        if (result) {
          // if (result.properties && axiomToken.)
          definitionList.push(result);
        }
      },
      (error) => this.log.error(error)
    );
  }

  insertDef(axiomToken: string, definitionList?:any, definition?: any) {
    if (definition == null)
      return;

    if (!this.conceptAxioms.find(a => a.token == axiomToken))
        this.conceptAxioms.push({token: axiomToken, definitions: []});

  }

  editDefintionExpression(definition: ConceptAxiom) {
/*    DefinitionEditDialogComponent.open(this.dialog, definition).subscribe(
      (result) => {},
      (error) => this.log.error(error)
    );*/
  }

  addGroup(axiom) {
    axiom.definitions.push({properties: []});
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
