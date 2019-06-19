export enum AnalysisMethod {
  CONCEPT_ID = 0,
  SCHEME_CODE = 1,
  NAME = 2,
  DESCRIPTION = 3,
  CONCEPT_EQUIVALENT = 4,
  CONCEPT_RELATED = 5,
  CONCEPT_REPLACED = 6
}

export class AnalysisMethodHelper {
  public static getName(status: AnalysisMethod): string {
    switch (status) {
      case AnalysisMethod.CONCEPT_ID: return 'Concept ID';
      case AnalysisMethod.CONCEPT_EQUIVALENT: return ' - Equivalent to';
      case AnalysisMethod.CONCEPT_RELATED: return ' - Related to';
      case AnalysisMethod.CONCEPT_REPLACED: return ' - Replaced by';
      case AnalysisMethod.SCHEME_CODE: return 'Scheme/Code';
      case AnalysisMethod.NAME: return 'Name';
      case AnalysisMethod.DESCRIPTION: return 'Description';
      default: return 'Unknown ['+status+']';
    }
  }
}
