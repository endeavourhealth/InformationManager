import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ConceptService} from '../concept.service';
import {LoggerService, MessageBoxDialogComponent} from 'dds-angular8';
import {MatDialog} from '@angular/material/dialog';
import {ParentHierarchyDialogComponent} from '../parent-hierarchy-dialog/parent-hierarchy-dialog.component';
import {ChildHierarchyDialogComponent} from '../child-hierarchy-dialog/child-hierarchy-dialog.component';
import {Namespace} from '../../models/Namespace';
import {ConceptExpressionDialogComponent} from '../concept-expression-dialog/concept-expression-dialog.component';
import {ConceptEditorDialogComponent} from '../concept-editor-dialog/concept-editor-dialog.component';
import {ConceptPickerDialogComponent} from '../concept-picker-dialog/concept-picker-dialog.component';
import {PropertyRangeDialogComponent} from '../property-range-dialog/property-range-dialog.component';
import {PropertyDomainDialogComponent} from '../property-domain-dialog/property-domain-dialog.component';
import {Concept} from '../../models/Concept';

@Component({
  selector: 'app-concept-details',
  templateUrl: './concept-details.component.html',
  styleUrls: ['./concept-details.component.scss']
})
export class ConceptDetailsComponent implements OnInit {
  // namespaces: Namespace[];
  definition: Concept;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private conceptService: ConceptService,
    private log: LoggerService,
    private dialog: MatDialog
  ) {
  }

  ngOnInit() {
    this.route.params.subscribe(
      (params) => this.loadDefinition(params['id'])
    );
/*
    this.conceptService.getNamespaces().subscribe(
      (result) => this.namespaces = result,
      (error) => this.log.error(error)
    )
*/
  }

  // General data load methods
  loadDefinition(iri: string) {
    this.conceptService.getConceptDefinition(iri)
      .subscribe(
        (result) => this.definition = result,
        (error) => this.log.error(error)
      );
  }

/*
  getNamespace(): string {
    if (!this.definition || !this.namespaces)
      return 'Loading...';

    let prefix = this.definition.iri.substr(0, this.definition.iri.indexOf(':'));
    let namespace = this.namespaces.find(ns => ns.prefix === prefix);

    return namespace ? namespace.iri : 'Unknown';
  }
*/

  getName(conceptId: string): string {
    if (!conceptId)
      return '';
    else
      return this.conceptService.getName(conceptId);
  }
/*
  // Concept details (iri, name, etc) editing
  editConcept() {
    ConceptEditorDialogComponent.open(this.dialog, this.definition).subscribe(
      (result) => result ? this.updateConcept(result) : {},
      (error) => this.log.error(error)
    );
  }

  updateConcept(concept: Concept) {
    this.conceptService.updateConcept(concept).subscribe(
      (result) => this.definition = concept,
      (error) => this.log.error(error)
    );
  }

  promptDelete() {
    MessageBoxDialogComponent.open(this.dialog, 'Delete concept', 'Are you sure that you want to delete this concept?', 'Delete concept', 'Cancel')
      .subscribe(
        (result) => result ? this.deleteConcept() : {},
        (error) => this.log.error(error)
      );
  }

  deleteConcept() {
    this.conceptService.deleteConcept(this.definition.id).subscribe(
      (result) => this.router.navigate(['concepts']),
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
          (result) => (result) ? this.addAxiomSupertype(axiom, {concept: result, inferred: false} as SimpleConcept) : {},
          (error) => this.log.error(error)
        );
        break;
      case 'MappedTo':
      case 'ReplacedBy':
      case 'ChildOf':
        ConceptPickerDialogComponent.open(this.dialog, null, ['cm:Core']).subscribe(                    // TODO: Check parent
          (result) => (result) ? this.addAxiomSupertype(axiom, {concept: result, inferred: false} as SimpleConcept) : {},
          (error) => this.log.error(error)
        );
        break;
      case 'PropertyRange':
        PropertyRangeDialogComponent.open(this.dialog, null).subscribe(
          (result) => (result) ? this.addPropertyRange(result) : {},
          (error) => this.log.error(error)
        );
        break;
      case 'PropertyDomain':
        PropertyDomainDialogComponent.open(this.dialog, null).subscribe(
          (result) => (result) ? this.addPropertyDomain(result) : {},
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
    this.conceptService.addAxiomSupertype(this.definition.id, axiom.token, <SimpleConcept>definition).subscribe(
      (result) => this.loadConcept(this.definition.iri),
      (error) => this.log.error(error)
    );
  }

  private addAxiomRoleGroupProperty(axiom: Axiom, definition: any, roleGroup: number) {
    this.conceptService.addAxiomRoleGroupProperty(this.definition.id, axiom.token, <PropertyDefinition>definition, roleGroup).subscribe(
      (result) => this.loadConcept(this.definition.iri),
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

  addPropertyRange(propertyRange: PropertyRange) {
    this.conceptService.addPropertyRange(this.definition.id, propertyRange).subscribe(
      (result) => this.loadConcept(this.definition.iri),
      (error) => this.log.error(error)
    );
  }

  addPropertyDomain(propertyDomain: PropertyDomain) {
    this.conceptService.addPropertyDomain(this.definition.id, propertyDomain).subscribe(
      (result) => this.loadConcept(this.definition.iri),
      (error) => this.log.error(error)
    );
  }

  deleteRange(propertyRange: PropertyRange) {
    this.conceptService.delPropertyRange(this.definition.id, propertyRange.id).subscribe(
      (result) => this.loadConcept(this.definition.iri),
      (error) => this.log.error(error)
    );
  }

  deleteDomain(propertyDomain: PropertyDomain) {
    this.conceptService.delPropertyDomain(this.definition.id, propertyDomain.id).subscribe(
      (result) => this.loadConcept(this.definition.iri),
      (error) => this.log.error(error)
    );
  }

  confirmDeleteDefinition(axiom: Axiom, definition: any, roleGroup?: number) {
    MessageBoxDialogComponent.open(this.dialog, 'Delete definition', 'Are you sure you want to delete this definition?', 'Delete definition', 'Cancel').subscribe(
      (ok) => ok ? this.deleteDefinition(axiom, definition, roleGroup) : {},
      (error) => this.log.error(error)
    );
  }

  deleteDefinition(axiom: Axiom, definition: any, roleGroup?: number) {
    if (definition.object || definition.data)
      this.conceptService.deleteAxiomRoleGroupProperty(this.definition.id, (<PropertyDefinition>definition)).subscribe(
        (result) => this.loadConcept(this.definition.iri),
        (error) => this.log.error(error)
      );
    else
      this.conceptService.deleteAxiomSupertype(this.definition.id, (<SimpleConcept>definition).id).subscribe(
        (result) => this.loadConcept(this.definition.iri),
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
    this.conceptService.deleteAxiomRoleGroup(this.definition.id, axiom, group.group).subscribe(
      (result) => this.loadConcept(this.definition.iri),
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
    this.conceptService.deleteAxiom(this.definition.id, axiom).subscribe(
      (result) => this.loadConcept(this.definition.iri),
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
    this.dialog.open(ParentHierarchyDialogComponent, {disableClose: true, data: this.definition}).afterClosed().subscribe(
      () => {},
      (error) => this.log.error(error)
    )
  }

  childHierarchy() {
    this.dialog.open(ChildHierarchyDialogComponent, {disableClose: true, data: this.definition}).afterClosed().subscribe(
      () => {},
      (error) => this.log.error(error)
    )
  }*/
}
