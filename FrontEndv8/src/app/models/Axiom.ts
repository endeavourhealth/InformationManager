import {Supertype} from './Supertype';
import {Property} from './Property';

export class Axiom {
  token: string;
  transitive: boolean;
  supertypes: Supertype[];
  properties: Property[];
}
