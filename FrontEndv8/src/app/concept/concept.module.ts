import { NgModule } from '@angular/core';
import {ConceptLibraryComponent} from './concept-library/concept-library.component';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {LayoutModule} from '../layout/layout.module';
import {
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatPaginatorModule, MatProgressSpinnerModule,
  MatSortModule,
  MatTableModule
} from '@angular/material';
import {FormsModule} from '@angular/forms';
import { ConceptEditorComponent } from './concept-editor/concept-editor.component';
import {RouterModule} from '@angular/router';



@NgModule({
  declarations: [
    ConceptLibraryComponent,
    ConceptEditorComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,

    LayoutModule,
    MatCardModule,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    FormsModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    RouterModule
  ]
})
export class ConceptModule { }
