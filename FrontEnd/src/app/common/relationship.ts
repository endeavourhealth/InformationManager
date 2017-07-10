export enum Relationship {
  IsA = 100,
  CopiesFields = 101,
  HasChild = 102,
  HasParent = 103,
  HasReciprocal = 104,
  HasSubtype = 105,
  HasField = 106,
  HasValueType = 107,
  HasPreferredValueSet = 108,
  HasLinkedRecordType = 109,
  HasLinkedField = 110,
  DerivedFromField = 111
}

function getRelationshipName(relatiohship : Relationship) : string {
  return Relationship[relatiohship];
}
