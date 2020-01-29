package org.endeavourhealth.informationmanager.common.models.definitionTypes;

public abstract class Definition<T extends Definition>  {
    private boolean inferred;

    public boolean isInferred() {
        return inferred;
    }

    public T setInferred(boolean inferred) {
        this.inferred = inferred;
        return (T)this;
    }
}
