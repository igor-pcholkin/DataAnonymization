package com.discreet.dataprotection.transformations;

import java.util.List;
import java.util.Map;

public class TransformationsFileEntry {
    private Map<String, String> anonymizers;
    private List<String> ids;

    public TransformationsFileEntry(Map<String, String> anonymizers, List<String> ids) {
        this.anonymizers = anonymizers;
        this.ids = ids;
    }

    public Map<String, String> getAnonymizers() {
        return anonymizers;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setAnonymizers(Map<String, String> anonymizers) {
        this.anonymizers = anonymizers;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
