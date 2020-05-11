import {Component, Input} from '@angular/core';
import {ConceptService} from '../concept.service';
import {Router} from '@angular/router';
import {LoggerService} from 'dds-angular8';
import {Axiom} from '../../models/Axiom';
import {PropertyDefinition} from '../../models/definitionTypes/PropertyDefinition';
import {Concept} from '../../models/Concept';
import {Clazz} from '../../models/Clazz';

@Component({
  selector: 'concept-definition',
  templateUrl: './concept-definition.component.html',
  styleUrls: ['./concept-definition.component.scss']
})
export class ConceptDefinitionComponent {
  iri: string;
  definition: Clazz;
  axioms: Axiom[];

  constructor(
    private router: Router,
    private log: LoggerService,
    private conceptService : ConceptService) {
/*    this.conceptService.getAxioms().subscribe(
      (result) => this.axioms = result,
      (error) => this.log.error(error)
    );*/
  }


  @Input()
  set conceptIri(iri: string) {
    this.conceptService.getConceptDefinition(iri).subscribe(
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

  getJson() : string {
    return JSON.stringify(this.definition);
  }

  getDefinitionProperties() {
    if (this.definition)
      return Object.keys(this.definition).filter(k => (this.definition[k] instanceof Object));
    else
      return [];
  }

  getKeys(item) {
    if (item)
      return Object.keys(item);
    else
      return [];
  }

  isPrimitive(item: any) {
    return !(item instanceof Object);
  }

  isObject(item: any) {
    return (item instanceof Object) && !Array.isArray(item);
  }

  isArray(item: any) {
    return Array.isArray(item);
  }
}
