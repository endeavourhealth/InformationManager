package org.endeavourhealth.informationmanager.common.models;

public class Axiom extends DBEntity<Axiom> {
    private String token;
    private boolean initial;

    public String getToken() {
        return token;
    }

    public Axiom setToken(String token) {
        this.token = token;
        return this;
    }

    public String getDefinitionProperty() {
        char c[] = this.token.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public boolean isInitial() {
        return initial;
    }

    public Axiom setInitial(boolean initial) {
        this.initial = initial;
        return this;
    }
}
