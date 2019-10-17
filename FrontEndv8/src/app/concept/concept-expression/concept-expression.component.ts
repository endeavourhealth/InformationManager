import {Component, Input, OnInit} from '@angular/core';
import {ConceptExpression} from '../../models/ConceptExpression';

@Component({
  selector: 'concept-expression',
  templateUrl: './concept-expression.component.html',
  styleUrls: ['./concept-expression.component.scss']
})
export class ConceptExpressionComponent implements OnInit {
  @Input() expression: ConceptExpression;

  constructor() { }

  ngOnInit() {
  }

}
