export class Category {

  public static CLASS: Category = new Category("Class", 1, 99, 1);
  public static RELATIONS_AND_ATTRIBUTES: Category = new Category("Relationships and Attributes", 100, 499, 15);
  public static FOLDERS: Category = new Category("Folders", 500, 999, 18);
  public static EVENT_AND_RECORD_TYPES: Category = new Category("Event and Record Types", 1000, 4999, 3);
  public static FIELD_LIBRARY: Category = new Category("Field Library", 5000, 7999, 17);
  public static INTERNAL_CODEABLE_CONCEPTS: Category = new Category("Internal Codeable Concepts", 8000, 9999, 13);
  public static FIELDS: Category = new Category("Fields", 10000, 49999, 16);
  public static EXTERNAL_CODEABLE_CONCEPTS: Category = new Category("External Codeable Concepts", 50000, 9999999999, 13);

  private static _categories: Category[] = [
    Category.CLASS,
    Category.RELATIONS_AND_ATTRIBUTES,
    Category.FOLDERS,
    Category.EVENT_AND_RECORD_TYPES,
    Category.FIELD_LIBRARY,
    Category.INTERNAL_CODEABLE_CONCEPTS,
    Category.FIELDS,
    Category.EXTERNAL_CODEABLE_CONCEPTS
  ];

  public static getById(categoryId: number): Category {
    return this._categories[categoryId];
  }

  public static getForConcept(conceptId: number): Category {
    if (conceptId <= Category.CLASS.getMax()) return Category.CLASS;
    if (conceptId <= Category.RELATIONS_AND_ATTRIBUTES.getMax()) return Category.RELATIONS_AND_ATTRIBUTES;
    if (conceptId <= Category.FOLDERS.getMax()) return Category.FOLDERS;
    if (conceptId <= Category.EVENT_AND_RECORD_TYPES.getMax()) return Category.EVENT_AND_RECORD_TYPES;
    if (conceptId <= Category.FIELD_LIBRARY.getMax()) return Category.FIELD_LIBRARY;
    if (conceptId <= Category.INTERNAL_CODEABLE_CONCEPTS.getMax()) return Category.INTERNAL_CODEABLE_CONCEPTS;
    if (conceptId <= Category.FIELDS.getMax()) return Category.FIELDS;
    return Category.EXTERNAL_CODEABLE_CONCEPTS;
  }

  private _display: string;
  private _min: number;
  private _max: number;
  private _class: number;

  constructor(display: string, min: number, max: number, clazz: number) {
    this._display = display;
    this._min = min;
    this._max = max;
    this._class = clazz;
  }

  public getId(): number { return Category._categories.indexOf(this); }
  public getDisplay(): string { return this._display; }
  public getMin(): number { return this._min; }
  public getMax(): number { return this._max; }
  public getClass(): number { return this._class; }
}
