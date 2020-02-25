import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {PropertyRange} from '../../models/definitionTypes/PropertyRange';

@Component({
  selector: 'app-property-range-dialog',
  templateUrl: './property-range-dialog.component.html',
  styleUrls: ['./property-range-dialog.component.scss']
})
export class PropertyRangeDialogComponent implements OnInit {
  static open(dialog: MatDialog, range?: PropertyRange) {
    let dialogRef = dialog.open(PropertyRangeDialogComponent, {disableClose: true, autoFocus: true, width: '50%'});
    if (!range) {
      range = {subsumption: '<', operator: 'OR'} as PropertyRange;
      dialogRef.componentInstance.createMode = true;
    }
    dialogRef.componentInstance.range = range;
    return dialogRef.afterClosed();
  }

  createMode: boolean = false;
  range: PropertyRange;
  operators: string[];

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<PropertyRangeDialogComponent>) {
  }

  ngOnInit() {
    this.conceptService.getOperators().subscribe(
      (result) => this.operators = result,
      (error) => this.log.error(error)
    );
  }

  mandatoryFilled() {
    return true;
  }

  ok() {
    this.dialogRef.close(this.range);
  }

  close() {
    this.dialogRef.close();
  }
}
