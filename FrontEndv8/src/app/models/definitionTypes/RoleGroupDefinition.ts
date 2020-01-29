import {Definition} from './Definition';
import {PropertyDefinition} from './PropertyDefinition';

export class RoleGroupDefinition extends Definition {
  group: number;
  properties: PropertyDefinition[];
}
