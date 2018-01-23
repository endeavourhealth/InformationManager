import {BaseConceptModuleComponent} from './base-concept-module.component';
import {Category} from '../../models/categories';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Component} from '@angular/core';

@Component({
  templateUrl: './base-concept-module.component.html',
  styleUrls: ['./base-concept-module.component.css']
})
export class CodeableConceptsModuleComponent extends BaseConceptModuleComponent {
  constructor(modal: NgbModal) {
    super(modal, 'Codeable Concepts', 'fa-list-ol', [Category.INTERNAL_CODEABLE_CONCEPTS, Category.EXTERNAL_CODEABLE_CONCEPTS]);
  }
}
