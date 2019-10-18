import {Operator} from "./Operator";
import {AttributeConstraint} from "./AttributeConstraint";
import {RoleGroup} from "../../models/RoleGroup";

export class ExpressionConstraint {

  name: string;
  operator: Operator;
  clazz: string[];
  classOrSubtypes: string[];
  subtypes: string[];
  valueSet: string[];
  attribute: AttributeConstraint[];
  roleGroup: RoleGroup[];

}
