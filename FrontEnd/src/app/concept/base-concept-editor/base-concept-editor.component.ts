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

  getFirstRelatedSourceByRelationship(relationship: Relationship): ConceptRelationship {
    if (this.related)
      for (let r of this.related) {
        if (r.relationshipId === relationship && r.targetId === this.concept.id)
          return r;
      }
    return null;
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
