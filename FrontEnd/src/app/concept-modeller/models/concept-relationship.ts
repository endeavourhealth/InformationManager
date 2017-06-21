export class ConceptRelationship {
  id: number;
  relationship_type: number;
  relationshipTypeName: string;
  relationshipTypeDescription: string;
  relationshipTypeShortName: string;
  sourceConcept: number;
  sourceConceptName: string;
  sourceConceptDescription: string;
  sourceConceptShortName: string;
  targetConcept: number;
  targetConceptName: string;
  targetConceptDescription: string;
  targetConceptShortName: string;
  targetLabel: string;
  count: number;
}
