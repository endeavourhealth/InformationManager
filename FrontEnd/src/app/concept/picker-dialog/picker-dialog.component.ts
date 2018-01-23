import { Component, OnInit } from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';
import {Concept} from '../../models/concept';
import {Category} from '../../models/categories';

@Component({
  selector: 'app-picker-dialog',
  templateUrl: './picker-dialog.component.html',
  styleUrls: ['./picker-dialog.component.css']
})
export class PickerDialogComponent implements OnInit {
  public static open(modalService: NgbModal, categoryFilter?: Category[]) {
    const modalRef = modalService.open(PickerDialogComponent, { backdrop: 'static', size: 'lg'});
    modalRef.componentInstance.categories = categoryFilter;
    return modalRef;
  }

  Category = Category;

  categories: Category[];
  selectedConcept: Concept;
  matches: Concept[] = [];
  filter: string;

  constructor(public activeModal: NgbActiveModal, protected conceptService: ConceptService) { }

  ngOnInit() {
  }

  applyFilter() {
    const vm = this;
    let categoryIds: number[] = [];
    vm.matches = null;

    for(let category of vm.categories)
      categoryIds.push(category.getId());

    vm.conceptService.listConcepts(categoryIds, 1, 20, vm.filter)
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
