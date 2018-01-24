import {OnInit} from '@angular/core';
import {Concept} from '../../models/concept';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Class} from '../../models/class';

export abstract class BaseConceptModuleComponent implements OnInit {
  moduleTitle: string;
  moduleIcon: string;
  moduleClasses: Class[];
  hideFilter: boolean = false;
  selectedClass: Class = null;

  constructor(protected modal: NgbModal, title: string, icon: string, classes: Class[], hideFilter? : boolean) {
    this.moduleTitle = title;
    this.moduleIcon = icon;
    this.moduleClasses = classes;
    this.hideFilter = hideFilter;
  }

  ngOnInit() {
  }

  addRecord() {
  }

  onClassSelected(selectedClass: Class) {
    this.selectedClass = selectedClass;
  }

  onConceptSelected(concept: Concept) {
  }
}
