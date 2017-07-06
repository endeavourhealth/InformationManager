import { Injectable } from '@angular/core';
import {URLSearchParams, Http} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class SettingsService {

  constructor(private http: Http) {
  }

  public startUpload() {
    const vm = this;
    return vm.http.get('/api/informationModel/startUpload', {withCredentials : true} )
      .map((response) => response.toString());
  }

  public uploadCSV(csv: any) {
    const vm = this;
    return vm.http.post('/api/informationModel/snomedUpload', csv, {withCredentials : true} )
      .map((response) => response.toString());
  }

  public uploadRelationshipCSV(csv: any) {
    const vm = this;
    return vm.http.post('/api/informationModel/snomedRelationshipUpload', csv, {withCredentials : true} )
      .map((response) => response.toString());
  }

  public completeUpload() {
    const vm = this;
    return vm.http.get('/api/informationModel/completeUpload', {withCredentials : true} )
      .map((response) => response.toString());
  }

}
