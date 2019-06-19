import {AfterViewInit, Component} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AnalysisResult} from '../../models/AnalysisResult';
import {ConceptService} from '../../concept/concept.service';
import {ConceptSummary} from '../../models/ConceptSummary';
import {LoggerService} from 'eds-angular4';
import {Concept} from '../../models/Concept';
import {WorkflowService} from '../workflow.service';
import {AnalysisMethod, AnalysisMethodHelper} from '../../models/AnalysisMethod';

@Component({
  selector: 'app-concept-name-matches-dialog',
  templateUrl: './concept-name-matches-dialog.component.html',
  styleUrls: ['./concept-name-matches-dialog.component.css']
})
export class ConceptNameMatchesDialog implements AfterViewInit{
  taskItem: ConceptSummary;
  concept: Concept;
  analysis: AnalysisResult[];

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
        (result) => this.concept = result,
        (error) => this.logger.error(error)
      );
    this.workflowService.analyseDraftConcept(this.taskItem)
      .subscribe(
        (result) => this.analysis = result,
        (error) => this.logger.error(error)
      )
  }

  close() {
    this.activeModal.close(null);
  }
}
