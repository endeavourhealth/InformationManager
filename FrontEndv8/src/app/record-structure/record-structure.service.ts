import { Injectable } from '@angular/core';
import {ConceptTreeViewService, DataModelNavigatorService} from 'im-common/im-controls';
import {Observable} from 'rxjs';
import {Concept} from '../models/Concept';
import {Related} from 'im-common/im-controls/models/Related';
import {PagedResultSet} from 'im-common/im-controls/models/PagedResultSet';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RecordStructureService implements ConceptTreeViewService {

  constructor(private http: HttpClient) { }

  getConcept(iri: string): Observable<Concept> {
    return this.http.get<Concept>('api/concepts/' + iri);
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

  loadTree(root: string, iri: string, relationships: string[]) {
    let params = new HttpParams();
    params = params.append('root', root);
    if (relationships != null) {
      relationships.forEach(r => params = params.append('relationship', r));
    }

    return this.http.get<Related[]>('api/concepts/' + iri + '/Tree', {params});
  }
}
