import {Definition} from './Definition';

export class PropertyDomain extends Definition {
  domain: string;
  inGroup: number;
  disjointGroup: boolean;
  minCardinality: number;
  maxCardinality: number;
  minInGroup: number;
  maxInGroup: number;
}
