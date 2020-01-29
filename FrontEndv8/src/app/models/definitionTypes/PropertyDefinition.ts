import {Definition} from './Definition';

export class PropertyDefinition extends Definition {
  property: string;
  minCardinality: number;
  maxCardinality: number;
  object: string;
  data: string;
}
