import { Injectable } from '@angular/core';
import {URLSearchParams, Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {SnomedConfig} from './models/snomedConfig';

@Injectable()
export class SettingsService {

  constructor(private http: Http) {
  }

  public startUpload(config: SnomedConfig) {
    const vm = this;
    return vm.http.post('/api/informationModel/startUpload', config, {withCredentials : true} )
      .map((response) => response.text());
  }

  public uploadConcepts(csv: any) {
    const vm = this;
    return vm.http.post('/api/informationModel/snomedUpload', csv, {withCredentials : true} )
      .map((response) => response.json());
  }

  public uploadRelationships(csv: any) {
    const vm = this;
    return vm.http.post('/api/informationModel/snomedRelationshipUpload', csv, {withCredentials : true} )
      .map((response) => response.json());
  }

  public saveConcepts(limit: number) {
    const vm = this;
    const params = new URLSearchParams();
    params.set('limit', limit.toString());
    return vm.http.get('/api/informationModel/saveConcepts', {withCredentials : true, search : params} )
      .map((response) => response.json());
  }

  public saveRelationships(limit: number) {
    console.log(limit);
    const vm = this;
    const params = new URLSearchParams();
    params.set('limit', limit.toString());
    return vm.http.get('/api/informationModel/saveRelationships', {withCredentials : true, search : params})
      .map((response) => response.json());
  }

  public setInactiveSnomed() {
    const vm = this;
    return vm.http.get('/api/informationModel/setInactiveSnomed', {withCredentials : true} )
      .map((response) => response.json());
  }

  public deleteInactiveRelationships() {
    const vm = this;
    return vm.http.get('/api/informationModel/deleteInactiveRelationships', {withCredentials : true} )
      .map((response) => response.json());
  }

  public completeUpload() {
    const vm = this;
    return vm.http.get('/api/informationModel/completeUpload', {withCredentials : true} )
      .map((response) => response.toString());
  }

}
