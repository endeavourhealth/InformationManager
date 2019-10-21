import {ExpressionConstraint} from "./ExpressionConstraint";

export class AttributeConstraint {

  property: string[];
  propertyOrSubtypes: string[];
  value: ExpressionConstraint[];

}
