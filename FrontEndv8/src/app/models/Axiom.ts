import {DBEntity} from './DBEntity';

export class Axiom extends DBEntity {
  token: string;
  definitionProperty: string;
  initial: boolean;
}
