import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {MatDialog} from '@angular/material/dialog';
import {ParentHierarchyDialogComponent} from '../parent-hierarchy-dialog/parent-hierarchy-dialog.component';
import {ChildHierarchyDialogComponent} from '../child-hierarchy-dialog/child-hierarchy-dialog.component';
import {Namespace} from '../../models/Namespace';
import {ConceptExpressionDialogComponent} from '../concept-expression-dialog/concept-expression-dialog.component';
import {ConceptEditorDialogComponent} from '../concept-editor-dialog/concept-editor-dialog.component';
import {Axiom} from '../../models/Axiom';
import {ConceptDefinition} from '../../models/ConceptDefinition';
import {RoleGroup} from '../../models/definitionTypes/RoleGroup';
import {ClassExpression} from '../../models/definitionTypes/ClassExpression';
import {PropertyDefinition} from '../../models/definitionTypes/PropertyDefinition';
import {SimpleConcept} from '../../models/definitionTypes/SimpleConcept';

@Component({
  selector: 'app-concept-details',
  templateUrl: './concept-details.component.html',
  styleUrls: ['./concept-details.component.scss']
})
export class ConceptDetailsComponent implements OnInit {
  namespaces: Namespace[];
  concept: Concept;
  definition: ConceptDefinition;
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
    this.loadDefinition(conceptId);
  }

  loadDefinition(conceptId: string) {
    this.conceptService.getConceptDefinition(conceptId)
      .subscribe(
        (result) => this.definition = result,
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
    if (this.definition == null || this.axioms == null)
      return [];

    // TODO: Finish filtering (cant add SubProperty if already SubClass, etc)
    if (this.definition)
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
  addDefinition(axiom: Axiom, roleGroup?: number) {
    switch (axiom.token) {
      case 'SubClassOf':
      case 'EquivalentTo':
        ConceptExpressionDialogComponent.open(this.dialog, axiom.token).subscribe(
          (result) => this.addClassExpression(axiom, result, roleGroup),
          (error) => this.log.error(error)
        );
        break;

    }
  }

  addClassExpression(axiom: Axiom, definition: any, roleGroup?: number) {
    if (definition.object || definition.data)
      this.conceptService.addAxiomRoleGroupProperty(this.concept.iri, axiom, <PropertyDefinition>definition, roleGroup).subscribe(
        (result) => this.loadConcept(this.concept.iri),
        (error) => this.log.error(error)
      );
    else
      this.conceptService.addAxiomSupertype(this.concept.iri, axiom, <SimpleConcept>definition).subscribe(
        (result) => this.loadConcept(this.concept.iri),
        (error) => this.log.error(error)
      );
  }

  addRoleGroup(axiom: Axiom) {
    let expression = <ClassExpression>this.definition[axiom.definitionProperty];
    this.addDefinition(axiom, expression.roleGroups.length);
  }

  deleteDefinition(axiom: Axiom, definition: any, roleGroup?: number) {
    if (definition.object || definition.data)
      this.conceptService.deleteAxiomRoleGroupProperty(this.concept.iri, axiom, (<PropertyDefinition>definition), roleGroup).subscribe(
        (result) => this.loadConcept(this.concept.iri),
        (error) => this.log.error(error)
      );
    else
      this.conceptService.deleteAxiomSupertype(this.concept.iri, axiom, (<SimpleConcept>definition).concept).subscribe(
        (result) => this.loadConcept(this.concept.iri),
        (error) => this.log.error(error)
      );
  }

  deleteAxiom(axiom: string) {

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
