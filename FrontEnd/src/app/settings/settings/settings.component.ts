import { Component, OnInit, ViewChild } from '@angular/core';
import { SettingsService } from '../settings.service';
import {SnomedConfig} from '../models/snomedConfig';

@Component({
  selector: 'app-record-viewer',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  @ViewChild('conceptInput')
  conceptInput: any;

  @ViewChild('relationshipInput')
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
  inactiveSnomedCount: number;
  inactiveRelationshipCount: number;
  processStatus: string;
  saveLimit: number;
  progress: string;
  config: SnomedConfig = new SnomedConfig();


  constructor(private settingsService: SettingsService) { }

  ngOnInit() {
    this.initialiseVariables();
  }

  initialiseVariables() {
    const vm = this;
    vm.cFile = null;
    vm.rFile = null;
    vm.startStatus = 'Initialising Upload. Please wait.';
    vm.startComplete = false;
    vm.conceptStatus = 'Concepts Not Uploaded';
    vm.conceptUploaded = false;
    vm.conceptCount = '0';
    vm.conceptSavedRemaining = 0;
    vm.relationshipStatus = 'Relationships Not Uploaded';
    vm.relationshipsUploaded = false;
    vm.relationshipCount = '0';
    vm.inactiveSnomedCount = 0;
    vm.inactiveRelationshipCount = 0;
    vm.relationshipSavedRemaining = 0;
    vm.processStatus = '';
    vm.saveLimit = 100000;
    vm.progress = '.';
    vm.config.activeOnly = true;
    vm.config.delta = false;
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
    if (vm.startComplete) {
      vm.completeUpload();
    }
    vm.initialiseVariables();

  }

  startUpload() {
    const vm = this;
    vm.settingsService.startUpload(vm.config)
      .subscribe (
        (result) => {
          console.log('success');
          console.log(result);
          vm.startStatus = result;
          if (result === 'OK') {
            vm.startComplete = true;
          }
          },
        (error) => {
            console.log('error' + error);
          }

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
      vm.checkDelta();
    }
  }

  checkDelta() {
    const vm = this;
    if (vm.config.delta) {
      vm.deleteInactiveRelationships();
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
        (error) => {
          vm.processStatus = 'Saving concepts failed. Error : ' + error;
          vm.completeUpload(true);
        }
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
        (error) => {
          vm.processStatus = 'Saving relationships failed. Error : ' + error;
          vm.completeUpload(true);
        }
      )
  }

  setInactiveSnomed() {
    const vm = this;
    vm.processStatus = 'Setting inactive snomed codes.  Please Wait' + vm.progress;
    vm.settingsService.setInactiveSnomed()
      .subscribe (
        (result) => {
          vm.inactiveSnomedCount = result;
          vm.completeUpload();
        },
        (error) => {
          vm.processStatus = 'Setting inactive snomed codes failed. Error : ' + error;
          vm.completeUpload(true);
        }
      )
  }

  deleteInactiveRelationships() {
    const vm = this;
    vm.processStatus = 'Deleting inactive snomed relationships.  Please Wait' + vm.progress;
    vm.settingsService.deleteInactiveRelationships()
      .subscribe (
        (result) => {
          vm.inactiveRelationshipCount = result;
          vm.setInactiveSnomed();
        },
        (error) => {
          vm.processStatus = 'Deleting inactive snomed relationships failed. Error : ' + error;
          vm.completeUpload(true);
        }
      )
  }

  completeUpload(silent: boolean = false) {
    const vm = this;
    if (!silent) {
      vm.processStatus = 'Finalising Upload. Please Wait';
    }
    vm.settingsService.completeUpload()
      .subscribe (
        (result) => {
            if (!silent) {
              vm.processStatus = 'Everything Uploaded Successfully.';
            }
          },
        (error) => vm.processStatus = 'Error Occurred during finalisation. Error : ' + error
      )
  }

}
