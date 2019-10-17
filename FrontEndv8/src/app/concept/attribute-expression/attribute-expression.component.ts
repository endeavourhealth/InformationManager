import {Component, Input, OnInit} from '@angular/core';
import {AttributeExpression} from '../../models/AttributeExpression';

@Component({
  selector: 'attribute-expression',
  templateUrl: './attribute-expression.component.html',
  styleUrls: ['./attribute-expression.component.scss']
})
export class AttributeExpressionComponent implements OnInit {
  @Input() attributeExpression: AttributeExpression;

  constructor() { }

  ngOnInit() {
  }

}
