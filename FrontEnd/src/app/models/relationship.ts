export enum Relationship {
  FIELD_INHERITOR = 101,
  FIELD = 106,
  FIELD_VALUE_TYPE = 107,
  LINKED_RECORD = 109
}

/*
export class Relationship {
  public static FIELD_INHERITOR: Relationship = new Relationship(101, 'Field inheritor');
  public static FIELD_VALUE_TYPE: Relationship = new Relationship(107, 'Field value type');

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
