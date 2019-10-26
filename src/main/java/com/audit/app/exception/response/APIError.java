package com.audit.app.exception.response;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)

public class APIError {
	
	@Getter	@Setter	
	private HttpStatus status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss") 
	@Getter	@Setter	
	private Date timestamp;
	@Getter	@Setter	
	private String error;
	@Getter	@Setter	
	private String message;
	@Getter	@Setter	
	private String path;
	@Getter	@Setter	
	private String traceId;
	
	@Getter	@Setter	
    private List<APIFieldErrors> subErrors;
	
	public APIError(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp=new Date();
    }
	
	
	

}
