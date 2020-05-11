import {DocumentInfo} from './DocumentInfo';
import {AnnotationProperty} from './AnnotationProperty';
import {EntityKey} from './EntityKey';
import {DataSet} from './DataSet';
import {Namespace} from './Namespace';
import {Clazz} from './Clazz';
import {ObjectProperty} from './ObjectProperty';
import {DataProperty} from './DataProperty';
import {DataType} from './DataType';
import {ValueSet} from './ValueSet';

export class Ontology {
    namespace: Namespace[];
    documentInfo: DocumentInfo;
    clazz: Clazz[];
    objectProperty: ObjectProperty[];
    dataProperty: DataProperty[];
    dataType: DataType[];
    annotationProperty: AnnotationProperty[];
    key: EntityKey[];
    valueSet: ValueSet[];
    dataSet: DataSet[];
}
