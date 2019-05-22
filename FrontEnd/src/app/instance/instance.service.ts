import { Injectable } from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Instance} from '../models/Instance';
import {IMDocument} from '../models/IMDocument';

@Injectable()
export class InstanceService {

  constructor(private http: Http) { }

  getInstances(): Observable<Instance[]> {
    return this.http.get('api/Instance')
      .map((result) => result.json());
  }

  getStatus(instance: Instance): Observable<string> {
    return this.http.get(instance.url + '/public/Management/status')
      .map((result) => result.text());
  }

  getInstanceDocuments(instance: Instance): Observable<IMDocument[]> {
    return this.http.get(instance.url + '/public/Management/documents')
      .map((result) => result.json());
  }

  sendDocumentToInstance(instanceDbid: number, documentDbid: number) {
    return this.http.post('api/Instance/'+instanceDbid+'/Document/'+documentDbid, null)
      .map((result) => result.text());
  }

  // getDocument(item: IMDocument): Observable<any> {
  //   return this.http.get('api/IM/Document/' + item.dbid + '/LatestPublished');
  // }
  //
  // sendUpdate(document: any, instance:Instance) {
  //   return this.http.post(instance.url + '/public/Management/document', document)
  //     .map((result) => result.text());
  //
  // }

}
