import { Component, OnInit } from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-cardinality-dialog',
  templateUrl: './cardinality-dialog.component.html',
  styleUrls: ['./cardinality-dialog.component.css']
})
export class CardinalityDialogComponent implements OnInit {
  public static open(modalService: NgbModal, cardinality: number) {
    const modalRef = modalService.open(CardinalityDialogComponent, { backdrop: 'static', size: 'sm'});
    modalRef.componentInstance.cardinality = cardinality;
    return modalRef;
  }

  cardinality: number;

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }

  ok() {
    this.activeModal.close(this.cardinality);
  }

  cancel() {
    this.activeModal.close();
  }

  getCardinalityValue() {
    if (this.cardinality > 1)
      return 2;
    else
      return this.cardinality;
  }

  setCardinality(cardinality: string) {
    this.cardinality = parseInt(cardinality);
  }
}
