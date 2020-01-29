import {Component, Input} from '@angular/core';
import {ConceptService} from '../concept.service';
import {Router} from '@angular/router';
import {LoggerService} from 'dds-angular8';
import {ConceptAxiom} from '../../models/ConceptAxiom';

@Component({
  selector: 'concept-definition',
  templateUrl: './concept-definition.component.html',
  styleUrls: ['./concept-definition.component.scss']
})
export class ConceptDefinitionComponent {
  iri: string;
  conceptAxioms: ConceptAxiom[];

  constructor(
    private router: Router,
    private log: LoggerService,
    private conceptService : ConceptService) {}


  @Input()
  set conceptIri(iri: string) {
    this.conceptService.getConceptAxioms(iri).subscribe(
      (result) => this.conceptAxioms = result,
      (error) => this.log.error(error)
    );
  };


  navigateTo(conceptId: string) {
    this.router.navigate(['concepts', conceptId]);
  }

  getName(conceptId : string) : string {
    return this.conceptService.getName(conceptId);
  }
}
