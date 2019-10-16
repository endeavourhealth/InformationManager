import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QueryBuilderComponent } from './query-builder/query-builder.component';
import {
  MatCardModule, MatDatepickerModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule, MatSelectModule, MatSliderModule,
  MatSortModule,
  MatTableModule,
  MatTabsModule
} from "@angular/material";
import {FormsModule} from '@angular/forms';
import {MAT_DATE_LOCALE, SatDatepickerModule, SatNativeDateModule} from "saturn-datepicker";

@NgModule({
  declarations: [QueryBuilderComponent],
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatSliderModule,
    MatSelectModule,
    SatDatepickerModule,
    SatNativeDateModule,
    MatDatepickerModule,
    FormsModule,
    CommonModule
  ],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'en-GB'},
  ],
})
export class QueryModule { }




