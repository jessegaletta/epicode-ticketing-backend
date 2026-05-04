package edu.epicode.ticketing.exceptions;

import edu.epicode.ticketing.payloads.errors.ErrorsDTO;
import edu.epicode.ticketing.payloads.errors.ErrorsWithListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorsWithListDTO handleValidationError(ValidationException ex){
        return new ErrorsWithListDTO("Payload errors", LocalDateTime.now(), ex.getErrorsList());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //401
    public ErrorsDTO handleNotFound(UnauthorizedException ex){
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedAccess.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) //403
    public ErrorsDTO handleForbidden(UnauthorizedAccess ex){
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorsDTO handleNotFound(NotFoundException ex){
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorsDTO handleGenericErrors(Exception ex){
        ex.printStackTrace();
        return new ErrorsDTO("An error occurred, we will fix it very soon.", LocalDateTime.now());
    }
}
