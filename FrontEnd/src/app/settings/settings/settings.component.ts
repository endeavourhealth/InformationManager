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
  conceptSavedRemaining: number;
  relationshipStatus: string;
  relationshipsUploaded = false;
  relationshipCount: string;
  relationshipSavedRemaining: number;
  processStatus: string;
  saveLimit: number;
  progress: string;


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
    vm.conceptSavedRemaining = 0;
    vm.relationshipStatus = 'Relationships Not Uploaded';
    vm.relationshipsUploaded = false;
    vm.relationshipCount = '0';
    vm.relationshipSavedRemaining = 0;
    vm.processStatus = '';
    vm.saveLimit = 100000;
    vm.progress = '.';
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
            vm.conceptSavedRemaining = result;
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
            vm.relationshipSavedRemaining = result;
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
    vm.saveAllConcepts();
  }

  saveAllConcepts() {
    const vm = this;
    if (vm.conceptSavedRemaining > 0) {
      vm.progress = vm.progress + '.';
      vm.saveConcepts();
    } else {
      vm.saveAllRelationships();
    }
  }

  saveAllRelationships() {
    const vm = this;
    if (vm.relationshipSavedRemaining > 0) {
      vm.saveRelationships();
    } else {
      vm.completeUpload();
    }
  }

  saveConcepts() {
    const vm = this;
    vm.processStatus = 'Saving Concepts.  Please Wait' + vm.progress;
    vm.settingsService.saveConcepts(vm.saveLimit)
      .subscribe (
        (result) => {
          vm.conceptSavedRemaining = result;
          vm.conceptStatus = vm.conceptSavedRemaining + ' snomed concepts to save.'
          vm.saveAllConcepts();
        },
        (error) => vm.processStatus = 'Saving concepts failed. Error : ' + error
      )
  }

  saveRelationships() {
    const vm = this;
    vm.processStatus = 'Saving Relationships.  Please Wait' + vm.progress;
    vm.settingsService.saveRelationships(vm.saveLimit)
      .subscribe (
        (result) => {
          vm.relationshipSavedRemaining = result;
          vm.relationshipStatus = vm.relationshipSavedRemaining + ' snomed relationships to save.'
          vm.saveAllRelationships();
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
