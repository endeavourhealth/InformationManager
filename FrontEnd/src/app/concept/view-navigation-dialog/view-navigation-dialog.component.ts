import { Component, OnInit } from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';

@Component({
  selector: 'app-view-navigation-dialog',
  templateUrl: './view-navigation-dialog.component.html',
  styleUrls: ['./view-navigation-dialog.component.css']
})
export class ViewNavigationDialogComponent implements OnInit {
  public static open(modalService: NgbModal, viewId: number) {
    const modalRef = modalService.open(ViewNavigationDialogComponent, { backdrop: 'static', size: 'lg'});
    modalRef.componentInstance.viewId = viewId;
    return modalRef;
  }

  viewId: number;

  constructor(public activeModal: NgbActiveModal, protected conceptService: ConceptService) { }

  ngOnInit() {
  }

  ok() {
    this.activeModal.close();
  }

  cancel() {
    this.activeModal.close();
  }
}
