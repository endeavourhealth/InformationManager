import {DBEntity} from '../DBEntity';

export class PropertyRange extends DBEntity {
  range: string;
  subsumption: string;
  operator: string;
}
