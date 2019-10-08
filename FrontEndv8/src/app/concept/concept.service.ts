import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {Concept} from '../models/Concept';

@Injectable({
  providedIn: 'root'
})
export class ConceptService {

  constructor(private http: HttpClient) { }

  getMRU(): Observable<any> {
    return this.http.get('api/concepts/mru');
  }

  getConcept(conceptId: string): Observable<Concept> {
    return this.http.get<Concept>('api/concepts/' + conceptId);
  }
}
