import {Criterion} from './Criterion';

export class DataSetAttribute {
    iri: string;
    fromPath: string;
    filter: Criterion[];
    assignedValue: string;
}
