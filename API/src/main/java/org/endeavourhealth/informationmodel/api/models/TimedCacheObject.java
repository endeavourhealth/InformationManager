package org.endeavourhealth.informationmodel.api.models;

import java.util.Date;

public class TimedCacheObject<ObjectType> {
    private ObjectType object;
    private Date lastUsed;

    public ObjectType getObject() {
        return object;
    }

    public TimedCacheObject<ObjectType> setObject(ObjectType object) {
        this.object = object;
        return this;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public TimedCacheObject<ObjectType> setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
        return this;
    }
}
