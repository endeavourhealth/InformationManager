import { Injectable } from '@angular/core';
import {ConceptSummary} from "./models/concept-summary";
import {URLSearchParams, Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {ConceptRelationship} from "./models/concept-relationship";

@Injectable()
export class ConceptModellerService {
  constructor(private http : Http) {
  }

  public findConceptsByName(search: string, pageNumber: number = 1, pageSize: number = 10, excludeCore : boolean = false): Observable<ConceptSummary[]> {
    let vm = this;
    let params = new URLSearchParams();
    params.set('conceptName',search);
    params.set('pageNumber', pageNumber.toString());
    params.set('pageSize', pageSize.toString());
    params.set('excludeCore', excludeCore.toString());
    return vm.http.get('/api/informationModel', {withCredentials : true, search : params} )
      .map((response) => response.json());
  }

  public getConceptSearchTotalCount(search: string): Observable<number> {
    const vm = this;
    const params = new URLSearchParams();
    params.set('conceptName', search);
    return vm.http.get('/api/informationModel/searchCount', {withCredentials : true, search : params} )
      .map((response) => response.json());
  }

  public findConceptsById(id: number): Observable<ConceptSummary> {
    let vm = this;
    let params = new URLSearchParams();
    params.set('conceptId', id.toString());
    return vm.http.get('/api/informationModel', {withCredentials : true, search : params} )
      .map((response) => response.json());
  }

  public getCommonConcepts(limit : number) : Observable<ConceptSummary[]> {
    let vm = this;
    let params = new URLSearchParams();
    params.set('limit','10');
    return vm.http.get('/api/informationModel/common', {withCredentials : true, search : params} )
      .map((response) => response.json());
  }

  /*Code added to simply test the API calls...feel free to delete*/

  public saveConcept(concept : ConceptSummary) : Observable<any> {
    let vm = this;
    return vm.http.post('/api/informationModel', concept, {withCredentials : true})
      .map((response) => response.json());
  }

  public deleteConcept(conceptId: number): Observable<any> {
    let vm = this;

    let params = new URLSearchParams();
    params.set('conceptId', conceptId.toString());
    return vm.http.delete('/api/informationModel', {withCredentials : true, search : params})
      .map((response) => response.toString());
  }

  public deleteConceptRelationship(conceptRelationshipId: number): Observable<any> {
    let vm = this;

    let params = new URLSearchParams();
    params.set('conceptRelationshipId', conceptRelationshipId.toString());
    return vm.http.delete('/api/informationModel/relationships', {withCredentials : true, search : params})
      .map((response) => response.toString());
  }

  public addRelationship(conceptRelationship: ConceptRelationship): Observable<any> {
    let vm = this;

    return vm.http.post('/api/informationModel/relationships', conceptRelationship)
      .map((response) => response.toString());
  }

  public getRelationships(conceptId: number): Observable<ConceptRelationship[]> {
    let vm = this;

    let params = new URLSearchParams();
    params.set('conceptId', conceptId.toString());
    return vm.http.get('/api/informationModel/relationships', {withCredentials : true, search : params})
      .map((response) => response.json());
  }

  public getRelationshipTypes(): Observable<ConceptSummary[]> {
    let vm = this;

    return vm.http.get('/api/informationModel/relationshipConcepts', {withCredentials : true})
      .map((response) => response.json());
  }

  public getClasses(): Observable<ConceptSummary[]> {
    let vm = this;

    return vm.http.get('/api/informationModel/classConcepts', {withCredentials : true})
      .map((response) => response.json());
  }
}
