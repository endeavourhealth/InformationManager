import { Injectable } from '@angular/core';
import {ConceptSummary} from "./models/concept-summary";
import {URLSearchParams, Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {ConceptRelationship} from "./models/concept-relationship";

@Injectable()
export class ConceptModellerService {
  private CONCEPTS : ConceptSummary[];

  constructor(private http : Http) {
    this.CONCEPTS = [
      { id: 1, name: 'Observation', count: 2},
      { id: 2, name: 'Person', count: 9},
      { id: 3, name: 'Encounter', count: 23},
      { id: 4, name: 'Diabetes', count: 42},
      { id: 5, name: 'Diabetes Mellitus', count: 31},
      { id: 6, name: 'Type I Diabetes', count: 3},
      { id: 7, name: 'Type II Diabetes', count: 5},
      { id: 8, name: 'Gender', count: 88},
      { id: 9, name: 'Male', count: 32},
      { id: 10, name: 'Female', count: 53},
      { id: 11, name: 'Penicillin Allergy', count: 21},
      { id: 12, name: 'Asthma', count: 32},
      { id: 13, name: 'Co-codamol', count: 11},
      { id: 14, name: 'Paracetamol', count: 23},
      { id: 15, name: 'Influenza Vaccination', count: 1}
    ];
  }

  public findConceptsByName(search : string) : Observable<ConceptSummary[]> {
    let vm = this;
    let params = new URLSearchParams();
    params.set('conceptName',search);
    return vm.http.get("/api/informationModel", {withCredentials : true, search : params} )
      .map((response) => response.json());
  }

  public getCommonConcepts(limit : number) : Observable<ConceptSummary[]> {
    //return Promise.resolve(this.CONCEPTS);
    let vm = this;
    let params = new URLSearchParams();
    params.set('limit','10');
    console.log('getting common');
    return vm.http.get('/api/informationModel/common', {withCredentials : true, search : params} )
      .map((response) => response.json());
  }

  /*Code added to simply test the API calls...feel free to delete*/

  public addConcept(concept : ConceptSummary) : Observable<any> {
    let vm = this;
    return vm.http.post('/api/informationModel', concept, {withCredentials : true})
      .map((response) => response.toString());
  }

  public populateAllConcepts(): void {
    let vm = this;

    for (let concept of vm.CONCEPTS) {
      vm.addConcept(concept)
        .subscribe(
          (result) => console.log(result),
          (error) => console.log(error)
        );
    }
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
}
