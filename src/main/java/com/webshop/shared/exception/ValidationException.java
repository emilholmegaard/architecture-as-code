package com.webshop.shared.exception;

import java.util.List;

/**
 * Exception for validation errors.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public class ValidationException extends BusinessException {
    
    private final List<String> errors;
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
        this.errors = List.of(message);
    }
    
    public ValidationException(List<String> errors) {
        super("Validation failed: " + String.join(", ", errors), "VALIDATION_ERROR");
        this.errors = errors;
    }
    
    public List<String> getErrors() {
        return errors;
    }
}
