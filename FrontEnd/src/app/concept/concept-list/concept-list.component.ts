import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Category} from '../../models/categories';
import {Concept} from '../../models/concept';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'eds-angular4';

@Component({
  selector: 'app-concept-list',
  templateUrl: './concept-list.component.html',
  styleUrls: ['./concept-list.component.css']
})
export class ConceptListComponent implements OnInit {
  @Input() categories: Category[];
  @Input() hideFilter: boolean;
  @Output() conceptSelected: EventEmitter<Concept> = new EventEmitter<Concept>();
  @Output() categorySelected: EventEmitter<Category> = new EventEmitter<Category>();

  page: number = 1;
  pageSize: number = 15;
  filter: string = null;
  selectedCategoryId: number;
  concepts: Concept[] = [];
  totalConcepts: number = null;

  constructor(protected logger: LoggerService, private conceptService : ConceptService) { }

  ngOnInit() {
    const state = this.conceptService.state;
    if (state && this.categories.indexOf(Category.getById(state.selectedCategoryId)) >= 0) {
      this.selectedCategoryId = state.selectedCategoryId;
      this.page = state.page;
      this.filter = state.filter;
    } else
      this.selectedCategoryId = this.categories[0].getId();

    this.loadConcepts();
    this.categorySelected.next(Category.getById(this.selectedCategoryId));
  }

  loadConcepts() {
    const vm = this;
    vm.concepts = null;

    if (!vm.totalConcepts)
      vm.getConceptCount();

    vm.conceptService.listConcepts([this.selectedCategoryId], this.page, this.pageSize, this.filter)
      .subscribe(
        (result) => this.concepts = result,
        (error) => this.logger.error(error)
      );
  }

  private getConceptCount() {
    const vm = this;
    vm.conceptService.getConceptCount(this.selectedCategoryId, this.filter)
      .subscribe(
        (result) => this.totalConcepts = result,
        (error) => this.logger.error(error)
      );
  }

  categoryChanged(categoryId: number) {
    this.page = 1;
    this.totalConcepts = null;
    this.loadConcepts();
    this.saveState();
    this.categorySelected.next(Category.getById(categoryId));
  }

  applyFilter() {
    this.page = 1;
    this.totalConcepts = null;
    this.loadConcepts();
    this.saveState();
  }

  clearFilter() {
    this.page = 1;
    this.totalConcepts = null;
    this.filter = null;
    this.applyFilter();
    this.saveState();
  }

  selectConcept(concept: Concept) {
    this.conceptSelected.next(concept);
  }

  getStatusText(status: number) {
    return this.conceptService.getStatusName(status);
  }

  getCachedConceptName(conceptId: number) {
    return this.conceptService.getCachedConceptName(conceptId);
  }

  pageChanged(pageNumber: number) {
    this.page = pageNumber;
    this.loadConcepts();
    this.saveState();
  }

  private saveState() {
    this.conceptService.state = {
      selectedCategoryId: this.selectedCategoryId,
      page: this.page,
      filter: this.filter
    };
  }

}
