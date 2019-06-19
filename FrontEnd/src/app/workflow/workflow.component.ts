import {Component, OnInit} from '@angular/core';
import {LoggerService} from 'eds-angular4';
import {TaskCategory} from '../models/TaskCategory';
import {WorkflowService} from './workflow.service';
import {TaskSummary} from '../models/TaskSummary';
import {Router} from '@angular/router';
import {StatusHelper} from '../models/Status';

@Component({
  selector: 'app-workflow-list',
  templateUrl: './workflow.component.html',
  styleUrls: ['./workflow.component.css']
})
export class WorkflowComponent implements OnInit {
  selected: TaskCategory;
  taskCategories: TaskCategory[];
  tasks: TaskSummary[];

  statusName = StatusHelper.getName;

  constructor(
    private router: Router,
    private workflowService: WorkflowService,
    private log: LoggerService
  ) { }

  ngOnInit() {
    this.getCategories();
  }

  getCategories() {
    this.taskCategories = null;
    this.workflowService.getCategories()
      .subscribe(
        (result) => this.taskCategories = result,
        (error) => this.log.error(error)
      );
  }

  select(item: TaskCategory) {
    this.selected = item;
    this.workflowService.getSummary(item.dbid)
      .subscribe(
        (result) => this.tasks = result,
        (error) => this.log.error(error)
      );
  }

  view(item: TaskSummary) {
    switch (item.category) {
      case 0:
        this.router.navigate(['workflow/draft-concept', item.dbid]);
        break;
      case 1:
        this.router.navigate(['workflow/draft-term-map', item.dbid]);
        break;
    }
  }
}
