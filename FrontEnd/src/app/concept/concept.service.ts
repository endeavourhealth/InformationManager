import { Injectable } from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Concept} from '../models/Concept';
import {SearchResult} from '../models/SearchResult';

@Injectable()
export class ConceptService {

  constructor(private http: Http) { }

  getMRU(): Observable<SearchResult> {
    return this.http.get('api/concepts/mru')
      .map((result) => result.json());
  }

  search(args: {term: string, size?: number, page?: number, documents?: number[], relationship?: string, target?: string}): Observable<SearchResult> {
    const params = new URLSearchParams();
    params.append('term', args.term);
    if (args.size) params.append('size', args.size.toString());
    if (args.page) params.append('page', args.page.toString());
    if (args.documents) {
      for (let doc of args.documents)
        params.append('docDbid', doc.toString());
    }
    if (args.relationship) params.append('relationship', args.relationship);
    if (args.target) params.append('target', args.target);

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

  getRange(id: string): Observable<string> {
    return this.http.get('api/concepts/' + id + '/range')
      .map((result) => result.status == 204 ? null : result.text());
  }

  updateConcept(concept: Concept): Observable<Concept> {
    const id = concept.id;
    return this.http.post('api/concepts/'+id, concept)
      .map((result) => result.json());
  }

  getCodeSchemes() {
    return this.http.get('api/concepts/schemes')
      .map((result) => result.json());
  }
}
