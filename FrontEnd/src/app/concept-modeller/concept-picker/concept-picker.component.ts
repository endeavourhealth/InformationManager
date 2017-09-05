import {Component, OnInit} from '@angular/core';

import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptSummary} from "../models/concept-summary";
import {ConceptModellerService} from "../concept-modeller.service";

@Component({
  selector: 'ngbd-modal-content',
  templateUrl: './concept-picker.component.html',
  styleUrls: ['./concept-picker.component.css']
})
export class ConceptPickerComponent implements OnInit {
  concepts : ConceptSummary[];
  searchTerms : string;
  selectedItem : ConceptSummary;

  public static open(modalService: NgbModal) {
    const modalRef = modalService.open(ConceptPickerComponent, { backdrop : "static", size: "lg"});
    return modalRef;
  }

  constructor(public activeModal: NgbActiveModal, private service : ConceptModellerService) {}

  ngOnInit(): void {
    let vm = this;
    vm.service.getCommonConcepts(10)
      .subscribe(
        (result) => vm.concepts = result,
        (error) => console.error(error)
      );
  }

  search() : void {
    let vm = this;
    vm.service.findConceptsByName(vm.searchTerms)
      .subscribe(
        (result) => vm.concepts = result,
        (error) => console.error(error)
      );
  }

  getIcon(clazz : number) {
    switch (clazz) {
      case 1: return 'fa fa-cube';
      case 15 : return 'fa fa-share-alt';
      default : return 'fa fa-lightbulb-o';
    }
  }

}
