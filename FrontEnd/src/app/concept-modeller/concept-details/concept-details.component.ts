import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ConceptSummary} from "../models/concept-summary";
import {ConceptModellerService} from "../concept-modeller.service";
import {ConceptRelationship} from "../models/concept-relationship";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptPickerComponent} from "../concept-picker/concept-picker.component";


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

  constructor(private $modal : NgbModal, private route: ActivatedRoute, private conceptService: ConceptModellerService) { }

  ngOnInit() {
    this.paramSubscriber = this.route.params.subscribe(
      params => {
        this.id = params['id'];
        this.reload();
      });
  }

  ngOnDestroy(): void {
    this.paramSubscriber.unsubscribe();
  }

  reload() {
    this.loadConcept(this.id);
    this.loadRelationships(this.id);
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

  navigateConcept(id: number) {
    this.id = id;
    this.reload();
    console.log(this.id);
    console.log('reloading');
  }

  showDialog() {
    let vm = this;
    ConceptPickerComponent.open(vm.$modal)
      .result.then(
      (result) => vm.addRelatedConcept(result),
      (error) => console.error(error)
    );
  }

  addRelatedConcept(concept : ConceptSummary) : void {
    let conceptRelationship : ConceptRelationship = new ConceptRelationship();
    conceptRelationship.targetConcept = this.id;
    conceptRelationship.targetConceptName= this.concept.name;
    conceptRelationship.sourceConcept = concept.id;
    conceptRelationship.sourceConceptName = concept.name;
    this.conceptRelationships.push(conceptRelationship);

    conceptRelationship = new ConceptRelationship();
    conceptRelationship.sourceConcept = this.id;
    conceptRelationship.sourceConceptName = this.concept.name;
    conceptRelationship.targetConcept = concept.id;
    conceptRelationship.targetConceptName = concept.name;
    this.conceptRelationships.push(conceptRelationship);
  }
}
