import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QueryBuilderComponent } from './query-builder/query-builder.component';
import {
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule, MatOption, MatSelect, MatSelectModule, MatSliderModule,
  MatSortModule,
  MatTableModule,
  MatTabsModule
} from "@angular/material";



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
    CommonModule
  ]
})
export class QueryModule { }




