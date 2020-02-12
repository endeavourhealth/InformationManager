import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {LoggerService, MessageBoxDialogComponent} from 'dds-angular8';
import {MatDialog} from '@angular/material/dialog';
import {ParentHierarchyDialogComponent} from '../parent-hierarchy-dialog/parent-hierarchy-dialog.component';
import {ChildHierarchyDialogComponent} from '../child-hierarchy-dialog/child-hierarchy-dialog.component';
import {Namespace} from '../../models/Namespace';
import {ConceptExpressionDialogComponent} from '../concept-expression-dialog/concept-expression-dialog.component';
import {ConceptEditorDialogComponent} from '../concept-editor-dialog/concept-editor-dialog.component';
import {Axiom} from '../../models/Axiom';
import {ConceptDefinition} from '../../models/ConceptDefinition';
import {ClassExpression} from '../../models/definitionTypes/ClassExpression';
import {PropertyDefinition} from '../../models/definitionTypes/PropertyDefinition';
import {SimpleConcept} from '../../models/definitionTypes/SimpleConcept';
import {RoleGroup} from '../../models/definitionTypes/RoleGroup';
import {ConceptPickerDialogComponent} from '../concept-picker-dialog/concept-picker-dialog.component';

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

    let available = this.axioms;

    if (this.definition.subClassOf)
      available = available.filter(i => ['SubClassOf', 'SubPropertyOf', 'InversePropertyOf', 'PropertyRange', 'PropertyDomain', 'PropertyChain'].indexOf(i.token) == -1);

    if (this.definition.equivalentTo)
      available = available.filter(i => ['EquivalentTo'].indexOf(i.token) == -1);


    if (this.definition.subPropertyOf)
      available = available.filter(i => ['SubClassOf', 'SubPropertyOf', 'MappedTo', 'ReplacedBy', 'Replaced', 'MemberOf'].indexOf(i.token) == -1);



    if (available.length == this.axioms.length) // Nothing yet defined
      available = available.filter(i => i.initial);

    return available;
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
          (result) => (result) ? this.addClassExpression(axiom, result, roleGroup) : {},
          (error) => this.log.error(error)
        );
        break;
      case 'SubPropertyOf':
      case 'InversePropertyOf':
        ConceptPickerDialogComponent.open(this.dialog, null, ['cm:Property']).subscribe(
          (result) => (result) ? this.addAxiomSupertype(axiom, {concept: result, inferred: false}) : {},
          (error) => this.log.error(error)
        );
        break;
    }
  }

  addClassExpression(axiom: Axiom, definition: any, roleGroup?: number) {
    if (definition.object || definition.data)
      this.addAxiomRoleGroupProperty(axiom, definition, roleGroup);
    else
      this.addAxiomSupertype(axiom, <SimpleConcept>definition);
  }

  private addAxiomSupertype(axiom: Axiom, definition: SimpleConcept) {
    this.conceptService.addAxiomSupertype(this.concept.iri, axiom, <SimpleConcept>definition).subscribe(
      (result) => this.loadConcept(this.concept.iri),
      (error) => this.log.error(error)
    );
  }

  private addAxiomRoleGroupProperty(axiom: Axiom, definition: any, roleGroup: number) {
    this.conceptService.addAxiomRoleGroupProperty(this.concept.iri, axiom, <PropertyDefinition>definition, roleGroup).subscribe(
      (result) => this.loadConcept(this.concept.iri),
      (error) => this.log.error(error)
    );
  }

  addRoleGroup(axiom: Axiom) {
    let expression = <ClassExpression>this.definition[axiom.definitionProperty];
    let group = 1;
    if (expression.roleGroups && expression.roleGroups.length > 0)
      group = Math.max.apply(Math, expression.roleGroups.map(r => r.group)) + 1;
    this.addDefinition(axiom, group);
  }

  confirmDeleteDefinition(axiom: Axiom, definition: any, roleGroup?: number) {
    MessageBoxDialogComponent.open(this.dialog, 'Delete definition', 'Are you sure you want to delete this definition?', 'Delete definition', 'Cancel').subscribe(
      (ok) => ok ? this.deleteDefinition(axiom, definition, roleGroup) : {},
      (error) => this.log.error(error)
    );
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

  confirmDeleteGroup(axiom: Axiom, group: RoleGroup) {
    MessageBoxDialogComponent.open(this.dialog, 'Delete group', 'Are you sure you want to delete this group and its definitions?', 'Delete group', 'Cancel').subscribe(
      (ok) => ok ? this.deleteGroup(axiom, group) : {},
      (error) => this.log.error(error)
    );
  }

  deleteGroup(axiom: Axiom, group: RoleGroup) {
    this.conceptService.deleteAxiomRoleGroup(this.concept.iri, axiom, group.group).subscribe(
      (result) => this.loadConcept(this.concept.iri),
      (error) => this.log.error(error)
    );
  }

  confirmDeleteAxiom(axiom: Axiom) {
    MessageBoxDialogComponent.open(this.dialog, 'Delete axiom', 'Are you sure you want to delete this axiom and its definitions?', 'Delete axiom', 'Cancel').subscribe(
      (ok) => ok ? this.deleteAxiom(axiom) : {},
      (error) => this.log.error(error)
    );
  }

  deleteAxiom(axiom: Axiom) {
    this.conceptService.deleteAxiom(this.concept.iri, axiom).subscribe(
      (result) => this.loadConcept(this.concept.iri),
      (error) => this.log.error(error)
    );
  }

  getPropertyCardinality(property: PropertyDefinition) {
    if (!property.minCardinality && !property.maxCardinality)
      return '';
    else
      return '(' + (property.minCardinality ? property.minCardinality : 0) + ':' + (property.maxCardinality ? property.maxCardinality : 0) + ')';
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
