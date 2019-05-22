package org.endeavourhealth.informationmanager.common.models;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private int page = 1;
    private int count = 0;
    private List<ConceptSummary> results = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public SearchResult setPage(int page) {
        this.page = page;
        return this;
    }

    public int getCount() {
        return count;
    }

    public SearchResult setCount(int count) {
        this.count = count;
        return this;
    }

    public List<ConceptSummary> getResults() {
        return results;
    }

    public SearchResult setResults(List<ConceptSummary> results) {
        this.results = results;
        return this;
    }
}
