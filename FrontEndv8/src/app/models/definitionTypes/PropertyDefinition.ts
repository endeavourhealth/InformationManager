import {DBEntity} from '../DBEntity';

export class PropertyDefinition extends DBEntity {
  property: string;
  minCardinality: number;
  maxCardinality: number;
  object: string;
  data: string;
  inferred: boolean;
}
