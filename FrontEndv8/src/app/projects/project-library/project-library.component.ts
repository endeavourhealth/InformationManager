import { Component, OnInit } from '@angular/core';
import {ProjectService} from '../project.service';
import {LoggerService} from 'dds-angular8';
import {Project} from '../../models/Project';

@Component({
  selector: 'app-project-library',
  templateUrl: './project-library.component.html',
  styleUrls: ['./project-library.component.scss']
})
export class ProjectLibraryComponent implements OnInit {
  projects: Project[];

  constructor(private projectService: ProjectService,
              private log: LoggerService) { }

  ngOnInit() {
    this.projectService.getProjects().subscribe(
      (result) => this.projects = result,
      (error) => this.log.error(error)
    );
  }

  promptCreate() {

  }
}
