package org.endeavourhealth.informationmanager.migration.models;

public class DBExpression {
    public static final byte CLASS = 0;

    private byte type;
    private int target_concept;

    public DBExpression(byte type, int target_concept) {
        this.type = type;
        this.target_concept = target_concept;
    }

    public byte getType() {
        return type;
    }

    public DBExpression setType(byte type) {
        this.type = type;
        return this;
    }

    public int getTarget_concept() {
        return target_concept;
    }

    public DBExpression setTarget_concept(int target_concept) {
        this.target_concept = target_concept;
        return this;
    }
}
