package com.audit.app.exception.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)

public class APIError {
	
	@Getter	@Setter	
	private String status;
	@Getter	@Setter	
	private String timestamp;
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
    }
	
	
	

}
