import { Component, OnInit } from '@angular/core';
import {ConceptSummary} from "../models/concept-summary";
import {ConceptModellerService} from "../concept-modeller.service";
import {ConceptRelationship} from "../models/concept-relationship";
import {Router} from "@angular/router";

@Component({
  selector: 'app-count-reports',
  templateUrl: './concept-modeller.component.html',
  styleUrls: ['./concept-modeller.component.css']
})
export class ConceptModellerComponent implements OnInit {
  summaryList: ConceptSummary[];
  conceptRelationship: ConceptRelationship = new ConceptRelationship();
  conceptRelationships: ConceptRelationship[];
  clickedConcept: number;
  searchTerms: string;
  pageSize = 10;
  pageNumber = 1;
  totalConcepts = 30;

  constructor(private router : Router, private conceptService : ConceptModellerService) {
    let vm = this;
    vm.getCommonConcepts();
  }

  getCommonConcepts() {
    let vm = this;
    console.log('getting common');
    vm.conceptService.getCommonConcepts(10)
      .subscribe(
        (result) => vm.summaryList = result,
        (error) => console.log(error)
      );
  }

  ngOnInit() {
  }

  findConcepts() {
    const vm = this;
    vm.conceptService.findConceptsByName(vm.searchTerms, vm.pageNumber, vm.pageSize)
      .subscribe(
        (result) => {
          vm.summaryList = result;
          vm.getConceptSearchCount();
        },
        (error) => console.log(error)
      );
  }

  getConceptSearchCount() {
    const vm = this;
    vm.conceptService.getConceptSearchTotalCount(vm.searchTerms)
      .subscribe(
        (result) => vm.totalConcepts = result,
        (error) => console.log(error)
      );
  }

  populateDB() {
    let vm = this;
    vm.conceptService.populateAllConcepts();
  }

  delete(conceptId: number, e: any) {
    const vm = this;

    vm.conceptService.deleteConcept(conceptId)
     .subscribe(
     (result) => vm.getCommonConcepts(),
     (error) => console.log(error)
     );

    e.stopPropagation();
  }

  addRelationship() {
    var vm = this;

    vm.conceptService.addRelationship(vm.conceptRelationship)
      .subscribe(
        (result) => {vm.getRelationships(vm.clickedConcept); console.log('oh yea');},
        (error) => console.log(error)
      );
  }

  getRelationships(conceptId: number) {
    var vm = this;
    vm.clickedConcept = conceptId;
    vm.conceptService.getRelationships(conceptId)
      .subscribe(
        (result) => {vm.conceptRelationships = result; console.log(result)},
        (error) => console.log(error)
      )
  }

  deleteRelationship(relationshipId: number) {
    let vm = this;

    vm.conceptService.deleteConceptRelationship(relationshipId)
      .subscribe(
        (result) => vm.getRelationships(vm.clickedConcept),
        (error) => console.log(error)
      );
  }

  editConcept(itemId: number) {
    this.router.navigate(['/conceptDetails', itemId]);
  }

  pageChanged($event) {
    const vm = this;
    vm.pageNumber = $event;
    vm.findConcepts();
  }
}
