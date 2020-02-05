import {PropertyRange} from './definitionTypes/PropertyRange';
import {PropertyDomain} from './definitionTypes/PropertyDomain';
import {SimpleConcept} from './definitionTypes/SimpleConcept';
import {ClassExpression} from './definitionTypes/ClassExpression';

export class ConceptDefinition {
  subClassOf: ClassExpression;
  equivalentTo: ClassExpression;
  subPropertyOf: SimpleConcept[];
  inversePropertyOf: SimpleConcept[];
  mappedTo: SimpleConcept[];
  replacedBy: SimpleConcept[];
  childOf: SimpleConcept[];
  propertyRange: PropertyRange[];
  propertyDomain: PropertyDomain[];
  propertyChain: string[];
}
