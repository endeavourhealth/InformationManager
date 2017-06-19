import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../settings.service';

@Component({
  selector: 'app-record-viewer',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  private file: File;
  csvSrc: any;
  constructor(private settingsService : SettingsService) { }

  ngOnInit() {
  }

  fileChange(event) {
    const fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      this.file = fileList[0];
    } else {
      this.file = null;
    }
  }

  private uploadFile() {
    const vm = this;
    const myReader: FileReader = new FileReader();

    myReader.onloadend = function(e){
      vm.csvSrc = myReader.result;
      vm.settingsService.uploadCSV(myReader.result)
        .subscribe (
          (result) => console.log(result),
          (error) => console.log(error)
        );
    }

    myReader.readAsText(vm.file);
  }

  private uploadRelationshipFile() {
    const vm = this;
    const myReader: FileReader = new FileReader();

    myReader.onloadend = function(e){
      vm.csvSrc = myReader.result;
      vm.settingsService.uploadRelationshipCSV(myReader.result)
        .subscribe (
          (result) => console.log(result),
          (error) => console.log(error)
        );
    }

    myReader.readAsText(vm.file);
  }

  ok() {
    this.uploadFile();
  }

  cancel() {
    this.file = null;
  }

  okRelationShip() {
    this.uploadRelationshipFile();
  }

}
