package com.fanhab.portal.exceptionhandler;


import com.fanhab.portal.exception.AbstractException;
import com.fanhab.portal.exception.GracefulError;
import com.fanhab.portal.exception.ServiceException;

import com.fanhab.portal.exception.WriteException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {

    public final String exceptionLogMode = "active";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuneTime(
            final RuntimeException exception,
            final HttpServletRequest request
    ) {
        GracefulError error = GracefulError.builder()
                .occurrenceTime(LocalDateTime.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(exception.getLocalizedMessage())
                .requestPath(request.getRequestURI())

                .build();
        logPrintStackTrace(exception);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<Object> handleBaseBusinessException(
            final AbstractException exception,
            final HttpServletRequest request
    ) {
        GracefulError error = GracefulError.builder()
                .occurrenceTime(LocalDateTime.now())
                .statusCode(exception.getHttpStatusValue())
                .statusMessage(exception.getHttpStatusMessage())
                .requestPath(request.getRequestURI())
                .build();
        logPrintStackTrace(exception);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleReadableException(
            final ServiceException exception,
            final HttpServletRequest request
    ) {
        GracefulError error = GracefulError.builder()
                .occurrenceTime(LocalDateTime.now())
                .statusCode(exception.getHttpStatusValue())
                .statusMessage(exception.getHttpStatus().getReasonPhrase())
                .gracefulMessage(exception.getMessage())
                .requestPath(request.getRequestURI())
                .build();
        logPrintStackTrace(exception);
        return buildResponseEntity(error);
    }

//    @ExceptionHandler(WriteException.class)
//    public ResponseEntity<Object> handleWritableException(
//            final WriteException exception,
//            final HttpServletRequest request
//    ) {
//        GracefulError error = GracefulError.builder()
//                .occurrenceTime(LocalDateTime.now())
//                .statusCode(exception.getHttpStatus().value())
//                .statusMessage(exception.getHttpStatus().getReasonPhrase())
//                .gracefulMessage(exception.getMessage())
//                .build();
//        logPrintStackTrace(exception);
//        // log.error(exceptionLogMode.equals("active") ? exception.getStringStackTrack() : "writable-exception" );
//        return buildResponseEntity(error);
//    }

    @ExceptionHandler(WriteException.class)
    public ResponseEntity<Object> handleWritableException(
            final WriteException exception,
            final HttpServletRequest request
    ) {
        String detailedMessage = exception.getMessage(); // پیام اصلی خطا
        if (exception.getMessageArgs() != null && exception.getMessageArgs().containsKey("details")) {
            detailedMessage += " | " + exception.getMessageArgs().get("details"); // اضافه کردن details
        }

        GracefulError error = GracefulError.builder()
                .occurrenceTime(LocalDateTime.now())
                .statusCode(exception.getHttpStatus().value())
                .statusMessage(exception.getHttpStatus().getReasonPhrase())
                .gracefulMessage(detailedMessage)
                .build();

        logPrintStackTrace(exception);
        return buildResponseEntity(error);
    }

    public static String getStringStackTrack(StackTraceElement[] stackTraceElements) {
        String stackTrace = "";
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement element = stackTraceElements[i];
            stackTrace = stackTrace + "\n" + element.toString();
        }
        stackTrace = stackTrace + "\n";
        stackTrace = stackTrace + "\n" + "***********************************************************************";
        stackTrace = stackTrace + "\n" + "***********************************************************************";
        stackTrace = stackTrace + "\n";

        return stackTrace;
    }

    public void logPrintStackTrace(Exception exception) {
        exception.printStackTrace();
        logger.error(exception);
    }



    protected ResponseEntity<Object> buildResponseEntity(final GracefulError error) {
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatusCode()));
    }

}
