import {Component, Input, OnInit} from '@angular/core';
import {ConceptDefinition} from '../../models/ConceptDefinition';

@Component({
  selector: 'concept-definition',
  templateUrl: './concept-definition.component.html',
  styleUrls: ['./concept-definition.component.scss']
})
export class ConceptDefinitionComponent implements OnInit {
  @Input() definition: ConceptDefinition;

  constructor() { }

  ngOnInit() {
  }

}
