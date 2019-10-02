import {PropertyDomain} from './PropertyDomain';
import {ConceptDefinition} from './ConceptDefinition';


export class Concept {
  model: string;
  concept: any;
  definition: ConceptDefinition[];
  propertyDomain: PropertyDomain;
  propertyRange: any;
}
