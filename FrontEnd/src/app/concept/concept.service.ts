import { Injectable } from '@angular/core';
import {URLSearchParams, Http} from '@angular/http';
import {Concept} from '../models/concept';
import {Observable} from 'rxjs/Observable';
import {ConceptRelationship} from '../models/concept-relationship';
import {ConceptAndRelationships} from '../models/concept-and-relationships';

@Injectable()
export class ConceptService {

  private static conceptName: any = {};

  constructor(private http: Http) { }

  state: any;

  public listConcepts(classIds: number[], page: number, pageSize: number, filter?: string): Observable<Concept[]> {
    const params: URLSearchParams = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', pageSize.toString());
    for (let classId of classIds)
      params.append('classId', classId.toString());

    if (filter)
      params.append('filter', filter);

    return this.http.get('api/concept/list', {search: params, withCredentials: true})
      .map((response) => response.json());
  }

  public getConceptCount(classIds: number[], filter?: string): Observable<number> {
    const params: URLSearchParams = new URLSearchParams();
    for (let classId of classIds)
      params.append('classId', classId.toString());

    if (filter)
      params.append('filter', filter);

    return this.http.get('api/concept/count', {search: params, withCredentials: true})
      .map((response) => parseInt(response.text()));
  }

  public getRelated(conceptId: number): Observable<ConceptRelationship[]> {
    const params: URLSearchParams = new URLSearchParams();
    params.append('conceptId', conceptId.toString());

    return this.http.get('api/relationship', {search: params, withCredentials: true})
      .map((response) => response.json());

  }

  public getConceptName(conceptId: number): Observable<string> {
    const params: URLSearchParams = new URLSearchParams();
    params.append('conceptId', conceptId.toString());

    return this.http.get('api/concept/name', {search: params, withCredentials: true})
      .map((response) => response.text());
  }

  public getCachedConceptName(conceptId: number): string {
    if (!ConceptService.conceptName[conceptId]) {
      ConceptService.conceptName[conceptId] = 'Loading...';
      this.getConceptName(conceptId).subscribe(
        (result) => ConceptService.conceptName[conceptId] = result,
        (error) => console.log(error)
      );
    }

    return ConceptService.conceptName[conceptId];
  }

  public getStatusName(status: number) {
    switch (status) {
      case 0: return 'Draft';
      case 1: return 'Active';
      case 2: return 'Deprecated';
      default: return 'UNKNOWN!';
    }
  }

  public getCardinalityName(cardinality: number) {
    switch(cardinality) {
      case null: return 'Unlimited';
      case 0: return 'Unlimited';
      case 1: return 'Single';
      default: return 'Up to ' + cardinality;
    }
  }

  public save(concept: Concept, related: ConceptRelationship[]) : Observable<any> {
    const conceptAndRelationships: ConceptAndRelationships = {
      concept: concept,
      related: related
    };
    return this.http.post('api/concept/andRelationships', conceptAndRelationships,{withCredentials: true});
  }

  getConcept(conceptId: string) : Observable<Concept> {
    const params: URLSearchParams = new URLSearchParams();
    params.append('conceptId', conceptId.toString());

    return this.http.get('api/concept', {search: params, withCredentials: true})
      .map((response) => response.json());
  }
}
