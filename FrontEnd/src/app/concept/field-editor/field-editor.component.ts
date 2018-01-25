import {Component, OnInit} from '@angular/core';
import {ConceptRelationship} from '../../models/concept-relationship';
import {Relationship} from '../../models/relationship';
import {Class} from '../../models/class';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';
import {Concept} from '../../models/concept';
import {PickerDialogComponent} from '../picker-dialog/picker-dialog.component';
import {Attribute} from '../../models/attribute';
import {ConceptAttribute} from '../../models/concept-attribute';

@Component({
  selector: 'app-field-editor',
  templateUrl: './field-editor.component.html',
  styleUrls: ['./field-editor.component.css']
})
export class FieldEditorComponent extends BaseConceptEditorComponent implements OnInit {
  Class = Class;
  Relationship = Relationship;
  Attribute = Attribute;

  createNew() {
    this.setConcept(
      {
        name: 'New Field Concept',
        clazz: Class.FIELD.getId(),
        status: 0
      } as Concept
    );
  }

  selectValueType() {
    const vm = this;
    PickerDialogComponent.open(vm.modal, 'Select value type', [Class.CLASS])
      .result.then(
      (result) => vm.setValueType(result)
    );
  }

  setValueType(valueTypeConcept: Concept) {
    if (!valueTypeConcept)
      return;

    this.setAttribute(Attribute.VALUE_TYPE, valueTypeConcept);
  }

  selectPreferredValueSet() {
    const vm = this;
    PickerDialogComponent.open(vm.modal, 'Select preferred value set', [Class.CODEABLE_CONCEPT])
      .result.then(
      (result) => vm.setPreferredValueSet(result)
    );
  }

  setPreferredValueSet(preferredValueSetConcept: Concept) {
    if (!preferredValueSetConcept)
      return;

    this.setAttribute(Attribute.PREFERRED_VALUE_SET, preferredValueSetConcept);
  }

  clearPreferredValueSet() {
    const preferredValueSet: ConceptAttribute = this.getAttribute(Attribute.LINKED_RECORD_TYPE);
    this.removeAttribute(preferredValueSet);
  }

  selectOrigin() {
    const vm = this;
    PickerDialogComponent.open(vm.modal, 'Select origin', [Class.FIELD_LIBRARY])
      .result.then(
      (result) => vm.setOrigin(result)
    );
  }

  setOrigin(originConcept: Concept) {
    if (!originConcept)
      return;

    this.setAttribute(Attribute.INHERITS_FIELDS, originConcept);
  }
}
