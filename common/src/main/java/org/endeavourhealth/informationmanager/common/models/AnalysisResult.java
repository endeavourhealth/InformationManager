package org.endeavourhealth.informationmanager.common.models;

public class AnalysisResult {
    private AnalysisMethod method;
    private Integer dbid;
    private String id;
    private String name;

    public AnalysisMethod getMethod() {
        return method;
    }

    public AnalysisResult setMethod(AnalysisMethod method) {
        this.method = method;
        return this;
    }

    public Integer getDbid() {
        return dbid;
    }

    public AnalysisResult setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getId() {
        return id;
    }

    public AnalysisResult setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AnalysisResult setName(String name) {
        this.name = name;
        return this;
    }
}
