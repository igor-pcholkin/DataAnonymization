package com.discreet.datamasking.transformations;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransformationLoaderTest {
    TransformationsLoader loader = new TransformationsLoader();

    @Test
    public void testReadTransformations() {
        List<Transformation> transformations = loader.loadDefinitions(
                "src/test/resources/transformations.yaml");

        assertEquals(List.of(
                new Transformation("test", "users",
                        Map.of("name", "name",
                            "email", "email",
                            "address", "address",
                            "birthdate", "birthdate",
                                "socialNumber", "pid",
                        "ccard", "ccard")),

                new Transformation("test", "companies",
                        Map.of("name", "name",
                        "address", "address"))
        ), transformations);
    }

}