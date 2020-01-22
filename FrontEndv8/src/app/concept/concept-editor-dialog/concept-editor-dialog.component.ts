import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Definition} from '../../models/Definition';
import {Supertype} from '../../models/Supertype';
import {Property} from '../../models/Property';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';

@Component({
  selector: 'app-concept-editor-dialog',
  templateUrl: './concept-editor-dialog.component.html',
  styleUrls: ['./concept-editor-dialog.component.scss']
})
export class ConceptEditorDialogComponent implements OnInit {
  static open(dialog: MatDialog, concept: Concept) {
    let dialogRef = dialog.open(ConceptEditorDialogComponent, {disableClose: true, autoFocus: true, maxWidth: '50%'});
    dialogRef.componentInstance.concept = concept;
    return dialogRef.afterClosed();
  }

  concept: Concept;

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<ConceptEditorDialogComponent>) {
  }

  ngOnInit() {
  }

  ok() {
    // Save to database
    this.dialogRef.close(this.concept);
  }

  close() {
    this.dialogRef.close();
  }

}
