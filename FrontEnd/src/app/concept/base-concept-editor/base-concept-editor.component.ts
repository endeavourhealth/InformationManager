import { Component, OnInit } from '@angular/core';
import {LoggerService} from 'eds-angular4';
import {Concept} from '../../models/concept';
import {ConceptService} from '../concept.service';
import {ConceptRelationship} from '../../models/concept-relationship';
import {Category} from '../../models/categories';
import {Relationship} from '../../models/relationship';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './base-concept-editor.component.html',
  styleUrls: ['./base-concept-editor.component.css']
})
export class BaseConceptEditorComponent implements OnInit {
  concept: Concept;
  category: Category;
  related: any[] = [];
  editMode: boolean = false;

  constructor(protected route: ActivatedRoute,
              protected location: Location,
              protected modal: NgbModal,
              protected logger: LoggerService,
              protected conceptService: ConceptService) { }

  ngOnInit() {
    const vm = this;
    vm.route.params.subscribe(
      params => vm.loadConcept(params['id'])
    );
  }

  loadConcept(conceptId: string) {
    if (conceptId) {
      const vm = this;
      vm.conceptService.getConcept(conceptId)
        .subscribe(
          (result) => vm.setConcept(result)
        );
    } else {
      this.createNew();
    }
  }

  createNew() {
    this.logger.error('OVERRIDE createNew() IN SUB CLASSES WITH APPROPRIATE setConcept() CALL');
  }

  setConcept(concept: Concept) {
    this.concept = concept;
    this.editMode = (this.concept.id == null);
    this.loadRelated();
  }

  protected loadRelated() {
    if (this.concept && this.concept.id) {
      const vm = this;
      vm.conceptService.getRelated(vm.concept.id)
        .subscribe(
          (result) => vm.related = (!result) ? [] : result,
          (error) => vm.logger.error(error)
        );
    }
  }

  getRelatedName(row: ConceptRelationship) {
    if (row.sourceId == this.concept.id)
      return row.targetName;
    else
      return row.sourceName;
  }

  getRelationshipName(row: ConceptRelationship) {
    if (row.sourceId == this.concept.id)
      return 'has ' + row.relationshipName + ' of';
    else
      return 'is ' + row.relationshipName + ' of';
  }

  getClassName(clazz: number) {
    return this.conceptService.getCachedConceptName(clazz);
  }

  getCardinalityName(cardinality: number) {
    return this.conceptService.getCardinalityName(cardinality);
  }

  getStatusName(status: number) {
    return this.conceptService.getStatusName(status);
  }

  get_IS_RelationshipSingle(relationship: Relationship): ConceptRelationship {
    if (this.related)
      for (let r of this.related) {
        if (r.relationshipId === relationship && r.targetId === this.concept.id)
          return r;
      }
    return null;
  }

  addRelationship(relationship: ConceptRelationship) {
    if (!relationship)
      return;

    this.related.push(relationship);
  }

  get_HAS_RelationshipSingle(relationship: Relationship): ConceptRelationship {
    if (this.related)
      for (let r of this.related) {
        if (r.relationshipId === relationship && r.sourceId === this.concept.id)
          return r;
      }
    return null;
  }

  set_HAS_Relationship(targetConcept: Concept, relationship: Relationship, relationshipName: string) {
    if (!targetConcept)
      return;

    let target: ConceptRelationship = this.get_HAS_RelationshipSingle(relationship);

    if (!target) {
      target = {
        relationshipId: relationship,
        relationshipName: relationshipName,
        sourceId: this.concept.id,
        sourceName: this.concept.name,
        order: this.related.length
      } as ConceptRelationship;
      this.related.push(target);
    }

    target.targetId = targetConcept.id;
    target.targetName = targetConcept.name;
  }

  set_IS_Relationship(sourceConcept: Concept, relationship: Relationship, relationshipName: string) {
    if (!sourceConcept)
      return;

    let source: ConceptRelationship = this.get_IS_RelationshipSingle(relationship);

    if (!source) {
      source = {
        relationshipId: relationship,
        relationshipName: relationshipName,
        targetId: this.concept.id,
        targetName: this.concept.name,
        order: this.related.length
      } as ConceptRelationship;
      this.related.push(source);
    }

    source.sourceId = sourceConcept.id;
    source.sourceName = sourceConcept.name;
  }

  removeRelationship(relationship: ConceptRelationship) {
    if (relationship) {
      const i = this.related.indexOf(relationship);
      if (i >= 0)
        this.related.splice(i,1);
    }
  }

  getBadgeClass(status: number) {
    switch (status) {
      case 0: return 'badge-warning';
      case 1: return 'badge-success';
      case 2: return 'badge-danger';
      default: return 'badge-info';
    }
  }

  save() {
    this.location.back();
  }

  cancel() {
    this.location.back();
  }

  close() {
    this.location.back();
  }
}
