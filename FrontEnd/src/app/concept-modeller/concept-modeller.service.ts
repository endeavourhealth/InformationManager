import { Injectable } from '@angular/core';
import {ConceptSummary} from "./models/concept-summary";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class ConceptModellerService {
  private CONCEPTS : ConceptSummary[];

  constructor(private http : Http) {
    this.CONCEPTS = [
      { id: 1, name: 'Observation'},
      { id: 2, name: 'Person'},
      { id: 3, name: 'Encounter'}
    ];
  }

  public findConceptsByName(search : string) : Observable<ConceptSummary[]> {
    let vm = this;
    return vm.http.get("/api/informationManager", {withCredentials : true})
      .map((response) => response.json());
  }

  public getCommonConcepts(limit : number) : Promise<ConceptSummary[]> {
    return Promise.resolve(this.CONCEPTS);
  }
}
