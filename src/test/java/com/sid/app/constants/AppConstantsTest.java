package com.sid.app.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppConstantsTest {

    @Test
    void testEmployeeEndpoint() {
        assertEquals("/api/v1/redis-db-integration-service/employee",
                AppConstants.EMPLOYEE_ENDPOINT,
                "EMPLOYEE_ENDPOINT constant value should match expected");
    }

}
