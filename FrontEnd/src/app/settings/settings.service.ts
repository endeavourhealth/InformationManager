import { Injectable } from '@angular/core';
import {URLSearchParams, Http} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class SettingsService {

  constructor(private http: Http) {
  }

  public uploadCSV(csv: any) {
    const vm = this;
    return vm.http.post('/api/informationModel/snomedUpload', csv, {withCredentials : true} )
      .map((response) => response.json());
  }

}
