package org.endeavourhealth.informationmanager.trud;

public class TrudFeed {
    private String name;
    private String id;
    private String localVersion;
    private String remoteVersion;
    private String download;

    public TrudFeed(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TrudFeed setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public TrudFeed setId(String id) {
        this.id = id;
        return this;
    }

    public String getLocalVersion() {
        return localVersion;
    }

    public TrudFeed setLocalVersion(String localVersion) {
        this.localVersion = localVersion;
        return this;
    }

    public String getRemoteVersion() {
        return remoteVersion;
    }

    public TrudFeed setRemoteVersion(String remoteVersion) {
        this.remoteVersion = remoteVersion;
        return this;
    }

    public String getDownload() {
        return download;
    }

    public TrudFeed setDownload(String download) {
        this.download = download;
        return this;
    }
}
