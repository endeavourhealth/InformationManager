import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Namespace} from '../models/Namespace';
import {Concept} from '../models/Concept';
import {SearchResult} from '../models/SearchResult';
import {ConceptTreeNode} from '../models/ConceptTreeNode';
import {SimpleConcept} from '../models/definitionTypes/SimpleConcept';
import {PropertyDefinition} from '../models/definitionTypes/PropertyDefinition';
import {Axiom} from '../models/Axiom';
import {PropertyRange} from '../models/definitionTypes/PropertyRange';
import {PropertyDomain} from '../models/definitionTypes/PropertyDomain';
import {ConceptAxiom} from '../models/ConceptAxiom';

@Injectable({
  providedIn: 'root'
})
export class ConceptService {
  private _nameCache: any = {};

  constructor(private http: HttpClient) { }

  // Concept specific
  getMRU(args: {size?: number, supertypes?: string[]}): Observable<SearchResult> {
    let params = new HttpParams();

    if (args.size) params = params.append('size', args.size.toString());
    if (args.supertypes) {
      for (let item of args.supertypes)
        params = params.append('supertype', item);
    }

    return this.http.get<SearchResult>('api/concepts/mru', {params: params});
  }













  getNamespaces(): Observable<Namespace[]> {
    return this.http.get<Namespace[]>('api/namespaces');
  }

  complete(args: {term: string, superTypeFilter?: string[]}): Observable<Concept[]> {
    let params = new HttpParams();
    params = params.append('term', args.term);

    if (args.superTypeFilter) {
      for (let item of args.superTypeFilter)
        params = params.append('superType', item);
    }

    return this.http.get<Concept[]>('api/complete', {params});
  }

  completeWord(term: string): Observable<string> {
    let params = new HttpParams();
    params = params.append('term', term);
    return this.http.get('api/completeWord', {params: params, responseType: 'text'});
  }

  getOperators(): Observable<string[]> {
    return this.http.get<string[]>('api/operators');
  }

  getStatusList(): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/statuses');
  }



  getConcept(iri: string): Observable<Concept> {
    return this.http.get<Concept>('api/concepts/' + iri);
  }

  getAssertedDefinition(iri: string): Observable<any> {
    return this.http.get<any>('api/concepts/' + iri + '/asserted');
  }

  search(args: {term: string, supertypes?: string[], size?: number, page?: number, models?: string[], status?: string[]}): Observable<SearchResult> {
    let params = new HttpParams();
    params = params.append('term', args.term);

    if (args.supertypes) {
      for (let item of args.supertypes)
      params = params.append('supertype', item);
    }
    if (args.size) params = params.append('size', args.size.toString());
    if (args.page) params = params.append('page', args.page.toString());
    if (args.models) {
      for (let item of args.models)
        params = params.append('model', item);
    }
    if (args.status) {
      for (let item of args.status)
        params = params.append('status', item);
    }


    return this.http.get<SearchResult>('api/concepts/search', {params});
  }

  getName(iri: string) {
    if (!this._nameCache[iri]) {

      this._nameCache[iri] = iri;

      this.http.get('api/concepts/' + iri + '/name', {responseType: 'text'})
        .subscribe(
          (response) => this._nameCache[iri] = ( response || iri),
          (error) => console.error(error)
        );
    }
    return this._nameCache[iri];
  }








  // Common
  getAxioms(iri: string): Observable<ConceptAxiom[]> {
    return this.http.get<ConceptAxiom[]>('api/concepts/' + iri + '/axioms');
  }

  getParentTree(id: number, root?: string): Observable<ConceptTreeNode[]> {
    let params = new HttpParams();

    if (root) params = params.append('root', root);

    return this.http.get<ConceptTreeNode[]>('api/concepts/' + id + '/parentTree', {params: params});
  }

  getParents(id: number): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/concepts/' + id + '/parents');
  }

  getChildren(id: number): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/concepts/' + id + '/children');
  }

  getRootConcepts(): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/concepts/root');
  }

  getConceptHierarchy(id: number): Observable<ConceptTreeNode[]> {
    return this.http.get<ConceptTreeNode[]>('api/concepts/' + id + '/hierarchy')
  }

  getDescendents(id: number): Observable<Concept[]> {
    return this.getChildren(id);
  }

  createConcept(concept: Concept): Observable<string> {
    return this.http.post<string>('api/concepts', concept, { responseType: 'text' as 'json'});
  }

  updateConcept(concept: Concept) {
    return this.http.put('api/concepts', concept);
  }

  getAncestors(id: number): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/concepts/' + id + '/ancestors');
  }


  addAxiomSupertype(id: number, axiomToken: string, definition: SimpleConcept) {
    return this.http.post('api/concepts/' + id + '/' + axiomToken + '/supertypes', definition);
  }

  addAxiomRoleGroupProperty(id: number, axiomToken: string, definition: PropertyDefinition, group: number=0) {
    return this.http.post('api/concepts/' + id + '/' + axiomToken + '/rolegroups/' + group , definition);
  }

  deleteAxiomSupertype(id: number, supertypeId: number) {
    return this.http.delete('api/concepts/' + id + '/supertypes/' + supertypeId);
  }

  deleteAxiomRoleGroupProperty(id: number, definition: PropertyDefinition) {
    if (definition.object)
      return this.http.delete('api/concepts/' + id + '/property/object/' + definition.id);
    else
      return this.http.delete('api/concepts/' + id + '/property/data/' + definition.id);
  }

  deleteAxiomRoleGroup(id: number, axiom: Axiom, group: number) {
    return this.http.delete('api/concepts/' + id + '/' + axiom.token + '/rolegroups/' + group);
  }

  deleteAxiom(id: number, axiom: Axiom) {
    return this.http.delete('api/concepts/' + id + '/' + axiom.token);
  }

  deleteConcept(id: number) {
    return this.http.delete('api/concepts/' + id);
  }

  addPropertyRange(id: number, propertyRange: PropertyRange) {
    return this.http.post('api/concepts/' + id + '/propertyrange', propertyRange);
  }

  delPropertyRange(id: number, propertyRangeId: number) {
    return this.http.delete('api/concepts/' + id + '/propertyrange/' + propertyRangeId);
  }

  addPropertyDomain(id: number, propertyDomain: PropertyDomain) {
    return this.http.post('api/concepts/' + id + '/propertydomain', propertyDomain);
  }

  delPropertyDomain(id: number, propertyDomainId: number) {
    return this.http.delete('api/concepts/' + id + '/propertydomain/' + propertyDomainId);
  }
}
