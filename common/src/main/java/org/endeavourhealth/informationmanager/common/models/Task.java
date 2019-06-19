package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Task extends TaskSummary {
    private String userName;
    private String userId;
    private JsonNode data;

    public String getUserName() {
        return userName;
    }

    public Task setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Task setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public JsonNode getData() {
        return data;
    }

    public Task setData(JsonNode data) {
        this.data = data;
        return this;
    }
}
