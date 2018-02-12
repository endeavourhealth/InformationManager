import {BaseConceptModuleComponent} from './base-concept-module.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Component} from '@angular/core';
import {Concept} from '../../models/concept';
import {Router} from '@angular/router';
import {Class} from '../../models/class';

@Component({
  templateUrl: './base-concept-module.component.html',
  styleUrls: ['./base-concept-module.component.css']
})
export class RecordStructuresModuleComponent extends BaseConceptModuleComponent {
  constructor(private router: Router, modal: NgbModal) {
    super(modal, 'Record structures', 'fa-sitemap', [Class.EVENT_TYPE, Class.RECORD_TYPE, Class.FIELD_LIBRARY, Class.FIELD]);
  }

  onConceptSelected(concept: Concept) {
    switch (this.selectedClass) {
      case Class.EVENT_TYPE:
      case Class.RECORD_TYPE:
        this.router.navigate(['editEventRecordConcept', concept.id]);
        break;
      case Class.FIELD_LIBRARY:
        this.router.navigate(['editAbstractFieldConcept', concept.id]);
        break;
      case Class.FIELD:
        this.router.navigate(['editFieldConcept', concept.id]);
        break;
      default:
        console.log('Invalid Record Structure class ' + this.selectedClass.getDisplay());
    }
  }

  addRecord() {
    switch (this.selectedClass) {
      case Class.EVENT_TYPE:
      case Class.RECORD_TYPE:
        this.router.navigate(['addEventRecordConcept']);
        break;
      case Class.FIELD_LIBRARY:
        this.router.navigate(['addAbstractFieldConcept']);
        break;
      case Class.FIELD:
        this.router.navigate(['addFieldConcept']);
        break;
      default:
        console.log('Invalid Record Structure class' + this.selectedClass.getDisplay());
    }
  }
}
