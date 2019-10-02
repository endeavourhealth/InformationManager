import { Injectable } from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Concept} from '../models/Concept';
import {SearchResult} from '../models/SearchResult';
import {IMModel} from '../models/IMModel';

@Injectable()
export class ConceptService {

  constructor(private http: Http) { }

  getModels(): Observable<IMModel[]> {
    return this.http.get('api/models')
      .map((result) => result.json());
  }

  createConcept(modelIri: string, id: string, name: string) {
    const params = new URLSearchParams();
    params.append('id', id);
    params.append('name', name);

    return this.http.post('api/models/' + modelIri + '/concepts', null, {search: params})
      .map((result) => result.text());
  }

  getMRU(): Observable<SearchResult> {
    return this.http.get('api/concepts/mru')
      .map((result) => result.json());
  }

  search(args: {term: string, size?: number, page?: number, models?: any[], relationship?: string, target?: string}): Observable<SearchResult> {
    const params = new URLSearchParams();
    params.append('term', args.term);
    if (args.size) params.append('size', args.size.toString());
    if (args.page) params.append('page', args.page.toString());
    if (args.models) {
      for (let model of args.models)
        params.append('model', model);
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
      .map((result) => result.json());
  }

  updateConcept(concept: Concept): Observable<Concept> {
    const id = concept.concept.id;
    return this.http.post('api/concepts/'+id, concept)
      .map((result) => result.json());
  }

  getCodeSchemes() {
    return this.http.get('api/concepts/schemes')
      .map((result) => result.json());
  }
}
