import {BaseConceptModuleComponent} from './base-concept-module.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Component} from '@angular/core';
import {Class} from '../../models/class';

@Component({
  templateUrl: './base-concept-module.component.html',
  styleUrls: ['./base-concept-module.component.css']
})
export class CodeableConceptsModuleComponent extends BaseConceptModuleComponent {
  constructor(modal: NgbModal) {
    super(modal, 'Codeable Concepts', 'fa-list-ol', [Class.CODEABLE_CONCEPT]);
  }
}
