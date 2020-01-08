import {Model} from './Model';

export class Concept {
  id: string;
  model: Model;
  name: string;
  description: string;
  status: string;
  codeScheme: string;
  code: string;
  update: Date;
  weighting: number;
}
