package com.walmart.exception;

public class ReservationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String errorCode = "";
    public ReservationException(){
        super();
    }
    
    public ReservationException(Integer integer) {
       
    }
    
    public ReservationException(String error) {
        super(error);
    }
    
    
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
