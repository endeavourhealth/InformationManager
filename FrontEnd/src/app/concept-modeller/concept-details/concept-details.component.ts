import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ConceptSummary} from "../models/concept-summary";
import {ConceptModellerService} from "../concept-modeller.service";
import {ConceptRelationship} from "../models/concept-relationship";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptPickerComponent} from "../concept-picker/concept-picker.component";
import { LinqService } from 'ng2-linq';
import {Observable} from "rxjs/Observable";
import {Clazz} from "../../common/clazz";

@Component({
  selector: 'app-concept-details',
  templateUrl: './concept-details.component.html',
  styleUrls: ['./concept-details.component.css']
})
export class ConceptDetailsComponent implements OnInit, OnDestroy {
  private paramSubscriber: any;

  id: number;
  concept: ConceptSummary;
  ancestors: ConceptRelationship[];
  descendants: ConceptRelationship[];
  relationshipTypes: ConceptSummary[];
  clazzes: ConceptSummary[];

  deletedRelationships: ConceptRelationship[] = [];
  editedRelationships: ConceptRelationship[] = [];

  constructor(private $modal : NgbModal, private linq : LinqService, private router : Router,  private route: ActivatedRoute, private conceptService: ConceptModellerService) { }

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
    this.loadClasses();
    this.loadRelationshipTypes();
  }

  loadConcept(id: number) {
    const vm = this;
    if (id == -1) {
      vm.concept = new ConceptSummary();
      vm.concept.name = '<New concept>';
      // vm.concept.count = 0;
    } else {
      vm.conceptService.findConceptsById(id)
        .subscribe(
          (result) => vm.concept = result,
          (error) => console.log(error)
        );
    }
  }

  loadRelationships(id: number) {
    const vm = this;

    vm.conceptService.getRelationships(id)
      .subscribe(
        (result) => vm.splitRelationships(result),
        (error) => console.log(error)
      )
  }

  loadRelationshipTypes() {
    const vm = this;

    vm.conceptService.getRelationshipTypes()
      .subscribe(
        (result) => vm.relationshipTypes = result,
        (error) => console.log(error)
      )
  }

  loadClasses() {
    const vm = this;

    vm.conceptService.getClasses()
      .subscribe(
        (result) => vm.clazzes = result,
        (error) => console.log(error)
      )
  }

  splitRelationships(relationships : ConceptRelationship[]): void {
    this.ancestors = this.linq.Enumerable()
      .From(relationships)
      .Where(r => r.sourceConcept == this.id)
      .OrderBy(r => r.targetConceptName)
      .ToArray();

    this.descendants = this.linq.Enumerable()
      .From(relationships)
      .Where(r => r.targetConcept == this.id)
      .OrderBy(r => r.sourceConceptName)
      .ToArray();
  }

  showAncestorPicker() {
    let vm = this;
    ConceptPickerComponent.open(vm.$modal)
      .result.then(
      (result) => vm.addAncestorConcept(result),
      (error) => console.error(error)
    );
  }

  addAncestorConcept(concept : ConceptSummary) : void {
    let conceptRelationship: ConceptRelationship = new ConceptRelationship();
    conceptRelationship.targetConcept = concept.id;
    conceptRelationship.targetConceptName = concept.name;
    conceptRelationship.targetConceptShortName = concept.shortName;
    conceptRelationship.targetConceptDescription = concept.description;

    conceptRelationship.sourceConcept = this.concept.id;
    conceptRelationship.sourceConceptName = this.concept.name;
    conceptRelationship.sourceConceptShortName = this.concept.shortName;
    conceptRelationship.sourceConceptDescription = this.concept.description;

    this.editedRelationships.push(conceptRelationship);
    this.ancestors.push(conceptRelationship);
  }

  deleteAncestor(item : ConceptRelationship) {
    var index = this.ancestors.indexOf(item, 0);
    if (index > -1) {
      this.deletedRelationships.push(item);
      this.ancestors.splice(index, 1);
    }
  }

  showDescendantPicker() {
    let vm = this;
    ConceptPickerComponent.open(vm.$modal)
      .result.then(
      (result) => vm.addDescendantConcept(result),
      (error) => console.error(error)
    );
  }

  setRelationshipType(relationship : ConceptRelationship, relationshipType : ConceptSummary): void {
    relationship.relationship_type = relationshipType.id;
    relationship.relationshipTypeName = relationshipType.name;
    relationship.relationshipTypeShortName = relationshipType.shortName;
    relationship.relationshipTypeDescription = relationshipType.description;
  }

  addDescendantConcept(concept : ConceptSummary) : void {
    let conceptRelationship: ConceptRelationship = new ConceptRelationship();
    conceptRelationship.targetConcept = this.concept.id;
    conceptRelationship.targetConceptName = this.concept.name;
    conceptRelationship.targetConceptShortName = this.concept.shortName;
    conceptRelationship.targetConceptDescription = this.concept.description;

    conceptRelationship.sourceConcept = concept.id;
    conceptRelationship.sourceConceptName = concept.name;
    conceptRelationship.sourceConceptShortName = concept.shortName;
    conceptRelationship.sourceConceptDescription = concept.description;
    this.editedRelationships.push(conceptRelationship);
    this.descendants.push(conceptRelationship);
  }

  deleteDescendant(item: ConceptRelationship) {
    var index = this.descendants.indexOf(item, 0);
    if (index > -1) {
      this.deletedRelationships.push(item);
      this.descendants.splice(index, 1);
    }
  }

  cancel() {
    this.router.navigate(['/conceptModeller']);
  }

  save(){
    let vm = this;
    vm.conceptService.saveConcept(this.concept)
      .subscribe(
        (result) => {
          vm.concept = result;
          if (!vm.updateConceptIdsAndSaveRelationships())
            vm.router.navigate(['/conceptModeller']);
        }
      );
  }

  updateConceptIdsAndSaveRelationships() : boolean {
    if (this.deletedRelationships.length == 0 && this.editedRelationships.length == 0)
      return false;

    for (let a of this.ancestors)
      a.sourceConcept = this.concept.id;

    for (let d of this.descendants)
      d.targetConcept = this.concept.id;

    this.saveRelationships();

    return true;
  }

  saveRelationships() {
    let vm = this;
    let observables : Observable<any>[] = [];

    for (let d of this.deletedRelationships) {
      observables.push(this.conceptService.deleteConceptRelationship(d.id));
    }

    for (let e of this.editedRelationships) {
      if (e.id)
        observables.push(this.conceptService.deleteConceptRelationship(e.id));
      observables.push(this.conceptService.addRelationship(e));
    }

    if (observables.length > 0)
      Observable.forkJoin(observables)
        .subscribe(
          (success) => vm.router.navigate(['/conceptModeller']),
          (error) => console.error(error),
        );
  }

  traverseConcept(id: number) {
    this.router.navigate(['/conceptDetails', id]);
  }

  getClassName(clazz : Clazz) : string {
    return Clazz[clazz];
  }
}
