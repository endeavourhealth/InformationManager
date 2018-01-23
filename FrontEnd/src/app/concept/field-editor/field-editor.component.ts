import {Component, OnInit} from '@angular/core';
import {ConceptRelationship} from '../../models/concept-relationship';
import {Relationship} from '../../models/relationship';
import {Class} from '../../models/class';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';
import {Concept} from '../../models/concept';

@Component({
  selector: 'app-field-editor',
  templateUrl: './field-editor.component.html',
  styleUrls: ['./field-editor.component.css']
})
export class FieldEditorComponent extends BaseConceptEditorComponent implements OnInit {
  Class = Class;
  Relationship = Relationship;

  relationship: ConceptRelationship;

  createNew() {
    this.setConcept(
      {
        name: 'New Field Concept',
        clazz: Class.FIELD,
        status: 0
      } as Concept
    );
  }

  getCardinality() {
    if (this.relationship && this.relationship.cardinality < 2)
      return this.relationship.cardinality;
    else
      return '';
  }

}
