package com.walmart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LevelNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;
    private String errorCode = "";
    public LevelNotFoundException(){
        super();
    }
    
    public LevelNotFoundException(Integer integer) {
        // TODO Auto-generated constructor stub
    }
    
    public LevelNotFoundException(String error) {
        super(error);
    }
    
    
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

   
    
    
    
}
