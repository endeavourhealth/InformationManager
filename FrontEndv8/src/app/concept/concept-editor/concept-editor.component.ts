import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {MatDialog} from '@angular/material/dialog';
import {ParentHierarchyDialogComponent} from '../parent-hierarchy-dialog/parent-hierarchy-dialog.component';
import {ChildHierarchyDialogComponent} from '../child-hierarchy-dialog/child-hierarchy-dialog.component';
import {Ontology} from '../../models/Ontology';
import {Axiom} from '../../models/Axiom';

@Component({
  selector: 'app-concept-editor',
  templateUrl: './concept-editor.component.html',
  styleUrls: ['./concept-editor.component.scss']
})
export class ConceptEditorComponent implements OnInit {
  createMode: boolean = false;
  locked: boolean = true;
  ontologies: Ontology[];
  concept: Concept;
  axioms: Axiom[];
  selected: any;

  constructor(private route: ActivatedRoute,
              private conceptService: ConceptService,
              private log: LoggerService,
              private dialog: MatDialog
              ) { }

  ngOnInit() {
    this.route.params.subscribe(
      (params) => this.initialize(params['id'])
    );
    this.conceptService.getOntologies().subscribe(
      (result) => this.ontologies = result,
        (error) => this.log.error(error)
    )
  }

  initialize(conceptId: string) {
    if (conceptId)
      this.loadConcept(conceptId);
    else {
      this.createMode = true;
      this.concept = {
        iri: 'NEW_CONCEPT',
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
    this.conceptService.getAxioms(conceptId)
      .subscribe(
        (result) => this.axioms = result,
        (error) => this.log.error(error)
      );
  }

  getName(conceptId : string) : string {
    if (!conceptId)
      return '';
    else
      return this.conceptService.getName(conceptId);
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
