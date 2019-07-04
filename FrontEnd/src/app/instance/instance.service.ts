import { Injectable } from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Instance} from '../models/Instance';
import {IMDocument} from '../models/IMDocument';

@Injectable()
export class InstanceService {

  constructor(private http: Http) { }

  getInstances(): Observable<Instance[]> {
    return this.http.get('api/instances')
      .map((result) => result.json());
  }

  getStatus(instance: Instance): Observable<string> {
    return this.http.get(instance.url + '/management/status')
      .map((result) => result.text());
  }

  getInstanceDocuments(instance: Instance): Observable<IMDocument[]> {
    return this.http.get(instance.url + '/management/documents')
      .map((result) => result.json());
  }

  sendDocumentToInstance(instanceDbid: number, documentDbid: number) {
    return this.http.post('api/instances/'+instanceDbid+'/documents/'+documentDbid, null)
      .map((result) => result.text());
  }

  getDocumentDrafts(instanceDbid: number, documentDbid: number) {
    return this.http.get('api/instances/'+instanceDbid+'/documents/'+documentDbid + '/drafts')
      .map((result) => result.text());
  }
}
