import { Component } from '@angular/core';
import {Concept} from '../../models/concept';
import {Class} from '../../models/class';
import {BaseConceptEditorComponent} from '../base-concept-editor/base-concept-editor.component';

@Component({
  selector: 'app-view-editor',
  templateUrl: './view-editor.component.html',
  styleUrls: ['./view-editor.component.css']
})
export class ViewEditorComponent extends BaseConceptEditorComponent {

  createNew() {
    this.setConcept(
      {
        name: 'New View',
        clazz: Class.VIEW.getId(),
        status: 0
      } as Concept
    );
  }
}
