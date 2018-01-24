import { Component, OnInit } from '@angular/core';
import {Relationship} from '../../models/relationship';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';
import {Class} from '../../models/class';
import {Concept} from '../../models/concept';
import {PickerDialogComponent} from '../picker-dialog/picker-dialog.component';
import {ConceptRelationship} from '../../models/concept-relationship';

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
    this.set_HAS_Relationship(valueTypeConcept, Relationship.FIELD_VALUE_TYPE, 'Field value type');
  }

  selectLinkedRecord() {
    const vm = this;
    PickerDialogComponent.open(this.modal, 'Select linked record', [Class.RECORD_TYPE, Class.EVENT_TYPE])
      .result.then(
      (result) => vm.setLinkedRecord(result)
    );
  }

  setLinkedRecord(linkedRecordConcept: Concept) {
    this.set_HAS_Relationship(linkedRecordConcept, Relationship.LINKED_RECORD, 'linked record');
  }

  clearLinkedRecord() {
    const linkedRecord: ConceptRelationship = this.get_HAS_RelationshipSingle(Relationship.LINKED_RECORD);
    this.removeRelationship(linkedRecord);
  }
}
