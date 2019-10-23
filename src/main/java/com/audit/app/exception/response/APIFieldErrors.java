package com.audit.app.exception.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIFieldErrors {
	
	@Getter	@Setter	
	private String object;
	@Getter	@Setter	
	private String field;
	@Getter	@Setter	
	private Object rejectedValue;
	@Getter	@Setter	
	private String message;
	

}
