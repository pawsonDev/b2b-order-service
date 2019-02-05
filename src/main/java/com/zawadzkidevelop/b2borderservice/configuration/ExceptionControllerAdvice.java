package com.zawadzkidevelop.b2borderservice.configuration;

import com.zawadzkidevelop.b2borderservice.exception.ExceptionResponse;
import com.zawadzkidevelop.b2borderservice.exception.ProductQuantityBelowZeroException;
import com.zawadzkidevelop.b2borderservice.exception.ShoppingBasketValidationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.PersistenceException;
import java.util.Locale;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    @Autowired
    public ExceptionControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionResponse handlePersistenceException(PersistenceException e, Locale locale) {
        String message;
        if (e.getCause() instanceof JDBCConnectionException) {
            message = messageSource.getMessage("error.no.database.connection", null, locale);
        } else {
            message = messageSource.getMessage("error.database", null, locale);
        }
        ExceptionResponse response = new ExceptionResponse(message, getCauseMsg(e.getCause()), e.getClass().getName());

        log.error("handlePersistenceException", e);

        return response;
    }

    @ExceptionHandler({ProductQuantityBelowZeroException.class, ShoppingBasketValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleArgumentsValidationException(Exception e) {
        ExceptionResponse response = new ExceptionResponse("validation exception", e.getMessage(), e.getClass().getName());

        log.error("handleValidationException", e);

        return response;
    }

    private String getCauseMsg(Throwable cause) {
        return cause == null ? null : cause.getMessage();
    }
}