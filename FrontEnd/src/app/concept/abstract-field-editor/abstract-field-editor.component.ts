import { Component, OnInit } from '@angular/core';
import {Relationship} from '../../models/relationship';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';
import {Class} from '../../models/class';
import {Concept} from '../../models/concept';
import {PickerDialogComponent} from '../picker-dialog/picker-dialog.component';
import {ConceptRelationship} from '../../models/concept-relationship';
import {Attribute} from '../../models/attribute';
import {ConceptAttribute} from '../../models/concept-attribute';

@Component({
  selector: 'app-abstract-field-editor',
  templateUrl: './abstract-field-editor.component.html',
  styleUrls: ['./abstract-field-editor.component.css']
})
export class AbstractFieldEditorComponent extends BaseConceptEditorComponent {

  Attribute = Attribute;  // Import Enum for use in template

  createNew() {
    this.setConcept(
      {
        name: 'New Abstract Field',
        clazz: Class.FIELD_LIBRARY.getId(),
        status: 0
      } as Concept
    );
  }

  selectValueType() {
    const vm = this;
    PickerDialogComponent.open(this.modal, 'Select value type', [Class.CLASS])
      .result.then(
      (result) => vm.setValueType(result)
    );
  }

  setValueType(valueTypeConcept: Concept) {
    this.setAttribute(Attribute.VALUE_TYPE, valueTypeConcept);
  }

  selectLinkedRecord() {
    const vm = this;
    PickerDialogComponent.open(this.modal, 'Select linked record', [Class.RECORD_TYPE, Class.EVENT_TYPE])
      .result.then(
      (result) => vm.setLinkedRecord(result)
    );
  }

  setLinkedRecord(linkedRecordConcept: Concept) {
    this.setAttribute(Attribute.LINKED_RECORD_TYPE, linkedRecordConcept);
  }

  clearLinkedRecord() {
    const linkedRecord: ConceptAttribute = this.getAttribute(Attribute.LINKED_RECORD_TYPE);
    this.removeAttribute(linkedRecord);
  }
}
