import {Supertype} from './Supertype';
import {Property} from './Property';

export class Definition {
  axiom: string;
  transitive: boolean;
  supertypes: Supertype[];
  properties: Property[];
}
