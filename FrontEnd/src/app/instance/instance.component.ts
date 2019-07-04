import {Component, OnInit} from '@angular/core';
import {LoggerService, MessageBoxDialog} from 'eds-angular4';
import {InstanceService} from './instance.service';
import {Instance} from '../models/Instance';
import {IMDocument} from '../models/IMDocument';
import {forkJoin} from 'rxjs/observable/forkJoin';
import {ConceptService} from '../concept/concept.service';
import {Version} from '../models/Version';
import {DocumentService} from '../document/document.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-instance-list',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.css']
})
export class InstanceComponent implements OnInit {
  Version = Version;
  instances: Instance[];
  selected: Instance;
  working: string;

  constructor(
    private $modal: NgbModal,
    private conceptService: ConceptService,
    private documentService: DocumentService,
    private instanceService: InstanceService,
    private log: LoggerService
  ) {
  }

  ngOnInit() {
    this.getInstances();
  }

  getInstances() {
    this.instances = null;
    this.instanceService.getInstances()
      .subscribe(
        (result) => this.instances = result,
        (error) => this.log.error(error)
      );
  }

  getStatus(instance: Instance, force: boolean = false) {
    if (instance.status && !force)
      return instance.status;

    instance.status = 'Checking...';
    this.instanceService.getStatus(instance)
      .subscribe(
        (result) => instance.status = result,
        (error) => {
          this.log.error('Unable to get status for [' + instance.name + ']', error, 'Instance error');
          instance.status = 'Error!';
        }
      );

    return instance.status;
  }

  select(instance: Instance) {
    this.selected = instance;
    if (!this.selected.documents) {
      this.loadDocuments();
    }
  }

  loadDocuments() {
    this.working = 'Fetching document data';
    this.selected.documents = null;
    this.getStatus(this.selected, true);
    forkJoin(
      this.documentService.getDocuments(),
      this.instanceService.getInstanceDocuments(this.selected)
    ).subscribe(
      (result) => this.buildDocumentMap(result[0], result[1]),
      (error) => this.log.error('Error comparing documents', error)
    )
  }

  buildDocumentMap(master: IMDocument[], instance: IMDocument[]) {
    for (let i of instance) {
      let d = master.findIndex(m => m.path == i.path);
      if (d > -1) {
        master[d].instanceVersion = i.version;
        master[d].instanceDrafts = i.drafts;
      } else {
        i.instanceDrafts = 0;
        i.instanceVersion = i.version;
        i.version = null;
        master.push(i);
      }
    }
    this.selected.documents = master;
    this.working = null;
  }

  getClass(item: IMDocument) {
    let i = Version.compare(item.version, item.instanceVersion);

    if (i == 0)
      return 'badge badge-info';

    if (i > 0)
      return 'badge badge-warning';

    if (i < 0)
      return 'badge badge-danger';

    return '';
  }

  updateAvailable(item: IMDocument): boolean {
    if (item.drafts > 0)
      return false;

    if (!item.instanceVersion)
      return true;

    if (Version.compare(item.version, item.instanceVersion) > 0)
      return true;

    return false;
  }

  sendUpdate(item: IMDocument) {
    this.selected.status = "Sending document...";
    this.working = "Sending document...";
    this.instanceService.sendDocumentToInstance(this.selected.dbid, item.dbid)
      .subscribe(
        (result) => this.loadDocuments(),
        (error) => this.getStatus(this.selected)
      );
  }

  getDrafts(item: IMDocument) {
    this.selected.status = "Fetching drafts...";
    this.working = "Fetching drafts...";
    this.instanceService.getDocumentDrafts(this.selected.dbid, item.dbid)
      .subscribe(
        (result) => this.draftsReceived(),
        (error) => this.getStatus(this.selected)
      );
  }

  draftsReceived() {
    this.working = null;
    MessageBoxDialog.open(this.$modal, 'Fetch drafts', 'Workflow task has been created for the new drafts', 'Close', null)
      .result.then(
      () => {}
    );
  }
}
