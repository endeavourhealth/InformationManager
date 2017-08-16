import { Injectable } from '@angular/core';
import {URLSearchParams, Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {ConceptRelationship} from "../concept-modeller/models/concept-relationship";

@Injectable()
export class ConceptPickerService {
  constructor(private http : Http) {
  }

  public getRelationships(conceptId: number): Observable<ConceptRelationship[]> {
    let vm = this;

    let params = new URLSearchParams();
    params.set('conceptId', conceptId.toString());
    return vm.http.get('/api/informationModel/relationships', {withCredentials : true, search : params})
      .map((response) => response.json());
  }
}
