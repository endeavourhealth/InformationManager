import { Component } from '@angular/core';
import {Concept} from '../../models/concept';
import {Class} from '../../models/class';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';
import {ViewNavigationDialogComponent} from '../view-navigation-dialog/view-navigation-dialog.component';
import {Relationship} from '../../models/relationship';

@Component({
  selector: 'app-view-editor',
  templateUrl: './view-editor.component.html',
  styleUrls: ['./view-editor.component.css']
})
export class ViewEditorComponent extends BaseConceptEditorComponent {

  children: Concept[];

  createNew() {
    this.setConcept(
      {
        name: 'New View',
        clazz: Class.VIEW.getId(),
        status: 0
      } as Concept
    );
  }

  setConcept(concept: Concept) {
    super.setConcept(concept);
    this.conceptService.getViewChildren(concept.id, Relationship.CHILD)
      .subscribe(
        (result) => this.children = result,
        (error) => this.logger.error(error)
      );
  }

  testPicker() {
    ViewNavigationDialogComponent.open(this.modal, this.concept.id);
  }
}
