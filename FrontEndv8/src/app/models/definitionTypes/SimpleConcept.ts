import {DBEntity} from '../DBEntity';

export class SimpleConcept extends DBEntity {
  concept: string;
  inferred: boolean;
}
