package org.endeavourhealth.informationmodel.api.framework;

import org.endeavourhealth.common.cache.CacheManager;
import org.endeavourhealth.common.cache.ICache;
import org.endeavourhealth.informationmodel.api.models.TimedCacheObject;

import java.util.Date;
import java.util.Stack;

public abstract class TimedGenericCache<ObjectType> implements ICache {
    private final Stack<TimedCacheObject<ObjectType>> _cache = new Stack<>();
    private Integer timeout = 30;

    public TimedGenericCache() {
        this(30);
    }

    public TimedGenericCache(Integer timeout) {
        this.timeout = timeout;
        CacheManager.registerCache(this);
    }

    public ObjectType pop() {
        TimedCacheObject<ObjectType> tco = null;
        Long oldest = new Date().getTime() - (timeout * 1000);

        while (tco == null && !_cache.empty()) {
            tco = _cache.pop();

            // Bin it if too old
            if (tco.getLastUsed().getTime() < oldest) {
                cleanup(tco.getObject());
                tco = null;
            }
        }

        if (_cache.size()> 0)
            return _cache.pop().getObject();
        else
            return this.create();

    }

    public void push(ObjectType object) {
        if (isValid(object))
            _cache.push(
                new TimedCacheObject<ObjectType>()
                .setObject(object)
                .setLastUsed(new Date())
            );
    }

    protected boolean isValid(ObjectType object) { return true; }

    protected void cleanup(ObjectType objectType) {}

    protected abstract ObjectType create();

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public long getSize() {
        return _cache.size();
    }

    @Override
    public void clearCache() {
        _cache.empty();
    }

    public TimedGenericCache<ObjectType> setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }
}
