import {Component, Input} from '@angular/core';
import {ConceptService} from '../concept.service';
import {Router} from '@angular/router';
import {Definition} from '../../models/Definition';
import {LoggerService} from 'dds-angular8';

@Component({
  selector: 'concept-definition',
  templateUrl: './concept-definition.component.html',
  styleUrls: ['./concept-definition.component.scss']
})
export class ConceptDefinitionComponent {
  iri: string;
  axioms: Definition[];

  constructor(
    private router: Router,
    private log: LoggerService,
    private conceptService : ConceptService) {}


  @Input()
  set conceptIri(iri: string) {
    this.conceptService.getDefinition(iri).subscribe(
      (result) => this.axioms = result,
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
