package com.discreet.datamasking.transformations;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransformationLoaderTest {
    TransformationsLoader loader = new TransformationsLoader();

    @Test
    public void testReadTransformations() {
        List<Transformation> transformations = loader.loadDefinitions();

        assertEquals(List.of(
                new Transformation("test", "users", "name", "name"),
                new Transformation("test", "users","email", "email"),
                new Transformation("test", "users","address", "address"),
                new Transformation("test", "users","birthdate", "birthdate"),
                new Transformation("test", "users","socialNumber", "pid"),
                new Transformation("test", "users","ccard", "ccard"),

                new Transformation("test", "companies","name", "name"),
                new Transformation("test", "companies","address", "address")
        ), transformations);
    }

}