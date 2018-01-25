import {BaseConceptModuleComponent} from './base-concept-module.component';
import {Concept} from '../../models/concept';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Component} from '@angular/core';
import {Class} from '../../models/class';
import {Router} from '@angular/router';

@Component({
  templateUrl: './base-concept-module.component.html',
  styleUrls: ['./base-concept-module.component.css']
})
export class ViewModuleComponent extends BaseConceptModuleComponent {
  constructor(private router: Router, modal: NgbModal) {
    super(modal, 'Views', 'fa-eye', [Class.VIEW], true);
  }

  onConceptSelected(concept: Concept) {
    this.router.navigate(['editViewConcept', concept.id]);
  }

  addRecord() {
    this.router.navigate(['addViewConcept']);
  }
}
