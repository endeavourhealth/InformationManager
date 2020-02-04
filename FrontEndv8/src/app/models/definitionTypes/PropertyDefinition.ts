import {Definition} from './Definition';

export class PropertyDefinition extends Definition {
  group: number;
  property: string;
  minCardinality: number;
  maxCardinality: number;
  object: string;
  data: string;
  inferred: boolean;
}
