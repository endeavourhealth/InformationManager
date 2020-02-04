package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import java.util.ArrayList;
import java.util.List;

public class PropertyChain extends Definition {
    private List<String> chain = new ArrayList<>();

    public List<String> getChain() {
        return chain;
    }

    public PropertyChain setChain(List<String> chain) {
        this.chain = chain;
        return this;
    }

    public PropertyChain addProperty(String property) {
        this.chain.add(property);
        return this;
    }
}
