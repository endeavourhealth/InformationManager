package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.informationmanager.common.transform.model.Clazz;

public class SnomedMeta {
    private Clazz clazz;
    private String moduleId;

    public Clazz getClazz() {
        return clazz;
    }

    public SnomedMeta setClazz(Clazz clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getModuleId() {
        return moduleId;
    }

    public SnomedMeta setModuleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }
}
