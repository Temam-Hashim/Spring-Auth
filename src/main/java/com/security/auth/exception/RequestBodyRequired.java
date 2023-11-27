package com.security.auth.exception;

public class RequestBodyRequired extends RuntimeException {

    public RequestBodyRequired(String message){
        super(message);
    }


}
