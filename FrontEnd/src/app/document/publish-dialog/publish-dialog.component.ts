import {Component} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {IMDocument} from '../../models/IMDocument';
import {Version} from '../../models/Version';
import {DocumentService} from '../document.service';

@Component({
  selector: 'app-publish-dialog',
  templateUrl: './publish-dialog.component.html',
  styleUrls: ['./publish-dialog.component.css']
})
export class PublishDialogComponent {
  public static open(modalService: NgbModal, document: IMDocument) {
    const modalRef = modalService.open(PublishDialogComponent, { backdrop: 'static', size: 'sm'});
    modalRef.componentInstance.document = document;
    return modalRef;
  }

  Version = Version;
  document: IMDocument;
  level: string = 'Build';
  done: boolean = true;

  constructor(public activeModal: NgbActiveModal,
              private documentService: DocumentService
  ) { }

  ok() {
    this.done = false;
    this.documentService.publish(this.document.dbid, this.level)
      .subscribe(
        (result) => this.activeModal.close(result),
        (error) => this.activeModal.dismiss(error)
      );
  }

  cancel() {
    this.activeModal.close(null);
  }
}
