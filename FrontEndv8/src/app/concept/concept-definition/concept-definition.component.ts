import {Component, Input} from '@angular/core';
import {ConceptService} from '../concept.service';
import {Router} from '@angular/router';
import {LoggerService} from 'dds-angular8';
import {ConceptDefinition} from '../../models/ConceptDefinition';
import {Axiom} from '../../models/Axiom';
import {PropertyDefinition} from '../../models/definitionTypes/PropertyDefinition';

@Component({
  selector: 'concept-definition',
  templateUrl: './concept-definition.component.html',
  styleUrls: ['./concept-definition.component.scss']
})
export class ConceptDefinitionComponent {
  iri: string;
  definition: ConceptDefinition;
  axioms: Axiom[];

  constructor(
    private router: Router,
    private log: LoggerService,
    private conceptService : ConceptService) {
    this.conceptService.getAxioms().subscribe(
      (result) => this.axioms = result,
      (error) => this.log.error(error)
    );
  }


  @Input()
  set conceptId(id: number) {
    this.conceptService.getConceptDefinition(id).subscribe(
      (result) => this.definition = result,
      (error) => this.log.error(error)
    );
  };

  getPropertyCardinality(property: PropertyDefinition) {
    if (!property.minCardinality && !property.maxCardinality)
      return '';
    else
      return '(' + (property.minCardinality ? property.minCardinality : 0) + ':' + (property.maxCardinality ? property.maxCardinality : 0) + ')';
  }

  navigateTo(conceptId: string) {
    this.router.navigate(['concepts', conceptId]);
  }

  getName(conceptId : string) : string {
    return this.conceptService.getName(conceptId);
  }
}
