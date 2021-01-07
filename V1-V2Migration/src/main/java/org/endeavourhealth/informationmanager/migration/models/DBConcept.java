package org.endeavourhealth.informationmanager.migration.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DBConcept {
    public static final byte CLASS = 0;

    private int dbid;
    private int namespace;
    private int module;
    private String iri;
    private String name;
    private String description;
    private byte type;
    private String code;
    private Integer scheme;
    private byte status;
    private int weighting;
    private Timestamp updated;
    private List<DBAxiom> axioms = new ArrayList<>();

    public int getDbid() {
        return dbid;
    }

    public DBConcept setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public int getNamespace() {
        return namespace;
    }

    public DBConcept setNamespace(int namespace) {
        this.namespace = namespace;
        return this;
    }

    public int getModule() {
        return module;
    }

    public DBConcept setModule(int module) {
        this.module = module;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public DBConcept setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public DBConcept setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DBConcept setDescription(String description) {
        this.description = description;
        return this;
    }

    public byte getType() {
        return type;
    }

    public DBConcept setType(byte type) {
        this.type = type;
        return this;
    }

    public String getCode() {
        return code;
    }

    public DBConcept setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getScheme() {
        return scheme;
    }

    public DBConcept setScheme(Integer scheme) {
        this.scheme = scheme;
        return this;
    }

    public byte getStatus() {
        return status;
    }

    public DBConcept setStatus(byte status) {
        this.status = status;
        return this;
    }

    public int getWeighting() {
        return weighting;
    }

    public DBConcept setWeighting(int weighting) {
        this.weighting = weighting;
        return this;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public DBConcept setUpdated(Timestamp updated) {
        this.updated = updated;
        return this;
    }

    public List<DBAxiom> getAxioms() {
        return axioms;
    }

    public DBConcept setAxioms(List<DBAxiom> axioms) {
        this.axioms = axioms;
        return this;
    }
}
