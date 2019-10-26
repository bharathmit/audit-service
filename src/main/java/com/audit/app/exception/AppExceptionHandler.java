package com.audit.app.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.audit.app.exception.response.APIError;
import com.audit.app.exception.response.APIFieldErrors;

import brave.Tracer;


@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired 
	private Tracer tracer;

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
        
        error.setTraceId(tracer.currentSpan().toString());
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        
        error.setSubErrors(fieldErrorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
    
    @ExceptionHandler({ BusinessException.class })
    protected ResponseEntity<Object> handleBadRequest(RuntimeException e, WebRequest request) {
    	BusinessException bexc = (BusinessException) e;
    	
    	APIError error = new APIError("Business Exception", bexc.getMessage());
    	
    	error.setTraceId(tracer.currentSpan().toString());
    	error.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        error.setStatus(HttpStatus.BAD_REQUEST);
        
    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
    }

}
