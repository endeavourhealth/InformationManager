import { Injectable } from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {TaskCategory} from '../models/TaskCategory';
import {TaskSummary} from '../models/TaskSummary';
import {Task} from '../models/Task';
import {Concept} from '../models/Concept';

@Injectable()
export class WorkflowService {

  constructor(private http: Http) { }

  getCategories(): Observable<TaskCategory[]> {
    return this.http.get('api/workflow/categories')
      .map((result) => result.json());
  }

  getSummary(categoryDbid: number): Observable<TaskSummary[]> {
    return this.http.get('api/workflow/categories/' + categoryDbid + '/tasks/summary')
      .map((result) => result.json());
  }

  getTask(dbid: number): Observable<Task> {
    return this.http.get('api/workflow/tasks/' + dbid)
      .map((result) => result.json());
  }

  analyseDraftConcept(item: Concept) {
    return this.http.post('api/workflow/draftConcept/analyse', item)
      .map((result) => result.json());
  }

  updateTask(task: Task) {
    return this.http.post('api/workflow/tasks/' + task.dbid, task.data)
      .map((result) => result.text());
  }
}
