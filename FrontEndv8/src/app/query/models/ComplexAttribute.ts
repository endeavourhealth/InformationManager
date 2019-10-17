import {Operator} from "./Operator";
import {Function} from "./Function";
import {ExpressionConstraint} from "./ExpressionConstraint";
import {ValueComparison} from "./ValueComparison";

export class ComplexAttribute {

  description: string;
  operator: Operator;
  property: string;
  function: Function;
  definedFunction: string;
  valueExpression: ExpressionConstraint;
  value: ValueComparison;
  valueRange: Range;
  valueSet: string;
  valueCode: string;
  valueBoolean: boolean;
  hasValue: boolean;
  includeNull: boolean;
  assignedProperty: string;
  assignedBooleanProperty: string;

}
