import {Status} from './Status';
import {Version} from './Version';

export class ConceptMeta {
  document : number;
  name: string;
  description: string;
  scheme: string;
  code: string;
  status : Status;
  updated : Date;
  revision : number;
  published : Version;
}
