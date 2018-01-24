package org.endeavourhealth.informationmodel.api.models;

public class Class {
    public static Class CLASS = new Class(1L, "Class");
    public static Class RECORD_TYPE = new Class(2L, "Record type");
    public static Class EVENT_TYPE = new Class(3L, "Event type");
    // public static Class ATTRIBUTE_GROUP = new Class(5L,"Attribute group");
    // public static Class NUMERIC = new Class(8L,"Numeric");
    // public static Class DATE_TIME = new Class(9L,"DateTime");
    // public static Class CODE = new Class(10L,"Code");
    // public static Class TEXT = new Class(11L,"Text");
    // public static Class BOOLEAN = new Class(12L,"Boolean");
    public static Class CODEABLE_CONCEPT = new Class(13L, "Codeable concept");
    public static Class FOLDER = new Class(14L, "Folder");
    public static Class RELATIONSHIP = new Class(15L, "Relationship");
    public static Class FIELD = new Class(16L, "Field");
    public static Class FIELD_LIBRARY = new Class(17L, "Field library");
    public static Class ATTRIBUTE = new Class(19L, "Attribute");
    // public static Class VIEW: Class = new Class(22L,"View");
    // public static Class EXPRESSION: Class = new Class(24L,"Expression");
    // public static Class TERM: Class = new Class(25L,"Term");
    // public static Class LINKED_RECORD: Class = new Class(26L,"Linked record");
    // public static Class LINKED_FIELD: Class = new Class(27L,"Linked field");

    private Long _id;
    private String _display;

    Class(Long id, String display) {
        this._id = id;
        this._display = display;
    }

    public Long getId() {
        return this._id;
    }

    public String getDisplay() {
        return this._display;
    }

    public static Class byId(Long id) {
        // While ConceptId's can break the Int barrier, classes are always in the range 1-99
        switch (id.intValue()) {
            case 1:
                return Class.CLASS;
            case 2:
                return Class.RECORD_TYPE;
            case 3:
                return Class.EVENT_TYPE;
            case 13:
                return Class.CODEABLE_CONCEPT;
            case 14:
                return Class.FOLDER;
            case 15:
                return Class.RELATIONSHIP;
            case 16:
                return Class.FIELD;
            case 17:
                return Class.FIELD_LIBRARY;
            case 19:
                return Class.ATTRIBUTE;
        }
        throw new IllegalArgumentException("Unknown Class ID " + id.toString());
    }
}
