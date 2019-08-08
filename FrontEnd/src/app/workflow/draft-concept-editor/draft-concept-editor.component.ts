import {AfterViewInit, Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {LoggerService} from 'eds-angular4';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {WorkflowService} from '../workflow.service';
import {Task} from '../../models/Task';
import {ConceptService} from '../../concept/concept.service';
import {StatusHelper} from '../../models/Status';
import {Location} from '@angular/common';
import {DocumentService} from '../../document/document.service';
import {ConceptNameMatchesDialog} from '../concept-name-matches-dialog/concept-name-matches-dialog.component';

@Component({
  selector: 'app-draft-concept-editor',
  templateUrl: './draft-concept-editor.component.html',
  styleUrls: [
    './draft-concept-editor.component.css',
  ]
})
export class DraftConceptEditor implements AfterViewInit {
  task: Task;
  private statusName = StatusHelper.getName;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private logger: LoggerService,
              private location: Location,
              private workflowService: WorkflowService,
              private conceptService: ConceptService,
              private documentService: DocumentService,
              private modal: NgbModal) { }

  ngAfterViewInit() {
    this.route.params.subscribe(
      (params) => this.loadTask(params['id'])
    );
  }

  loadTask(dbid: number) {
    this.workflowService.getTask(dbid)
      .subscribe(
        (result) => this.task = result,
        (error) => this.logger.error(error)
      );
  }

  select(item: any) {
    ConceptNameMatchesDialog.open(this.modal, item)
      .result.then(
      (result) => { if (result) this.updateTask(); },
      (error) => this.logger.error(error)
    );
/*    this.selected = item;
    this.analysisResults = null;
    this.workflowService.analyseDraftConcept(item)
      .subscribe(
        (result) => this.analysisResults = result,
        (error) => this.logger.error(error)
      );*/
  }

  updateTask() {
    this.workflowService.updateTask(this.task)
      .subscribe(
        (result) => {},
        (error) => this.logger.error(error)
      );
  }

/*
  actionAvailable(item: AnalysisResult) {
    if (item.method == AnalysisMethod.CONCEPT_ID)
      return true;
    if (item.method == AnalysisMethod.SCHEME_CODE && item.matches)
      return true;
    if (item.method == AnalysisMethod.NAME && item.matches)
      return true;

    return false;
  }

  getActionName(item: AnalysisResult) {
    if (item.method == AnalysisMethod.CONCEPT_ID)
      return 'Edit';
    if (item.method == AnalysisMethod.SCHEME_CODE && item.matches)
      return 'Match';
    if (item.method == AnalysisMethod.NAME && item.matches)
      return 'View';

    return null;
  }

  actionItem(item: AnalysisResult) {
    switch (item.method) {
      case AnalysisMethod.CONCEPT_ID:
        this.actionItemConceptId(item);
        break;
      case AnalysisMethod.NAME:
        this.actionItemName(item);
        break;
    }
  }


  actionItemConceptId(item: AnalysisResult) {
      this.router.navigate(['concept', item.matches[0].id]);
  }

  actionItemName(item: AnalysisResult) {
    ConceptNameMatchesDialog.open(this.modal, item)
      .result.then(
      (result) => {},
      (error) => this.logger.error(error)
    );
  }
*/
  close() {
    this.location.back();
  }

}
