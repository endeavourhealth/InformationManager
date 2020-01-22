import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Concept} from '../models/Concept';
import {ConceptTreeNode} from '../models/ConceptTreeNode';
import {SearchResult} from '../models/SearchResult';
import {Namespace} from '../models/Namespace';
import {Definition} from '../models/Definition';

@Injectable({
  providedIn: 'root'
})
export class ConceptService {
  private _nameCache: any = {};

  constructor(private http: HttpClient) { }

  getMRU(args: {size?: number, supertype?: string}): Observable<SearchResult> {
    let params = new HttpParams();

    if (args.size) params = params.append('size', args.size.toString());
    if (args.supertype) params = params.append('supertype', args.supertype.toString());

    return this.http.get<SearchResult>('api/concepts/mru', {params});
  }

  getNamespaces(): Observable<Namespace[]> {
    return this.http.get<Namespace[]>('api/namespaces');
  }

  getConcept(iri: string): Observable<Concept> {
    return this.http.get<Concept>('api/concepts/' + iri);
  }

  getAxioms(): Observable<string[]> {
    return this.http.get<string[]>('api/concepts/axioms');
  }

  getDefinition(iri: string): Observable<Definition[]> {
    return this.http.get<Definition[]>('api/concepts/' + iri + '/definition');
  }

  search(args: {term: string, supertype?: string, size?: number, page?: number, models?: string[], status?: string[]}): Observable<SearchResult> {
    let params = new HttpParams();
    params = params.append('term', args.term);

    if (args.supertype) params = params.append('supertype', args.supertype);
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

  complete(args: {term: string, models?: string[], status?: string[]}): Observable<string[]> {
    let params = new HttpParams();
    params = params.append('term', args.term);

    if (args.models) {
      for (let item of args.models)
        params = params.append('model', item);
    }
    if (args.status) {
      for (let item of args.status)
        params = params.append('status', item);
    }


    return this.http.get<string[]>('api/concepts/complete', {params});
  }

  completeWord(term: string): Observable<string> {
    let params = new HttpParams();
    params = params.append('term', term);
    return this.http.get('api/concepts/completeWord', {params: params, responseType: 'text'});
  }

  getName(conceptId: string) {
    if (!this._nameCache[conceptId]) {

      this._nameCache[conceptId] = conceptId;

      this.http.get('api/concepts/' + conceptId + '/name', {responseType: 'text'})
        .subscribe(
          (response) => this._nameCache[conceptId] = ( response || conceptId),
          (error) => console.error(error)
        );
    }
    return this._nameCache[conceptId];
  }

  getParentTree(conceptId: string, root?: string): Observable<ConceptTreeNode[]> {
    let params = new HttpParams();

    if (root) params = params.append('root', root);

    return this.http.get<ConceptTreeNode[]>('api/concepts/' + conceptId + '/parentTree', {params: params});
  }

  getParents(conceptId: string): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/concepts/' + conceptId + '/parents');
  }

  getChildren(conceptId: string): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/concepts/' + conceptId + '/children');
  }

  getRootConcepts(): Observable<Concept[]> {
    return this.http.get<Concept[]>('api/concepts/root');
  }

  getConceptHierarchy(conceptId: any): Observable<ConceptTreeNode[]> {
    return this.http.get<ConceptTreeNode[]>('api/concepts/' + conceptId + '/hierarchy')
  }

}
