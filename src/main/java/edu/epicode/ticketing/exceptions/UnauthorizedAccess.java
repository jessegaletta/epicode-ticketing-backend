package edu.epicode.ticketing.exceptions;

public class UnauthorizedAccess extends RuntimeException {
    public UnauthorizedAccess(String message) {
        super(message);
    }
}
