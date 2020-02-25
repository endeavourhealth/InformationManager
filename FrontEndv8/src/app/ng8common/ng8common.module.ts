import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {InputBoxDialogComponent} from './input-box-dialog/input-box-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {FlexModule} from '@angular/flex-layout';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    InputBoxDialogComponent,
  ],
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FlexModule,
    FormsModule
  ],
  exports: [],
  entryComponents: [
    InputBoxDialogComponent,
  ]
})
export class Ng8commonModule { }
