import { Injectable } from '@angular/core';
import {AbstractMockObservable} from "./mock.observable";
import {Clazz} from "../common/clazz";
import {ConceptSummary} from "../concept-modeller/models/concept-summary";
import {ConceptRelationship} from "../concept-modeller/models/concept-relationship";

@Injectable()
export class MockConceptModellerService extends AbstractMockObservable {

  findConceptsById (id : number) {
    this._fakeContent = <ConceptSummary>{
      id: id,
      name: 'Mock concept name',
      clazz: Clazz.Concept,
      description: 'Mock concept description',
      shortName: 'MCKCNCPTSHRTNM'
    };

    return this;
  }

  getRelationships(conceptId: number) {
    this._fakeContent = <ConceptRelationship[]>[];

    return this;
  }

  getClasses() {
    this._fakeContent = <ConceptSummary[]>[];

    return this;
  }

  getRelationshipTypes() {
    this._fakeContent = <ConceptSummary[]>[];

    return this;
  }

  getCommonConcepts() {
    this._fakeContent = <ConceptSummary[]>[];

    return this;
  }

  findConceptsByName(search: string, pageNumber: number = 1, pageSize: number = 10, excludeCore : boolean = false) {
    this._fakeContent = <ConceptSummary[]>[];

    return this;
  }

  getConceptSearchTotalCount(search: string) {
    this._fakeContent = 15;

    return this;
  }

}
