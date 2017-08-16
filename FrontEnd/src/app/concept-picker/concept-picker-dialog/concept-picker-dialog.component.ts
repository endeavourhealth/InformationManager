import { Component, OnInit } from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {BaseEntity} from "../../common/BaseEntity";
import {ConceptPickerService} from "../concept-picker.service";
import {ConceptRelationship} from "../../concept-modeller/models/concept-relationship";
import { LinqService } from 'ng2-linq';

@Component({
  selector: 'app-concept-picker-dialog',
  templateUrl: './concept-picker-dialog.component.html',
  styleUrls: ['./concept-picker-dialog.component.css']
})
export class ConceptPickerDialogComponent implements OnInit {

  private rootConcept : number;
  private multiSelect : boolean;
  private relationshipFilter : number[];
  private classFilter : number[];
  private selectionClassFilter : number [];

  public static open(
    modalService: NgbModal,
    rootConcept : number,
    multiSelect? : boolean,
    relationshipFilter? : number[],
    classFilter? : number[],
    selectionClassFilter? : number []
  ) {
    const modalRef = modalService.open(ConceptPickerDialogComponent,{ backdrop : "static", size: "lg"});
    modalRef.componentInstance.rootConcept = rootConcept;
    modalRef.componentInstance.multiSelect = multiSelect;
    modalRef.componentInstance.relationshipFilter = relationshipFilter;
    modalRef.componentInstance.classFilter = classFilter;
    modalRef.componentInstance.selectionClassFilter = selectionClassFilter;

    return modalRef;
  }

  protected parents : BaseEntity[];
  protected siblings : BaseEntity[];
  protected children : BaseEntity[];
  protected selected : BaseEntity;

  constructor(public activeModal: NgbActiveModal, private linq : LinqService, protected service : ConceptPickerService) { }

  ngOnInit() {
    this.parents = [];
    this.selected = {id: this.rootConcept, name: 'ROOT'};
    this.siblings = [this.selected];
    this.getRelated();
  }

  private getRelated() {
    var vm = this;
    this.service.getRelationships(1001)
      .subscribe(
        (result) => vm.displayRelated(result),
        (error) => console.error(error)
      );
  }

  private displayRelated(related : ConceptRelationship[]) {
    if (this.selected.id == this.rootConcept)
      this.parents = [];
    else
      this.parents = this.linq.Enumerable()
        .From(related)
        .Where(r => r.targetConcept == this.selected.id && this.allowedRelationship(r.relationship_type) && this.allowedClass(r.sourceConceptClass))
        .OrderBy(r => r.sourceConceptName)
        .Select(r => { return {id: r.sourceConcept, name: r.sourceConceptName} })
        .ToArray();

    this.children = this.linq.Enumerable()
      .From(related)
      .Where(r => r.sourceConcept == this.selected.id && this.allowedRelationship(r.relationship_type) && this.allowedClass(r.targetConceptClass))
      .OrderBy(r => r.targetConceptName)
      .Select(r => { return {id: r.targetConcept, name: r.targetConceptName} })
      .ToArray();

  }

  private allowedRelationship(relationshipType : number) : boolean {
    if (this.relationshipFilter == null || this.relationshipFilter.length == 0)
      return true;

    return this.relationshipFilter.includes(relationshipType);
  }

  private allowedClass(clazz : number) : boolean {
    if (this.classFilter == null || this.classFilter.length == 0)
      return true;

    return this.classFilter.includes(clazz);
  }

}
