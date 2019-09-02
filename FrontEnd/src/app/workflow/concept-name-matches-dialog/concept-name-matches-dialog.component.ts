import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AnalysisResult} from '../../models/AnalysisResult';
import {ConceptService} from '../../concept/concept.service';
import {LoggerService} from 'eds-angular4';
import {Concept} from '../../models/Concept';
import {WorkflowService} from '../workflow.service';
import {AnalysisMethod, AnalysisMethodHelper} from '../../models/AnalysisMethod';
import {SearchResult} from '../../models/SearchResult';

@Component({
  selector: 'app-concept-name-matches-dialog',
  templateUrl: './concept-name-matches-dialog.component.html',
  styleUrls: ['./concept-name-matches-dialog.component.css']
})
export class ConceptNameMatchesDialog implements AfterViewInit{
  @ViewChild('searchField') searchField: ElementRef;
  taskItem: AnalysisResult;
  concept: Concept;
  analysis: AnalysisResult[] = [];
  equivalent: string = null;
  related: string = null;
  replaced: string = null;
  terms: string = null;
  loadingText: string;

  getMethodName = AnalysisMethodHelper.getName;

  public static open(modalService: NgbModal, taskItem: AnalysisResult) {
    const modalRef = modalService.open(ConceptNameMatchesDialog, { backdrop: 'static', size: 'lg'});
    modalRef.componentInstance.taskItem = taskItem;
    return modalRef;
  }

  constructor(public activeModal: NgbActiveModal,
              public conceptService: ConceptService,
              public workflowService: WorkflowService,
              public logger: LoggerService
              ) { }

  ngAfterViewInit(): void {
    this.conceptService.getConcept(this.taskItem.id)
      .subscribe(
        (result) => this.setConcept(result),
        (error) => this.logger.error(error)
      );
    setTimeout(() => this.searchField.nativeElement.focus());
  }

  setConcept(concept: Concept) {
/*    this.concept = concept;
    this.equivalent = (concept.data.is_equivalent_to) ? concept.data.is_equivalent_to.id : null;
    this.related = (concept.data.is_related_to) ? concept.data.is_related_to.id : null;
    this.replaced = (concept.data.is_replaced_by) ? concept.data.is_replaced_by.id : null;
    this.analyse();*/
  }

  search() {
    this.loadingText = 'Searching...';
    this.analysis = null;
    this.conceptService.search({term: this.terms})
      .subscribe(
        (result) => this.processSearchResults(result),
        (error) => this.logger.error(error)
      )
  }

  processSearchResults(result: SearchResult) {
/*    this.analysis = result
      .results
      .filter( sr => sr.dbid != this.concept.dbid)
      .map<AnalysisResult>(sr =>  ({
          method: AnalysisMethod.MANUAL,
          dbid: sr.dbid,
          id: sr.id,
          name: sr.name
        } as AnalysisResult));*/
  }

  analyse() {
/*
    this.loadingText = 'Analysing...';
    this.analysis = null;
    this.workflowService.analyseDraftConcept(this.concept.data)
      .subscribe(
        (result) => this.analysis = result,
        (error) => this.logger.error(error)
      )
*/
  }

  clear() {
    this.terms = null;
    this.analysis = [];
    this.searchField.nativeElement.focus();
  }

  setEquivalent(item: AnalysisResult) {
    this.equivalent = item.id;
    this.replaced = null;
  }

  setRelated(item: AnalysisResult) {
    this.related = item.id;
  }

  setReplacement(item: AnalysisResult) {
    this.replaced = item.id;
    this.equivalent = null;
  }

  save(resolve: boolean = false) {
/*
    if (this.equivalent) this.concept.data.is_equivalent_to = { id: this.equivalent};
    if (this.related) this.concept.data.is_related_to = { id: this.related};
    if (this.replaced) this.concept.data.is_replaced_by = { id: this.replaced};
*/
    if (resolve)
      this.taskItem.resolved = true;
    this.conceptService.updateConcept(this.concept).subscribe(
      (result) => this.close(resolve),
      (error) => this.logger.error(error)
    )
  }

  close(resolve: boolean = false) {
    this.activeModal.close(resolve);
  }
}
