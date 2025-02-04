package com.fanhab.portal.exception;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends AbstractException{


    private static final long serialVersionUID = 1L;
    private String gracefulMessage;

    public ServiceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }



    public static ServiceException notFoundException(final String message, final HttpStatus httpStatus) {
        ServiceException exception = new ServiceException(message, httpStatus);

//        exception.setStackTrace(exception.getStackTrace());
        return exception;
    }

    public static ServiceException badRequestException(final String error) {
        ServiceException exception =
                new ServiceException(error,HttpStatus.BAD_REQUEST);


//        exception.setStackTrace(exception.getStackTrace());
        return exception;
    }


}
