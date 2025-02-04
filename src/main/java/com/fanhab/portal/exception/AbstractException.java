package com.fanhab.portal.exception;


import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractException extends RuntimeException  {


    private static final long serialVersionUID = 1L;
    private Map<String, Object> messageArgs;
    protected HttpStatus httpStatus;
    public AbstractException(){

    }

    public AbstractException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;

    }

    public AbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractException(Throwable cause) {
        super(cause);
    }

    protected AbstractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }





    /**
     * @return always return HttpStatus.BAD_REQUEST
     */
    public int getHttpStatusValue() {
        return httpStatus.value();
    }

    public String getHttpStatusMessage(){
        return httpStatus.getReasonPhrase();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getStringStackTrack() {
        String stackTrace = "";
        for (int i = 0; i < getStackTrace().length; i++) {
            StackTraceElement element = getStackTrace()[i];
            stackTrace = stackTrace + "\n" + element.toString();
        }

        stackTrace =  stackTrace + "\n"+"***********************************************************************";
        stackTrace =  stackTrace + "\n"+"***********************************************************************";
        return stackTrace;
    }
    public void addMessageArg(final String messageArg, final Object messageVal) {
        if (this.messageArgs==null) {
            this.messageArgs = new HashMap<>();
        }
        this.messageArgs.put(messageArg, messageVal);
    }
    public Map<String, Object> getMessageArgs() {
        return messageArgs != null ? messageArgs : new HashMap<>();
    }

}
