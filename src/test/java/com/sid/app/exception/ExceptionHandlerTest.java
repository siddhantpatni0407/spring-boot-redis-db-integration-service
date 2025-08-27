package com.sid.app.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class ExceptionHandlerTest {

  private final ExceptionHandler exceptionHandler = new ExceptionHandler();

  @Test
  @DisplayName("Should return error message map when UserNotFoundException is thrown")
  void testExceptionHandler_ReturnsErrorMessage() {
    // Arrange
    String originalMessage = "User not found with ID 123";
    String expectedErrorMessage = "Could not found the user with id " + originalMessage;
    UserNotFoundException exception = new UserNotFoundException(originalMessage);

    // Act
    Map<String, String> response = exceptionHandler.exceptionHandler(exception);

    // Assert
    assertNotNull(response, "Response map should not be null");
    assertTrue(response.containsKey("errorMessage"), "Response should contain 'errorMessage' key");
    assertEquals(
        expectedErrorMessage,
        response.get("errorMessage"),
        "Error message should match the exception message");
  }

  @Test
  @DisplayName("Should have correct ResponseStatus annotation with NOT_FOUND")
  void testExceptionHandler_ResponseStatusAnnotation() throws NoSuchMethodException {
    // Act
    ResponseStatus responseStatus =
        exceptionHandler
            .getClass()
            .getMethod("exceptionHandler", UserNotFoundException.class)
            .getAnnotation(ResponseStatus.class);

    // Assert
    assertNotNull(responseStatus, "@ResponseStatus annotation should be present");
    assertEquals(
        HttpStatus.NOT_FOUND, responseStatus.value(), "HTTP status should be NOT_FOUND (404)");
  }
}
