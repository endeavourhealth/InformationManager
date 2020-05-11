import {DataRange} from './DataRange';

export class DPECardinalityRestriction extends DataRange {
    property: string;
    exact: number;
    min: number;
    max: number;
}
