import {Component, ElementRef, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-expression-builder',
  templateUrl: './expression-builder.component.html',
  styleUrls: ['./expression-builder.component.css']
})
export class ExpressionBuilderComponent implements OnInit {

  public static open(modalService: NgbModal) {
    const modalRef = modalService.open(ExpressionBuilderComponent,{ backdrop : "static", size: "lg"});
    return modalRef;
  }

  private infoModel : any =
    {
      id: 0,
      name: 'Enterprise Query UI',
      class: 'Concept',
      children: [
        {
          id: 1,
          name: 'Patient',
          class: 'Concept',
          children: [
            {
              id: 3,
              name: 'NHS Number',
              class: 'Number',
              children: []
            },
            {
              id: 4,
              name: 'Date Of Birth',
              class: 'Date',
              children: []
            }
          ]
        }, {
          id: 2
          ,
          name: 'Observation'
          ,
          class: 'Concept'
          ,
          children: [
            {
              id: 5,
              name: 'Effective Date',
              class: 'Date',
              children: []
            },
            {
              id: 6,
              name: 'Code',
              class: 'CodeableConcept',
              children: []
            },
            {
              id: 6,
              name: 'Value',
              class: 'Concept',
              children: [
                {
                  id: 7,
                  name: 'Amount',
                  class: 'Number',
                  children: []
                },
                {
                  id: 8,
                  name: 'Units',
                  class: 'String',
                  children: []
                }
              ]
            }
          ]
        }
      ]
    };
  options : any[] = this.infoModel;


  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit() {
  }

  search() {
    // do autocomplete/type ahead
  }

}
