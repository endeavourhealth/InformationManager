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
    if (concept)
      dialogRef.componentInstance.concept = concept;
    else {
      dialogRef.componentInstance.concept = new Concept();
      dialogRef.componentInstance.new = true;
    }
    return dialogRef.afterClosed();
  }

  new: boolean = false;
  concept: Concept;
  completions: Namespace[];
  namespaces: Namespace[];

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<ConceptEditorDialogComponent>) {
    this.conceptService.getNamespaces().subscribe(
      (result) => this.initNamespaces(result),
      (error) => this.log.error(error)
    );
  }

  ngOnInit() {
  }

  initNamespaces(namespaces: Namespace[]) {
    this.namespaces = namespaces;
    this.completions = [];

    for (let ns of namespaces) {
        this.completions.push({
          prefix: ns.prefix,
          name: ns.name,
          iri: ns.prefix + ':'
        });
    }
  }

  iriKeyup(event: any) {
    if (this.concept.iri === event.srcElement.value)
      return;

    this.concept.iri = event.srcElement.value;

    this.completions = [];

    let cursorPos = event.srcElement.selectionStart;
    let colonPos = event.srcElement.value.indexOf(':');

    if (this.namespaces && (colonPos == -1 || cursorPos <= colonPos)) {
      let iri = (colonPos >= 0) ? event.srcElement.value.substring(colonPos) : ':';
      let prefix = (colonPos >= 0) ? event.srcElement.value.substring(0, colonPos) : event.srcElement.value;
      for (let ns of this.namespaces) {
        if (ns.prefix.startsWith(prefix))
        this.completions.push({
          prefix: ns.prefix,
          name: ns.name,
          iri: ns.prefix + iri
        });
      }
    }
  }

  ok() {
/*
    this.conceptService.saveConcept(this.concept).subscribe(
      (result) => this.dialogRef.close(this.concept),
      (error) => this.log.error(error)
    );
*/
    this.dialogRef.close(this.concept);
  }

  close() {
    this.dialogRef.close();
  }

}
