package com.discreet.datamasking.transformations;

import java.util.ArrayList;
import java.util.List;

public class Transformations {
    private List<Schema> schemas;

    public Transformations() {
        schemas = new ArrayList<>();
    }

    public List<Schema> getSchemas() {
        return schemas;
    }

    public void addSchema(Schema schema) {
        schemas.add(schema);
    }
}
