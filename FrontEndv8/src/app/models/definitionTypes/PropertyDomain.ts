import {DBEntity} from '../DBEntity';

export class PropertyDomain extends DBEntity {
  domain: string;
  inGroup: number;
  disjointGroup: boolean;
  minCardinality: number;
  maxCardinality: number;
  minInGroup: number;
  maxInGroup: number;
  operator: string;
}
