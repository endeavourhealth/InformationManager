import {Component} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Concept} from '../../models/Concept';

@Component({
  selector: 'app-visualise-dialog',
  templateUrl: './visualise-dialog.component.html',
  styleUrls: ['./visualise-dialog.component.css']
})
export class VisualiseDialogComponent {
  concept: Concept;

  public static open(modalService: NgbModal, concept: Concept) {
    const modalRef = modalService.open(VisualiseDialogComponent, { backdrop: 'static', size: 'lg'});
    modalRef.componentInstance.concept = concept;
    return modalRef;
  }

  constructor(public activeModal: NgbActiveModal) { }

  cancel() {
    this.activeModal.dismiss();
  }
}
