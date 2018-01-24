import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Concept} from '../../models/concept';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'eds-angular4';
import {Class} from '../../models/class';

@Component({
  selector: 'app-concept-list',
  templateUrl: './concept-list.component.html',
  styleUrls: ['./concept-list.component.css']
})
export class ConceptListComponent implements OnInit {
  @Input() classes: Class[];
  @Input() hideFilter: boolean;
  @Output() conceptSelected: EventEmitter<Concept> = new EventEmitter<Concept>();
  @Output() classSelected: EventEmitter<Class> = new EventEmitter<Class>();

  Class = Class;

  page: number = 1;
  pageSize: number = 15;
  filter: string = null;
  selectedClassId: number;
  concepts: Concept[] = [];
  totalConcepts: number = null;

  constructor(protected logger: LoggerService, private conceptService : ConceptService) { }

  ngOnInit() {
    const state = this.conceptService.state;
    if (state && this.classes.indexOf(Class.byId(state.selectedClassId)) >= 0) {
      this.selectedClassId = state.selectedClassId;
      this.page = state.page;
      this.filter = state.filter;
    } else
      this.selectedClassId = this.classes[0].getId();

    this.loadConcepts();
    this.classSelected.next(Class.byId(this.selectedClassId));
  }

  loadConcepts() {
    const vm = this;
    vm.concepts = null;

    if (!vm.totalConcepts)
      vm.getConceptCount();

    vm.conceptService.listConcepts([this.selectedClassId], this.page, this.pageSize, this.filter)
      .subscribe(
        (result) => this.concepts = result,
        (error) => this.logger.error(error)
      );
  }

  private getConceptCount() {
    const vm = this;
    vm.conceptService.getConceptCount([this.selectedClassId], this.filter)
      .subscribe(
        (result) => this.totalConcepts = result,
        (error) => this.logger.error(error)
      );
  }

  classChanged(classId: string) {
    this.selectedClassId = parseInt(classId);
    this.page = 1;
    this.totalConcepts = null;
    this.loadConcepts();
    this.saveState();
    this.classSelected.next(Class.byId(this.selectedClassId));
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
      selectedClassId: this.selectedClassId,
      page: this.page,
      filter: this.filter
    };
  }

}
