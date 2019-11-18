package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class DataItemCategory {
    private String name;
    private List<DataItemGroup> itemGroup = new ArrayList<>();

    public String getName() {
        return name;
    }

    public DataItemCategory setName(String name) {
        this.name = name;
        return this;
    }

    public List<DataItemGroup> getItemGroup() {
        return itemGroup;
    }

    public DataItemCategory setItemGroup(List<DataItemGroup> itemGroup) {
        this.itemGroup = itemGroup;
        return this;
    }
}
