  import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {Model} from '../../models/Model';
import {LoggerService} from 'dds-angular8';
  import {MatDialog} from '@angular/material/dialog';
  import {ParentHierarchyDialogComponent} from '../parent-hierarchy-dialog/parent-hierarchy-dialog.component';
  import {ChildHierarchyDialogComponent} from '../child-hierarchy-dialog/child-hierarchy-dialog.component';
  import {ConceptRelation} from '../../models/ConceptRelation';
  import {combineLatest} from 'rxjs';
  import {map} from 'rxjs/operators';

@Component({
  selector: 'app-concept-editor',
  templateUrl: './concept-editor.component.html',
  styleUrls: ['./concept-editor.component.scss']
})
export class ConceptEditorComponent implements OnInit {
  createMode: boolean = false;
  locked: boolean = true;
  models: Model[];
  concept: Concept;
  conceptRelations: ConceptRelation[];

  constructor(private route: ActivatedRoute,
              private conceptService: ConceptService,
              private log: LoggerService,
              private dialog: MatDialog
              ) { }

  ngOnInit() {
    this.route.params.subscribe(
      (params) => this.initialize(params['id'])
    );
    this.conceptService.getModels().subscribe(
      (result) => this.models = result,
        (error) => this.log.error(error)
    )
  }

  initialize(conceptId: string) {
    if (conceptId)
      this.loadConcept(conceptId);
    else {
      this.createMode = true;
      this.concept = {
        id: 'NEW_CONCEPT',
        name: '<new concept>',
        status: 'CoreDraft'
      } as Concept;
    }
  }

  loadConcept(conceptId: string) {
    this.conceptService.getConcept(conceptId)
      .subscribe(
        (result) => this.concept = result,
        (error) => this.log.error(error)
      );

    this.conceptService.getConceptRelations(conceptId)
      .subscribe(
        (result) => this.conceptRelations = result,
        (error) => this.log.error(error)
      );
  }

  parentHierarchy() {
    this.dialog.open(ParentHierarchyDialogComponent, {disableClose: true, data: this.concept}).afterClosed().subscribe(
      () => {},
      (error) => this.log.error(error)
    )
  }

  childHierarchy() {
    this.dialog.open(ChildHierarchyDialogComponent, {disableClose: true, data: this.concept}).afterClosed().subscribe(
      () => {},
      (error) => this.log.error(error)
    )
  }


}
