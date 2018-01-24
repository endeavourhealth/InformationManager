import {BaseConceptModuleComponent} from './base-concept-module.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Component} from '@angular/core';
import {Class} from '../../models/class';

@Component({
  templateUrl: './base-concept-module.component.html',
  styleUrls: ['./base-concept-module.component.css']
})
export class CoreModelModuleComponent extends BaseConceptModuleComponent {
  constructor(modal: NgbModal) {
    super(modal, 'Core Model', 'fa-lightbulb-o', [Class.CLASS, Class.RELATIONSHIP, Class.ATTRIBUTE]);
  }
}
