export enum Class {
  EVENT_TYPE = 3,
  CODEABLE_CONCEPT = 13,
  FIELD = 16,
  ABSTRACT_FIELD = 17,
  RECORD_TYPE = 19
}

/*
export class Class {
  public static EVENT_TYPE: Class = new Class(3, 'Event type');
  public static FIELD: Class = new Class(16,'Field');
  public static ABSTRACT_FIELD: Class = new Class(17,'Abstract Field');
  public static RECORD_TYPE: Class = new Class(19,'Record type');

  private _id: number;
  private _display: string;

  constructor(id: number, display: string) {
    this._id = id;
    this._display = display;
  }

  public getId(): number { return this._id; }
  public getDisplay(): string { return this._display; }
}
*/
