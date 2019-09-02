import {ConceptProperty} from './ConceptProperty';
import {ConceptDomain} from './ConceptDomain';
import {ConceptMeta} from './ConceptMeta';

export class Concept {
  id: string;
  meta: ConceptMeta;
  range: string;
  properties: ConceptProperty[];
  domain: ConceptDomain[];
}
