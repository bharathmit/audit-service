package com.audit.app.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.audit.app.exception.response.APIError;
import com.audit.app.exception.response.APIFieldErrors;


@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ InvalidRequestException.class })
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        InvalidRequestException ire = (InvalidRequestException) e;
        List<APIFieldErrors> fieldErrorResources = new ArrayList<>();

        List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
        	APIFieldErrors fieldErrorResource = new APIFieldErrors();
        	fieldErrorResource.setField(fieldError.getField());
        	fieldErrorResource.setObject(fieldError.getObjectName());
        	fieldErrorResource.setRejectedValue(fieldError.getRejectedValue());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            fieldErrorResources.add(fieldErrorResource);
        }

        APIError error = new APIError("InvalidRequest", ire.getMessage());
        error.setSubErrors(fieldErrorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
    
    @ExceptionHandler({ BusinessException.class })
    protected ResponseEntity<Object> handleBadRequest(RuntimeException e, WebRequest request) {
    	BusinessException bexc = (BusinessException) e;
    	
    	APIError error = new APIError("Business Exception", bexc.getMessage());
    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
    }

}
