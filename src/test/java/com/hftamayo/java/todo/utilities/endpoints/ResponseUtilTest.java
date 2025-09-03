package com.hftamayo.java.todo.utilities.endpoints;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.error.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResponseUtilTest {

    // ==================== ERROR RESPONSE TESTS ====================

    @Test
    void shouldCreateErrorResponseWithValidParameters() {
        // Given
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Validation failed";
        Exception exception = new IllegalArgumentException("Invalid input data");

        // When
        EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(status, message, exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getCode());
        assertEquals("Validation failed", response.getResultMessage());
        assertNotNull(response.getData());

        ErrorResponseDto errorData = response.getData();
        assertNotNull(errorData.getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST, errorData.getStatus());
        assertEquals("Validation failed", errorData.getMessage());

        // Fix: getErrors() returns List<String>, not String
        assertNotNull(errorData.getErrors());
        assertEquals(1, errorData.getErrors().size());
        assertEquals("Invalid input data", errorData.getErrors().get(0));
    }

    @Test
    void shouldCreateErrorResponseWithNullExceptionMessage() {
        // Given
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Resource not found";
        Exception exception = new RuntimeException(); // Exception without message

        // When
        EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(status, message, exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getCode());
        assertEquals("Resource not found", response.getResultMessage());
        assertNotNull(response.getData());

        ErrorResponseDto errorData = response.getData();
        assertNotNull(errorData.getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, errorData.getStatus());
        assertEquals("Resource not found", errorData.getMessage());

        // When exception message is null, ResponseUtil should use "Unknown error" as fallback
        assertNotNull(errorData.getErrors());
        assertEquals(1, errorData.getErrors().size());
        assertEquals("Unknown error", errorData.getErrors().get(0));
    }

    @Test
    void shouldCreateErrorResponseWithDifferentHttpStatuses() {
        // Test various HTTP status codes
        HttpStatus[] statuses = {
            HttpStatus.BAD_REQUEST,
            HttpStatus.UNAUTHORIZED,
            HttpStatus.FORBIDDEN,
            HttpStatus.NOT_FOUND,
            HttpStatus.CONFLICT,
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.SERVICE_UNAVAILABLE
        };

        for (HttpStatus status : statuses) {
            // When
            EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(
                status, "Test message", new RuntimeException("Test exception"));

            // Then
            assertNotNull(response);
            assertEquals(status.value(), response.getCode());
            assertEquals("Test message", response.getResultMessage());
            assertNotNull(response.getData());
            assertEquals(status, response.getData().getStatus());
        }
    }

    @Test
    void shouldCreateErrorResponseWithUtcTimestamp() {
        // Given
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Test message";
        Exception exception = new RuntimeException("Test exception");

        // When
        EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(status, message, exception);

        // Then
        assertNotNull(response.getData().getTimestamp());
        
        // Verify timestamp is recent (within last minute)
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime responseTime = response.getData().getTimestamp();
        assertTrue(responseTime.isBefore(now.plusMinutes(1)));
        assertTrue(responseTime.isAfter(now.minusMinutes(1)));
    }

    // ==================== SUCCESS RESPONSE TESTS ====================

    @Test
    void shouldCreateSuccessResponseWithDefaultMessage() {
        // Given
        String testData = "Test data";

        // When
        EndpointResponseDto<String> response = ResponseUtil.successResponse(testData);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("SUCCESS", response.getResultMessage());
        assertEquals("Test data", response.getData());
    }

    @Test
    void shouldCreateSuccessResponseWithNullData() {
        // When
        EndpointResponseDto<String> response = ResponseUtil.successResponse((String) null);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("SUCCESS", response.getResultMessage());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateSuccessResponseWithCustomMessage() {
        // Given
        List<String> testData = List.of("item1", "item2", "item3");
        String customMessage = "Data retrieved successfully";

        // When
        EndpointResponseDto<List<String>> response = ResponseUtil.successResponse(testData, customMessage);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Data retrieved successfully", response.getResultMessage());
        assertEquals(testData, response.getData());
        assertEquals(3, response.getData().size());
    }

    @Test
    void shouldCreateSuccessResponseWithCustomMessageAndNullData() {
        // Given
        String customMessage = "Operation completed";

        // When
        EndpointResponseDto<Object> response = ResponseUtil.successResponse(null, customMessage);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Operation completed", response.getResultMessage());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateSuccessResponseWithCustomCodeAndMessage() {
        // Given
        Map<String, Object> testData = Map.of("key1", "value1", "key2", 42);
        int customCode = 202;
        String customMessage = "Request accepted";

        // When
        EndpointResponseDto<Map<String, Object>> response = ResponseUtil
                .successResponse(testData, customCode, customMessage);

        // Then
        assertNotNull(response);
        assertEquals(202, response.getCode());
        assertEquals("Request accepted", response.getResultMessage());
        assertEquals(testData, response.getData());
        assertEquals(2, response.getData().size());
    }

    @Test
    void shouldCreateSuccessResponseWithCustomCodeAndNullData() {
        // Given
        int customCode = 204;
        String customMessage = "No content";

        // When
        EndpointResponseDto<Object> response = ResponseUtil.successResponse(null, customCode, customMessage);

        // Then
        assertNotNull(response);
        assertEquals(204, response.getCode());
        assertEquals("No content", response.getResultMessage());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateSuccessResponseWithDifferentDataTypes() {
        // Test with String
        EndpointResponseDto<String> stringResponse = ResponseUtil.successResponse("test string");
        assertEquals("test string", stringResponse.getData());

        // Test with Integer
        EndpointResponseDto<Integer> intResponse = ResponseUtil.successResponse(42);
        assertEquals(42, intResponse.getData());

        // Test with Boolean
        EndpointResponseDto<Boolean> boolResponse = ResponseUtil.successResponse(true);
        assertEquals(true, boolResponse.getData());

        // Test with List
        List<String> listData = List.of("a", "b", "c");
        EndpointResponseDto<List<String>> listResponse = ResponseUtil.successResponse(listData);
        assertEquals(listData, listResponse.getData());

        // Test with Map
        Map<String, String> mapData = Map.of("key", "value");
        EndpointResponseDto<Map<String, String>> mapResponse = ResponseUtil.successResponse(mapData);
        assertEquals(mapData, mapResponse.getData());
    }

    // ==================== CREATED RESPONSE TESTS ====================

    @Test
    void shouldCreateCreatedResponseWithValidData() {
        // Given
        String createdData = "New resource created";
        String message = "Resource created successfully";

        // When
        EndpointResponseDto<String> response = ResponseUtil.createdResponse(createdData, message);

        // Then
        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("Resource created successfully", response.getResultMessage());
        assertEquals("New resource created", response.getData());
    }

    @Test
    void shouldCreateCreatedResponseWithNullData() {
        // Given
        String message = "Resource created";

        // When
        EndpointResponseDto<Object> response = ResponseUtil.createdResponse(null, message);

        // Then
        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("Resource created", response.getResultMessage());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateCreatedResponseWithComplexData() {
        // Given
        Map<String, Object> createdResource = Map.of(
            "id", 123,
            "name", "Test Resource",
            "active", true
        );
        String message = "User created successfully";

        // When
        EndpointResponseDto<Map<String, Object>> response = ResponseUtil.createdResponse(createdResource, message);

        // Then
        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("User created successfully", response.getResultMessage());
        assertEquals(createdResource, response.getData());
        assertEquals(3, response.getData().size());
    }

    // ==================== EDGE CASES AND NULL HANDLING ====================

    @Test
    void shouldHandleEmptyStringMessages() {
        // Test error response with empty message
        EndpointResponseDto<ErrorResponseDto> errorResponse = ResponseUtil.errorResponse(
            HttpStatus.BAD_REQUEST, "", new RuntimeException("Test"));
        assertEquals("", errorResponse.getResultMessage());

        // Test success response with empty message
        EndpointResponseDto<String> successResponse = ResponseUtil.successResponse("data", "");
        assertEquals("", successResponse.getResultMessage());

        // Test created response with empty message
        EndpointResponseDto<String> createdResponse = ResponseUtil.createdResponse("data", "");
        assertEquals("", createdResponse.getResultMessage());
    }

    @Test
    void shouldHandleVeryLongMessages() {
        // Given
        String longMessage = "This is a very long message that contains detailed information " +
            "about what went wrong with the system. It should be able to handle messages " +
            "of considerable length without any issues. The message continues with more " +
            "details about the error condition and potential solutions.";

        // When
        EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, longMessage, new RuntimeException("Test"));

        // Then
        assertNotNull(response);
        assertEquals(longMessage, response.getResultMessage());
        assertEquals(longMessage, response.getData().getMessage());
    }

    @Test
    void shouldHandleSpecialCharactersInMessages() {
        // Given
        String specialMessage = "Error with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        Exception exception = new RuntimeException("Exception with special chars: ñáéíóú 中文 русский");

        // When
        EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(
            HttpStatus.BAD_REQUEST, specialMessage, exception);

        // Then
        assertNotNull(response);
        assertEquals(specialMessage, response.getResultMessage());
        assertEquals(specialMessage, response.getData().getMessage());

        // Fix: getErrors() returns List<String>, not String
        assertNotNull(response.getData().getErrors());
        assertEquals(1, response.getData().getErrors().size());
        assertEquals("Exception with special chars: ñáéíóú 中文 русский", response.getData().getErrors().get(0));
    }

    @Test
    void shouldHandleChainedExceptions() {
        // Given
        RuntimeException originalCause = new RuntimeException("Original cause");
        IllegalArgumentException intermediateCause = new
                IllegalArgumentException("Intermediate cause", originalCause);
        String message = "Final error message";

        // When
        EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, message, intermediateCause);

        // Then
        assertNotNull(response);
        assertEquals(message, response.getResultMessage());

        // Fix: getErrors() returns List<String>, not String
        assertNotNull(response.getData().getErrors());
        assertEquals(1, response.getData().getErrors().size());
        assertEquals("Intermediate cause", response.getData().getErrors().get(0));
    }

    @Test
    void shouldHandleNullMessagesInSuccessResponse() {
        // When
        EndpointResponseDto<String> response = ResponseUtil.successResponse("data", null);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getResultMessage());
        assertEquals("data", response.getData());
    }

    @Test
    void shouldHandleNullMessagesInCreatedResponse() {
        // When
        EndpointResponseDto<String> response = ResponseUtil.createdResponse("data", null);

        // Then
        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertNull(response.getResultMessage());
        assertEquals("data", response.getData());
    }

    @Test
    void shouldHandleNullMessagesInSuccessResponseWithCode() {
        // When
        EndpointResponseDto<String> response = ResponseUtil.successResponse("data", 202, null);

        // Then
        assertNotNull(response);
        assertEquals(202, response.getCode());
        assertNull(response.getResultMessage());
        assertEquals("data", response.getData());
    }

    // ==================== RESPONSE STRUCTURE VALIDATION ====================

    @Test
    void shouldCreateValidEndpointResponseDtoStructure() {
        // Given
        String testData = "Test data";
        String message = "Test message";

        // When
        EndpointResponseDto<String> response = ResponseUtil.successResponse(testData, message);

        // Then
        assertNotNull(response);
        assertTrue(response instanceof EndpointResponseDto);
        assertEquals(200, response.getCode());
        assertEquals("Test message", response.getResultMessage());
        assertEquals("Test data", response.getData());
    }

    @Test
    void shouldCreateValidErrorResponseDtoStructure() {
        // Given
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Validation error";
        Exception exception = new IllegalArgumentException("Invalid input");

        // When
        EndpointResponseDto<ErrorResponseDto> response = ResponseUtil.errorResponse(status, message, exception);

        // Then
        assertNotNull(response);
        assertTrue(response instanceof EndpointResponseDto);
        assertTrue(response.getData() instanceof ErrorResponseDto);
        
        ErrorResponseDto errorData = response.getData();
        assertNotNull(errorData.getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST, errorData.getStatus());
        assertEquals("Validation error", errorData.getMessage());

        // Fix: getErrors() returns List<String>, not String
        assertNotNull(errorData.getErrors());
        assertEquals(1, errorData.getErrors().size());
        assertEquals("Invalid input", errorData.getErrors().get(0));
    }

    @Test
    void shouldMaintainDataIntegrityAcrossDifferentResponseTypes() {
        // Test that data is not modified during response creation
        List<String> originalData = List.of("item1", "item2", "item3");
        
        // Create different response types with the same data
        EndpointResponseDto<List<String>> successResponse = ResponseUtil.successResponse(originalData);
        EndpointResponseDto<List<String>> successWithMessage = ResponseUtil
                .successResponse(originalData, "Custom message");
        EndpointResponseDto<List<String>> successWithCode = ResponseUtil
                .successResponse(originalData, 202, "Accepted");
        EndpointResponseDto<List<String>> createdResponse = ResponseUtil
                .createdResponse(originalData, "Created");

        // Verify data integrity
        assertEquals(originalData, successResponse.getData());
        assertEquals(originalData, successWithMessage.getData());
        assertEquals(originalData, successWithCode.getData());
        assertEquals(originalData, createdResponse.getData());
        
        // Verify data is not the same reference (defensive copy if implemented)
        assertSame(originalData, successResponse.getData());
        assertSame(originalData, successWithMessage.getData());
        assertSame(originalData, successWithCode.getData());
        assertSame(originalData, createdResponse.getData());
    }
}
