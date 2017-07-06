import { Component, OnInit, ViewChild } from '@angular/core';
import { SettingsService } from '../settings.service';

@Component({
  selector: 'app-record-viewer',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  @ViewChild('conceptInput')
  conceptInput: any;

  @ViewChild('relationshipinput')
  relationshipInput: any;

  cFile: File;
  rFile: File;
  conceptFile: string;
  relationshipFile: string;
  startStatus: string;
  startComplete = false;
  conceptStatus: string;
  conceptUploaded = false;
  conceptCount: string;
  conceptSavedCount: string;
  relationshipStatus: string;
  relationshipsUploaded = false;
  relationshipCount: string;
  relationshipSavedCount: string;
  processStatus: string;

  constructor(private settingsService: SettingsService) { }

  ngOnInit() {
    this.initialiseVariables();
  }

  initialiseVariables() {
    const vm = this;
    vm.cFile = null;
    vm.rFile = null;
    vm.startStatus = 'Not Started';
    vm.startComplete = false;
    vm.conceptStatus = 'Concepts Not Uploaded';
    vm.conceptUploaded = false;
    vm.conceptCount = '0';
    vm.conceptSavedCount = '0';
    vm.relationshipStatus = 'Relationships Not Uploaded';
    vm.relationshipsUploaded = false;
    vm.relationshipCount = '0';
    vm.relationshipSavedCount = '0';
    vm.processStatus = '';
  }

  conceptFileChange(event) {
    const vm = this;
    const fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      vm.cFile = fileList[0];
    } else {
      vm.cFile = null;
    }
  }

  relationshipFileChange(event) {
    const vm = this;
    const fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      vm.rFile = fileList[0];
    } else {
      vm.rFile = null;
    }
  }

  private uploadFile() {
    const vm = this;
    const myReader: FileReader = new FileReader();

    myReader.onloadend = function(e){
      vm.settingsService.uploadConcepts(myReader.result)
        .subscribe (
          (result) => {
            vm.conceptStatus = 'Concepts Uploaded';
            vm.conceptUploaded = true;
            vm.conceptCount = result;
          },
          (error) => console.log(error)
        )
    }

    vm.conceptFile = vm.cFile.name;
    myReader.readAsText(vm.cFile);
  }

  private uploadRelationshipFile() {
    const vm = this;
    const myReader: FileReader = new FileReader();

    myReader.onloadend = function(e){
      vm.settingsService.uploadRelationships(myReader.result)
        .subscribe (
          (result) => {
            vm.relationshipStatus = 'Relationships Uploaded';
            vm.relationshipsUploaded = true;
            vm.relationshipCount = result;
          },
         (error) => console.log(error)
        )
    }

    vm.relationshipFile = vm.rFile.name;
    myReader.readAsText(vm.rFile);
  }

  ok() {
    this.uploadFile();
  }

  cancelConcept() {
    this.cFile = null;
  }

  cancelRelationship() {
    this.rFile = null;
  }

  okRelationShip() {
    this.uploadRelationshipFile();
  }

  cancelWizard() {
    const vm = this;
    vm.conceptInput.nativeElement.value = '';
    vm.relationshipInput.nativeElement.value = '';
    vm.completeUpload();
    vm.initialiseVariables();

  }

  startUpload() {
    const vm = this;
    vm.settingsService.startUpload()
      .subscribe (
        (result) => {
          vm.startStatus = 'Upload Started';
          vm.startComplete = true;
          },
            (error) => console.log(error)

      )
  }

  processUpload() {
    const vm = this;
    vm.saveConcepts();
  }

  saveConcepts() {
    const vm = this;
    vm.processStatus = 'Saving Concepts.  Please Wait.';
    vm.settingsService.saveConcepts()
      .subscribe (
        (result) => {
          vm.conceptSavedCount = result;
          vm.conceptStatus = 'Successfully saved ' + vm.conceptSavedCount + ' snomed concepts.'
          vm.saveRelationships();
        },
        (error) => vm.processStatus = 'Saving concepts failed. Error : ' + error
      )
  }

  saveRelationships() {
    const vm = this;
    vm.processStatus = 'Saving Relationships.  Please Wait.';
    vm.settingsService.saveRelationships()
      .subscribe (
        (result) => {
          vm.relationshipSavedCount = result;
          vm.relationshipStatus = 'Successfully saved ' + vm.relationshipSavedCount + ' snomed relationships.'
          vm.completeUpload();
        },
        (error) => vm.processStatus = 'Saving relationships failed. Error : ' + error
      )
  }

  completeUpload() {
    const vm = this;
    vm.processStatus = 'Finalising Upload. Please Wait';
    vm.settingsService.completeUpload()
      .subscribe (
        (result) => {
          vm.processStatus = 'Everything Uploaded Successfully.';
          },
        (error) => vm.processStatus = 'Error Occurred during finalisation. Error : ' + error
      )
  }

}
