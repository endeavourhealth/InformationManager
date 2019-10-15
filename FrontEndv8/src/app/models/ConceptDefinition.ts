import {ConceptExpression} from './ConceptExpression';

export class ConceptDefinition {
  status: string;
  subtypeOf: ConceptExpression[];
  equivalentTo: ConceptExpression[];
  disjointWith: ConceptExpression[];
}
