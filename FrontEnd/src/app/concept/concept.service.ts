import { Injectable } from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Concept} from '../models/Concept';
import {SearchResult} from '../models/SearchResult';
import {ConceptProperty} from '../models/ConceptProperty';
import {ConceptDomain} from '../models/ConceptDomain';

@Injectable()
export class ConceptService {

  constructor(private http: Http) { }

  getMRU(): Observable<SearchResult> {
    return this.http.get('api/concepts/mru')
      .map((result) => result.json());
  }

  search(searchTerm: string, size?: number, page?: number, documents?: number[]): Observable<SearchResult> {
    const params = new URLSearchParams();
    params.append('term', searchTerm);
    if (size) params.append('size', size.toString());
    if (page) params.append('page', page.toString());
    if (documents) {
      for (let doc of documents)
        params.append('docDbid', doc.toString());
    }

    return this.http.get('api/concepts/search', {search: params})
      .map((result) => result.json());
  }

  getConcept(id: string): Observable<Concept> {
    return this.http.get('api/concepts/' + id)
      .map((result) => result.json());
  }

  getName(id: string): Observable<string> {
    return this.http.get('api/concepts/' + id + '/name')
      .map((result) => result.status == 204 ? null : result.text());
  }

  updateConcept(concept: Concept): Observable<Concept> {
    const id = concept.id;
    return this.http.post('api/concepts/'+id, concept)
      .map((result) => result.json());
  }
}
