import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-input-box-dialog',
  templateUrl: './input-box-dialog.component.html',
  styleUrls: ['./input-box-dialog.component.scss']
})
export class InputBoxDialogComponent implements OnInit {
  static open(dialog: MatDialog, title: string, prompt: string, ok: string, cancel: string) {
    return dialog.open(InputBoxDialogComponent, {data: {title: title, prompt: prompt, ok: ok, cancel: cancel}, disableClose: true}).afterClosed();
  }

  value: string;

  constructor(
    public dialogRef: MatDialogRef<InputBoxDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {title: string, prompt: string, ok: string, cancel: string}
  ) { }

  ngOnInit() {
  }

  closeOk() {
    this.dialogRef.close(this.value);
  }

  closeCancel() {
    this.dialogRef.close(null);
  }
}
