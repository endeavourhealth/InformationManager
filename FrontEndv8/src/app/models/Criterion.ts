import {Restriction} from './Restriction';
import {Filter} from './Filter';

export class Criterion {
    iri: string;
    description: string;
    fromEntity: string;
    operator: string;
    filter: Filter;
    restriction: Restriction;
}
