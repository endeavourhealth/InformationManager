import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectLibraryComponent } from './project-library/project-library.component';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {FlexModule} from '@angular/flex-layout';
import {MatRippleModule} from '@angular/material/core';



@NgModule({
  declarations: [ProjectLibraryComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatMenuModule,
    FlexModule,
    MatRippleModule
  ]
})
export class ProjectsModule { }
