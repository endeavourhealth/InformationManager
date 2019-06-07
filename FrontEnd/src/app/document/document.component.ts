import {Component, OnInit} from '@angular/core';
import {LoggerService} from 'eds-angular4';
import {DocumentService} from './document.service';
import {IMDocument} from '../models/IMDocument';
import {StatusHelper} from '../models/Status';
import {Version} from '../models/Version';
import {PublishDialogComponent} from './publish-dialog/publish-dialog.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {DraftConcept} from '../models/DraftConcept';

@Component({
  selector: 'app-document-list',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.css']
})
export class DocumentComponent implements OnInit {
  Version=Version;
  getStatusName = StatusHelper.getName;

  documents: IMDocument[];
  selected: IMDocument;
  pending: DraftConcept[];

  constructor(
    private $modal: NgbModal,
    private documentService: DocumentService,
    private log: LoggerService
  ) { }

  ngOnInit() {
    this.getDocuments();
  }

  getDocuments() {
    this.selected = null;
    this.documents = null;
    this.documentService.getDocuments()
      .subscribe(
        (result) => this.documents = result,
        (error) => this.log.error(error)
      );
  }

  getStatus(document: IMDocument) {
    return document.draft ? 'Draft' : 'Published';
  }

  select(document: IMDocument) {
    this.pending = null;
    this.selected = document;
    this.documentService.getDocumentPending(document.dbid)
      .subscribe(
        (result) => this.pending = result,
        (error) => this.log.error('Error loading pending concepts for document', error)
      );
  }

  publish() {
    PublishDialogComponent.open(this.$modal, this.selected)
      .result.then(
      (result) => this.getDocuments(),
      (error) => this.log.error('Error publishing document', error)
    );
  }
}
