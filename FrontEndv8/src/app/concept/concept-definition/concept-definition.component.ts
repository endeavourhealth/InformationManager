import {Component, Input} from '@angular/core';
import {ConceptRelation} from '../../models/ConceptRelation';
import {ConceptService} from '../concept.service';
import {Router} from '@angular/router';

@Component({
  selector: 'concept-definition',
  templateUrl: './concept-definition.component.html',
  styleUrls: ['./concept-definition.component.scss']
})
export class ConceptDefinitionComponent {
  groupedDefinition: ConceptRelation[][];

  @Input()
  iri: string;

  @Input()
  set value(definition: ConceptRelation[]) {
    this.groupedDefinition = this.getGroupedDefinition(definition);
  }

  constructor(
    private router: Router,
    private conceptService : ConceptService) {}

  navigateTo(conceptId: string) {
    this.router.navigate(['concepts', conceptId]);
  }

  getName(conceptId : string) : string {
    return this.conceptService.getName(conceptId);
  }

  getGroupedDefinition(definition: ConceptRelation[]) {
    if (definition == null)
      return null;
    let groups: ConceptRelation[][] = [];
    let g = null;
    let group: ConceptRelation[] = [];
    let sorted = definition.sort((a, b) => a.group - b.group);

    for (let def of sorted) {
      if (def.group != g) {
        g = def.group;
        group = [];
        groups.push(group);
      }
      group.push(def);
    }

    return groups;
  }
}
