import { Injectable } from '@angular/core';
import {ConceptTreeViewService, DataModelNavigatorService} from 'im-common/im-controls';
import {Observable} from 'rxjs';
import {Concept} from '../models/Concept';
import {Related} from 'im-common/im-controls/models/Related';
import {Property} from 'im-common/im-controls/models/Property';
import {PagedResultSet} from 'im-common/im-controls/models/PagedResultSet';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RecordStructureService implements ConceptTreeViewService, DataModelNavigatorService{

  constructor(private http: HttpClient) { }

  getConcept(iri: string): Observable<Concept> {
    return this.http.get<Concept>('api/concepts/' + iri);
  }

  getDefinition(iri: string): Observable<Related[]> {
    return this.http.get<Related[]>('api/concepts/' + iri + '/Definition');
  }

  getProperties(iri: string, inherited: boolean = false): Observable<Property[]> {
    let params = new HttpParams();
    if (inherited) {
      params = params.append('inherited', 'true');
    }

    return this.http.get<Property[]>('api/concepts/' + iri + '/Properties', {params});
  }

  getSources(iri: string, relationships: string[], limit: number = 0, page: number = 1): Observable<PagedResultSet<Related>> {
    let params = new HttpParams();
    if (relationships != null) {
      relationships.forEach(r => params = params.append('relationship', r));
    }
    params = params.append('limit', limit.toString());
    params = params.append('page', page.toString());

    return this.http.get<PagedResultSet<Related>>('api/concepts/' + iri + '/Sources', {params});
  }

  getTargets(iri: string, relationships: string[], limit: number = 0, page: number = 1): Observable<PagedResultSet<Related>> {
    let params = new HttpParams();
    if (relationships != null) {
      relationships.forEach(r => params = params.append('relationship', r));
    }
    params = params.append('limit', limit.toString());
    params = params.append('page', page.toString());

    return this.http.get<PagedResultSet<Related>>('api/concepts/' + iri + '/Targets', {params});
  }

  loadTree(root: string, iri: string, relationships: string[]) {
    let params = new HttpParams();
    params = params.append('root', root);
    if (relationships != null) {
      relationships.forEach(r => params = params.append('relationship', r));
    }

    return this.http.get<Related[]>('api/concepts/' + iri + '/Tree', {params});
  }
}
