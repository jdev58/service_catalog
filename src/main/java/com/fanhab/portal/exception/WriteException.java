package com.fanhab.portal.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@AllArgsConstructor
@NoArgsConstructor
public class WriteException extends AbstractException  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gracefulMessage;
    private HttpStatus httpStatus;



    public static WriteException alreadyExistException(final String entityName, final String details, final HttpStatus httpStatus) {
        WriteException exception =
                new WriteException("error." + entityName + ".already-exist", httpStatus);
        exception.addMessageArg("details", details);
        return exception;
    }






    @Override
    public String getMessage(){
        return this.gracefulMessage;
    }

    public HttpStatus getHttpStatus(){
        return this.httpStatus;
    }
}
