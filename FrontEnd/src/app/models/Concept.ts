import {ConceptProperty} from './ConceptProperty';
import {ConceptDomain} from './ConceptDomain';
import {ConceptSummary} from './ConceptSummary';
import {Version} from './Version';

export class Concept extends ConceptSummary {
  document : number;
  description: string;
  revision : number;
  published : Version;

  range: string;
  properties: ConceptProperty[];
  domain: ConceptDomain[];
}
