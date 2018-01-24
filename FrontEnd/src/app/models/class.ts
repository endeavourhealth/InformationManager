export class Class {

  public static CLASS: Class = new Class(1,'Class');
  public static RECORD_TYPE: Class = new Class(2,'Record type');
  public static EVENT_TYPE: Class = new Class(3,'Event type');
  // public static ATTRIBUTE_GROUP: Class = new Class(5,'Attribute group');
  // public static NUMERIC: Class = new Class(8,'Numeric');
  // public static DATE_TIME: Class = new Class(9,'DateTime');
  // public static CODE: Class = new Class(10,'Code');
  // public static TEXT: Class = new Class(11,'Text');
  // public static BOOLEAN: Class = new Class(12,'Boolean');
  public static CODEABLE_CONCEPT: Class = new Class(13,'Codeable concept');
  public static FOLDER: Class = new Class(14,'Folder');
  public static RELATIONSHIP: Class = new Class(15,'Relationship');
  public static FIELD: Class = new Class(16,'Field');
  public static FIELD_LIBRARY: Class = new Class(17,'Field library');
  public static ATTRIBUTE: Class = new Class(19,'Attribute');
  // public static VIEW: Class = new Class(22,'View');
  // public static EXPRESSION: Class = new Class(24,'Expression');
  // public static TERM: Class = new Class(25,'Term');
  // public static LINKED_RECORD: Class = new Class(26,'Linked record');
  // public static LINKED_FIELD: Class = new Class(27,'Linked field');

  private _id: number;
  private _display: string;

  constructor(id: number, display: string) {
    this._id = id;
    this._display = display;
  }

  public getId(): number { return this._id; }
  public getDisplay(): string { return this._display; }

  public static byId(id: number): Class {
    switch (id) {
      case 1: return Class.CLASS;
      case 2: return Class.RECORD_TYPE;
      case 3: return Class.EVENT_TYPE;
      case 13: return Class.CODEABLE_CONCEPT;
      case 14: return Class.FOLDER;
      case 15: return Class.RELATIONSHIP;
      case 16: return Class.FIELD;
      case 17: return Class.FIELD_LIBRARY;
      case 19: return Class.ATTRIBUTE;
    }
  }
}

