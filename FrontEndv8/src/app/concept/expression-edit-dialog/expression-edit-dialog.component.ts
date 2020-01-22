import { Component, OnInit } from '@angular/core';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Definition} from '../../models/Definition';
import {Supertype} from '../../models/Supertype';
import {Property} from '../../models/Property';

@Component({
  selector: 'app-expression-edit-dialog',
  templateUrl: './expression-edit-dialog.component.html',
  styleUrls: ['./expression-edit-dialog.component.scss']
})
export class ExpressionEditDialogComponent implements OnInit {
  static open(dialog: MatDialog, definition: Definition, expression: Supertype | Property) {
    let dialogRef = dialog.open(ExpressionEditDialogComponent, {disableClose: true, autoFocus: true});
    dialogRef.componentInstance.definition = definition;
    dialogRef.componentInstance.expression = expression;
    return dialogRef.afterClosed();
  }

  definition: Definition;
  expression: Supertype | Property;

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<ExpressionEditDialogComponent>) {
  }

  ngOnInit() {
  }

  getName(conceptId : string) : string {
    if (!conceptId)
      return '';
    else
      return this.conceptService.getName(conceptId);
  }

  ok() {
    this.dialogRef.close();
  }

  close() {
    this.dialogRef.close();
  }

}
