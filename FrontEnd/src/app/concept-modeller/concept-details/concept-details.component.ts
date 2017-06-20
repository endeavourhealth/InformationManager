import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ConceptSummary} from "../models/concept-summary";
import {ConceptModellerService} from "../concept-modeller.service";
import {ConceptRelationship} from "../models/concept-relationship";

@Component({
  selector: 'app-concept-details',
  templateUrl: './concept-details.component.html',
  styleUrls: ['./concept-details.component.css']
})
export class ConceptDetailsComponent implements OnInit, OnDestroy {
  private paramSubscriber: any;

  id: number;
  concept: ConceptSummary;
  conceptRelationships: ConceptRelationship[];

  constructor(private route: ActivatedRoute, private conceptService: ConceptModellerService) { }

  ngOnInit() {
    this.paramSubscriber = this.route.params.subscribe(
      params => {
        this.id = params['id'];
        this.loadConcept(this.id);
        this.loadRelationships(this.id);
      });
  }

  ngOnDestroy(): void {
    this.paramSubscriber.unsubscribe();
  }

  loadConcept(id: number) {
    const vm = this;
    vm.conceptService.findConceptsById(id)
      .subscribe(
        (result) => vm.concept = result,
        (error) => console.log(error)
      );
  }

  loadRelationships(id: number) {
    const vm = this;

    vm.conceptService.getRelationships(id)
      .subscribe(
        (result) => {vm.conceptRelationships = result; console.log(result)},
        (error) => console.log(error)
      )
  }

}
