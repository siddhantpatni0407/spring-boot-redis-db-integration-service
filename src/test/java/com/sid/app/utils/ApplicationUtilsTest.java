package com.sid.app.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ApplicationUtilsTest {

    static class Dummy {
        private String name;
        private int age;

        public Dummy(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    @DisplayName("Should return valid JSON string for a valid object")
    void testGetJSONString_ValidObject() {
        Dummy dummy = new Dummy("John", 30);

        String jsonResult = ApplicationUtils.getJSONString(dummy);
        String expected = "{\"name\":\"John\",\"age\":30}";

        System.out.println("✅ Test: Valid Object → Expected: " + expected + ", Actual: " + jsonResult);

        assertEquals(expected, jsonResult, "The JSON string should match the expected structure.");
    }

    @Test
    @DisplayName("Should return empty string when object is null")
    void testGetJSONString_NullObject() {
        String result = ApplicationUtils.getJSONString(null);

        System.out.println("✅ Test: Null Object → Expected: \"\", Actual: " + result);

        assertEquals("", result, "For null object, JSON string should be empty.");
    }

    @Test
    @DisplayName("Should return empty string when serialization fails (circular reference)")
    void testGetJSONString_InvalidObject() {
        Object invalidObject = new Object() {
            public Object self = this; // Circular reference
        };

        String result = ApplicationUtils.getJSONString(invalidObject);

        System.out.println("✅ Test: Invalid Object → Expected: \"\", Actual: " + result);

        assertEquals("", result, "On serialization failure, should return empty string.");
    }
}
