import {Criterion} from './Criterion';
import {DataSetAttribute} from './DataSetAttribute';

export class DataSetEntity {
  iri: string;
  filter: Criterion[];
  attribute: DataSetAttribute[];
}
