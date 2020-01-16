import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {Axiom} from '../../models/Axiom';

@Component({
  selector: 'app-axiom-picker-dialog',
  templateUrl: './axiom-picker-dialog.component.html',
  styleUrls: ['./axiom-picker-dialog.component.scss']
})
export class AxiomPickerDialogComponent implements OnInit {
  static open(dialog: MatDialog, currentAxioms: Axiom[]) {
    let dialogRef = dialog.open(AxiomPickerDialogComponent, {disableClose: true});
    dialogRef.componentInstance.current = currentAxioms;
    return dialogRef.afterClosed();
  }

  current: Axiom[];
  axioms: string[] = [
    'SubClassOf',
    'SubPropertyOf',
    'EquivalentTo',
    'InversePropertyOf',
    'MappedTo',
    'ReplacedBy',
    'Replaced',
    'ChildOf',
    'MemberOf',
    'PropertyDomain',
    'PropertyRange',
    'Key',
    'PropertyChain'
  ];
  selected: string;

  constructor(
    private conceptService: ConceptService,
    private logger: LoggerService,
    public dialogRef: MatDialogRef<AxiomPickerDialogComponent>) {
  }

  ngOnInit() {
  }

  save() {
    let axiom = new Axiom();
    axiom.token = this.selected;
    this.dialogRef.close(axiom);
  }

  close() {
    this.dialogRef.close();
  }
}
