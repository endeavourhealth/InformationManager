import {Component, OnInit} from '@angular/core';
import {ConceptRelationship} from '../../models/concept-relationship';
import {Relationship} from '../../models/relationship';
import {Class} from '../../models/class';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';
import {Concept} from '../../models/concept';
import {PickerDialogComponent} from '../picker-dialog/picker-dialog.component';
import {Category} from '../../models/categories';

@Component({
  selector: 'app-field-editor',
  templateUrl: './field-editor.component.html',
  styleUrls: ['./field-editor.component.css']
})
export class FieldEditorComponent extends BaseConceptEditorComponent implements OnInit {
  Class = Class;
  Relationship = Relationship;

  createNew() {
    this.setConcept(
      {
        name: 'New Field Concept',
        clazz: Class.FIELD,
        status: 0
      } as Concept
    );
  }

  selectValueType() {
    const vm = this;
    PickerDialogComponent.open(vm.modal, 'Select value type', [Category.CLASS])
      .result.then(
      (result) => vm.setValueType(result)
    );
  }

  setValueType(valueTypeConcept: Concept) {
    if (!valueTypeConcept)
      return;

    this.set_HAS_Relationship(valueTypeConcept, Relationship.FIELD_VALUE_TYPE, 'field value type');
  }

  selectPreferredValueSet() {
    const vm = this;
    PickerDialogComponent.open(vm.modal, 'Select preferred value set', [Category.INTERNAL_CODEABLE_CONCEPTS, Category.EXTERNAL_CODEABLE_CONCEPTS])
      .result.then(
      (result) => vm.setPreferredValueSet(result)
    );
  }

  setPreferredValueSet(preferredValueSetConcept: Concept) {
    if (!preferredValueSetConcept)
      return;

    this.set_HAS_Relationship(preferredValueSetConcept, Relationship.PREFERRED_VALUE_SET, 'preferred value set');
  }

  clearPreferredValueSet() {
    const preferredValueSet: ConceptRelationship = this.get_HAS_RelationshipSingle(Relationship.LINKED_RECORD);
    if (preferredValueSet) {
      const i = this.related.indexOf(preferredValueSet);
      if (i >= 0)
        this.related.splice(i,1);
    }
  }

  selectOrigin() {
    const vm = this;
    PickerDialogComponent.open(vm.modal, 'Select origin', [Category.FIELD_LIBRARY])
      .result.then(
      (result) => vm.setOrigin(result)
    );
  }

  setOrigin(originConcept: Concept) {
    if (!originConcept)
      return;

    this.set_HAS_Relationship(originConcept, Relationship.ORIGIN, 'origin');
  }
}
