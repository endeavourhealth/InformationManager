import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {PropertyDomain} from '../../models/definitionTypes/PropertyDomain';

@Component({
  selector: 'app-property-domain-dialog',
  templateUrl: './property-domain-dialog.component.html',
  styleUrls: ['./property-domain-dialog.component.scss']
})
export class PropertyDomainDialogComponent implements OnInit {
  static open(dialog: MatDialog, domain?: PropertyDomain) {
    let dialogRef = dialog.open(PropertyDomainDialogComponent, {disableClose: true, autoFocus: true, width: '50%'});
    if (!domain) {
      domain = {operator: 'OR'} as PropertyDomain;
      dialogRef.componentInstance.createMode = true;
    }
    dialogRef.componentInstance.domain = domain;
    return dialogRef.afterClosed();
  }

  createMode: boolean = false;
  domain: PropertyDomain;
  operators: string[];

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<PropertyDomainDialogComponent>) {
  }

  ngOnInit() {
    this.conceptService.getOperators().subscribe(
      (result) => this.operators = result,
      (error) => this.log.error(error)
    );
  }

  mandatoryFilled() {
    return this.domain.domain;
  }

  ok() {
    this.dialogRef.close(this.domain);
  }

  close() {
    this.dialogRef.close();
  }
}
