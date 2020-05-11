import {Annotation} from './Annotation';
import {OPERestriction} from './OPERestriction';
import {OPECardinalityRestriction} from './OPECardinalityRestriction';
import {IndividualValueRestriction} from './IndividualValueRestriction';
import {DPERestriction} from './DPERestriction';
import {DPECardinalityRestriction} from './DPECardinalityRestriction';
import {DataValueRestriction} from './DataValueRestriction';

export class ClassExpression {
    annotation: Annotation[];
    inferred: boolean;
    Class: string;
    Intersection: ClassExpression[];
    Union: ClassExpression[];
    compliment: ClassExpression;
    ObjectSome: OPERestriction;
    objectOnly: OPERestriction;
    objectCardinality: OPECardinalityRestriction;
    objectHasValue: IndividualValueRestriction;
    dataSome: DPERestriction;
    dataOnly: DPERestriction;
    dataCardinality: DPECardinalityRestriction;
    dataHasValue: DataValueRestriction;
}
