package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataRange {
    public class DataTypeRestriction {
        private String dataType;
        private String facet;
        private String constrainingFacet;

        @JsonProperty("DataType")
        public String getDataType() {
            return dataType;
        }

        public DataTypeRestriction setDataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        @JsonProperty("Facet")
        public String getFacet() {
            return facet;
        }

        public DataTypeRestriction setFacet(String facet) {
            this.facet = facet;
            return this;
        }

        @JsonProperty("ConstrainingFacet")
        public String getConstrainingFacet() {
            return constrainingFacet;
        }

        public DataTypeRestriction setConstrainingFacet(String constrainingFacet) {
            this.constrainingFacet = constrainingFacet;
            return this;
        }
    }

    private String dataType;
    private DataTypeRestriction dataTypeRestriction;

    @JsonProperty("DataType")
    public String getDataType() {
        return dataType;
    }

    public DataRange setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("DataTypeRestriction")
    public DataTypeRestriction getDataTypeRestriction() {
        return dataTypeRestriction;
    }

    public DataRange setDataTypeRestriction(DataTypeRestriction dataTypeRestriction) {
        this.dataTypeRestriction = dataTypeRestriction;
        return this;
    }
}
