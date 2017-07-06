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
  startStatus: string = 'Not Started';
  conceptStatus: string = 'Concepts Not Uploaded';
  conceptUploaded: boolean = false;
  relationshipStatus: string = 'Relationships Not Uploaded';
  relationshipsUploaded: boolean = false;

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
          (result) => {
            vm.conceptStatus = 'Concepts Uploaded';
            vm.conceptUploaded = true;
          },
          (error) => console.log(error)
        )
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
          (result) => {
            vm.relationshipStatus = 'Relationships Uploaded';
            vm.relationshipsUploaded = true;
          },
         (error) => console.log(error)
        )
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

  cancelWizard() {

  }

  startUpload() {
    const vm = this;
    vm.settingsService.startUpload()
      .subscribe (
        (result) => {vm.startStatus = 'Upload Started'; console.log('upload started') },
            (error) => console.log(error)

      )
  }

  completeUpload() {
    const vm = this;
    vm.settingsService.completeUpload()
      .subscribe (
        (result) => {vm.startStatus = 'Upload Started'; console.log('upload started') },
        (error) => console.log(error)

      )
  }

}
