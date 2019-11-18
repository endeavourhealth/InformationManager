package org.endeavourhealth.informationmanager.common.models.document;

public class Version {
    private Integer major;
    private Integer minor;
    private Integer build;

    public static Version fromString(String version) {
        return new Version();
    }

    public Integer getMajor() {
        return major;
    }

    public Version setMajor(Integer major) {
        this.major = major;
        return this;
    }

    public Integer getMinor() {
        return minor;
    }

    public Version setMinor(Integer minor) {
        this.minor = minor;
        return this;
    }

    public Integer getBuild() {
        return build;
    }

    public Version setBuild(Integer build) {
        this.build = build;
        return this;
    }

    @Override
    public String toString() {      // TODO
        return "TODO";
    }
}
