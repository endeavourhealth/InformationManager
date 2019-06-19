import { Injectable } from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {IMDocument} from '../models/IMDocument';

@Injectable()
export class DocumentService {

  constructor(private http: Http) { }

  getDocuments(): Observable<IMDocument[]> {
    return this.http.get('api/documents')
      .map((result) => result.json());
  }

  getDocumentPending(dbid: number) {
    return this.http.get('api/documents/' + dbid + '/pending')
      .map((result) => result.json());
  }

  publish(dbid: number, level: string) {
    const params = new URLSearchParams();
    params.append('level', level);

    return this.http.post('api/documents/' + dbid + '/publish', null, {search: params})
      .map(result => result.text());

  }

  createConcept(docPath: string, concept: any) {
    return this.http.post('api/documents/' + docPath + '/concepts', concept)
      .map((result) => result.json());
  }
}
