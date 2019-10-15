import {AttributeExpression} from './AttributeExpression';
import {RoleGroup} from './RoleGroup';

export class ConceptExpression {
  name: string;
  operator: string;
  concept: string;
  attribute: AttributeExpression;
  roleGroup: RoleGroup;
  expression: ConceptExpression[];
}
