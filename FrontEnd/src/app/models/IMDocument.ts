import {Version} from './Version';

export class IMDocument {
  dbid : number;
  path: string;
  version: Version;

  draft: boolean;
  instanceVersion: Version;
}
