import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {Namespace} from '../../models/Namespace';

@Component({
  selector: 'app-concept-editor-dialog',
  templateUrl: './concept-editor-dialog.component.html',
  styleUrls: ['./concept-editor-dialog.component.scss']
})
export class ConceptEditorDialogComponent implements OnInit {
  static open(dialog: MatDialog, concept?: Concept) {
    let dialogRef = dialog.open(ConceptEditorDialogComponent, {disableClose: true, autoFocus: true, maxWidth: '50%'});
    if (concept) {
      concept = Object.assign({}, concept); // Duplicate so its not bound to the background UI
    } else {
      dialogRef.componentInstance.createMode = true;
      concept = new Concept();
      concept.status = 'cm:DraftStatus';
    }
    dialogRef.componentInstance.concept = concept;
    return dialogRef.afterClosed();
  }

  createMode: boolean = false;
  concept: Concept;
  namespace: Namespace;
  namespaces: Namespace[];
  statuses: Concept[];

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<ConceptEditorDialogComponent>) {
    this.conceptService.getNamespaces().subscribe(
      (result) => this.namespaces = result,
      (error) => this.log.error(error)
    );
    this.conceptService.getDescendents('cm:ActiveInactive').subscribe(
      (result) => this.statuses = result,
      (error) => this.log.error(error)
    );
  }

  ngOnInit() {
  }

  getIri() {
    if (!this.namespace)
      return '';

    if (this.concept.code)
      return this.namespace.prefix + ':' + this.concept.code;

    if (this.namespace.codePrefix)
      return this.namespace.prefix + ':' + this.namespace.codePrefix + '<Generate>';
    else
      return this.namespace.prefix + ':' + '<Generate>';
  }

  mandatoryFilled() {
    return this.namespace && this.concept.name;
  }

  ok() {
    if (this.concept.code)
      this.concept.iri = this.getIri();
    else
      this.concept.iri = this.namespace.prefix;

    this.dialogRef.close(this.concept);
  }

  close() {
    this.dialogRef.close();
  }
}
