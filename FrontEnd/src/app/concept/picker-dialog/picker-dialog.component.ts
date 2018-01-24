import { Component, OnInit } from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';
import {Concept} from '../../models/concept';
import {Class} from '../../models/class';

@Component({
  selector: 'app-picker-dialog',
  templateUrl: './picker-dialog.component.html',
  styleUrls: ['./picker-dialog.component.css']
})
export class PickerDialogComponent implements OnInit {
  public static open(modalService: NgbModal, title: string, classFilter?: Class[]) {
    const modalRef = modalService.open(PickerDialogComponent, { backdrop: 'static', size: 'lg'});
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.classes = classFilter;
    return modalRef;
  }

  Class = Class;

  title: string;
  classes: Class[];
  selectedConcept: Concept;
  matches: Concept[] = [];
  filter: string;

  constructor(public activeModal: NgbActiveModal, protected conceptService: ConceptService) { }

  ngOnInit() {
  }

  applyFilter() {
    const vm = this;
    let classes: number[] = [];
    vm.matches = null;

    vm.conceptService.listConcepts(classes, 1, 20, vm.filter)
      .subscribe(
        (result) => vm.matches = result,
        (error) => console.error(error)
      );
  }

  clearFilter() {}

  ok() {
    this.activeModal.close(this.selectedConcept);
  }

  cancel() {
    this.activeModal.close();
  }
}
