package com.security.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

//    bad request exception
    @ExceptionHandler(value=ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){

        ApiException apiException = new ApiException(e.getMessage(),e,HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }
//    not found exception
    @ExceptionHandler(value=NotFoundException.class)
    public ResponseEntity<Object> handleApiRequestException(NotFoundException e){

        ApiException apiException = new ApiException(e.getMessage(),e, HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
    }

    //    alreadyExist exception
    @ExceptionHandler(value=AlreadyExistException.class)
    public ResponseEntity<Object> handleApiRequestException(AlreadyExistException e){
        ApiException apiException = new ApiException(e.getMessage(),e, HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value=DeletedException.class)
    public ResponseEntity<Object> handleApiRequestException(DeletedException e){
        ApiException apiException = new ApiException(e.getMessage(),e, HttpStatus.GONE, HttpStatus.GONE.value(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.GONE);
    }

    @ExceptionHandler(value=RequestBodyRequired.class)
    public ResponseEntity<Object> handleApiRequestException(RequestBodyRequired e){
        ApiException apiException = new ApiException(e.getMessage(),e, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value=UnAuthorizedException.class)
    public ResponseEntity<Object> handleApiRequestException(UnAuthorizedException e){
        ApiException apiException = new ApiException(e.getMessage(),e, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value=BadRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(BadRequestException e){
        ApiException apiException = new ApiException(e.getMessage(),e, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }
}
