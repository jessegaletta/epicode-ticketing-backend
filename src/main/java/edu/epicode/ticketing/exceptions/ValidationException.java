package edu.epicode.ticketing.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException{

    private List<String> errorsList;
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<String> errorsList) {
        this.errorsList = errorsList;
    }
    public List<String> getErrorsList() {
        return errorsList;
    }

}
