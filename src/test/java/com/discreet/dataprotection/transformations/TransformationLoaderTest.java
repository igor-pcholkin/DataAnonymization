package com.discreet.dataprotection.transformations;

import com.discreet.dataprotection.transformations.Transformation;
import com.discreet.dataprotection.transformations.TransformationsLoader;
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
                new Transformation("test", "USERS",
                        Map.of("name", "name",
                            "email", "email",
                            "address", "address",
                            "birthdate", "birthdate",
                                "socialNumber", "pid",
                        "ccard", "ccard",
                                "post_code", "post")),

                new Transformation("test", "COMPANIES",
                        Map.of("name", "name",
                        "address", "address"), List.of("company_id"))
        ), transformations);
    }

}