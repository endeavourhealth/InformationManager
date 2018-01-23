import { Component, OnInit } from '@angular/core';
import {Relationship} from '../../models/relationship';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';
import {Class} from '../../models/class';
import {Concept} from '../../models/concept';

@Component({
  selector: 'app-abstract-field-editor',
  templateUrl: './abstract-field-editor.component.html',
  styleUrls: ['./abstract-field-editor.component.css']
})
export class AbstractFieldEditorComponent extends BaseConceptEditorComponent {

  Relationship = Relationship;  // Import Enum for use in template

  createNew() {
    this.setConcept(
      {
        name: 'New Abstract Field Concept',
        clazz: Class.ABSTRACT_FIELD,
        status: 0
      } as Concept
    );
  }

  selectValueType() {

  }

  selectLinkedRecord() {

  }

  clearLinkedRecord() {

  }
}
