import {DBEntity} from './DBEntity';

export class Concept extends DBEntity {
  iri: string;
  status: string;
  name: string;
  description: string;
  code: string;
}
