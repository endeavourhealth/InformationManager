import {Component, OnInit} from '@angular/core';
import {LoggerService} from 'eds-angular4';
import {InstanceService} from './instance.service';
import {Instance} from '../models/Instance';
import {IMDocument} from '../models/IMDocument';
import {forkJoin} from 'rxjs/observable/forkJoin';
import {ConceptService} from '../concept/concept.service';
import {Version} from '../models/Version';

@Component({
  selector: 'app-instance-list',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.css']
})
export class InstanceComponent implements OnInit {
  Version = Version;
  instances: Instance[];
  selected: Instance;

  constructor(
    private conceptService: ConceptService,
    private instanceService: InstanceService,
    private log: LoggerService
  ) { }

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

  getStatus(instance: Instance, force:boolean = false) {
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
      forkJoin(
        this.conceptService.getDocuments(),
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
      } else {
        i.instanceVersion = i.version;
        i.version = null;
        master.push(i);
      }
    }
    this.selected.documents = master;
  }

  getClass(item: IMDocument) {
    let i = Version.compare(item.version, item.instanceVersion);

    if (i == 0)
      return 'badge badge-success';

    if (i > 0)
      return 'badge badge-warning';

    return 'badge badge-danger';
  }

  updateAvailable(item: IMDocument): boolean {
    if (!item.instanceVersion)
      return true;

    if (Version.compare(item.version, item.instanceVersion) > 0)
      return true;

    return false;
  }


  sendUpdate(item: IMDocument) {
    this.instanceService.sendDocumentToInstance(this.selected.dbid, item.dbid)
      .subscribe(
        (result) => this.loadDocuments(),
        (error) => this.log.error(error)
      );
  }

  // sendToInstance(document: any, instance: Instance) {
  //   this.instanceService.sendUpdate(document, instance)
  //     .subscribe(
  //       (result) => this.loadDocuments(),
  //       (error) => this.log.error(error)
  //     );
  // }
}
