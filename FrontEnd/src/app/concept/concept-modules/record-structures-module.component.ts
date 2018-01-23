import {BaseConceptModuleComponent} from './base-concept-module.component';
import {Category} from '../../models/categories';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Component} from '@angular/core';
import {Concept} from '../../models/concept';
import {Router} from '@angular/router';

@Component({
  templateUrl: './base-concept-module.component.html',
  styleUrls: ['./base-concept-module.component.css']
})
export class RecordStructuresModuleComponent extends BaseConceptModuleComponent {
  constructor(private router: Router, modal: NgbModal) {
    super(modal, 'Record Structures', 'fa-sitemap', [Category.EVENT_AND_RECORD_TYPES, Category.FIELD_LIBRARY, Category.FIELDS]);
  }

  onConceptSelected(concept: Concept) {
    switch (this.selectedCategory) {
      case Category.EVENT_AND_RECORD_TYPES:
        this.router.navigate(['editEventRecordConcept', concept.id]);
        break;
      case Category.FIELD_LIBRARY:
        this.router.navigate(['editAbstractFieldConcept', concept.id]);
        break;
      case Category.FIELDS:
        this.router.navigate(['editFieldConcept', concept.id]);
        break;
      default:
        console.log('Invalid Record Structure category ' + this.selectedCategory.getDisplay());
    }
  }

  addRecord() {
    switch (this.selectedCategory) {
      case Category.EVENT_AND_RECORD_TYPES:
        this.router.navigate(['addEventRecordConcept']);
        break;
      case Category.FIELD_LIBRARY:
        this.router.navigate(['addAbstractFieldConcept']);
        break;
      case Category.FIELDS:
        this.router.navigate(['addFieldConcept']);
        break;
      default:
        console.log('Invalid Record Structure category ' + this.selectedCategory.getDisplay());
    }
  }
}
