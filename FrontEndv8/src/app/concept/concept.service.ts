import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ConceptService {

  constructor(private http: HttpClient) { }

  getMRU(): Observable<any> {
    return this.http.get('api/concepts/mru');
  }
}
