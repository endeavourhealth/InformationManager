import {BaseConceptModuleComponent} from './base-concept-module.component';
import {Category} from '../../models/categories';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Component} from '@angular/core';

@Component({
  templateUrl: './base-concept-module.component.html',
  styleUrls: ['./base-concept-module.component.css']
})
export class CoreModelModuleComponent extends BaseConceptModuleComponent {
  constructor(modal: NgbModal) {
    super(modal, 'Core Model', 'fa-lightbulb-o', [Category.CLASS, Category.RELATIONS_AND_ATTRIBUTES]);
  }
}
