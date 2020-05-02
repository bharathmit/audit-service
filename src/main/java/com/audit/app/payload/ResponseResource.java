package com.audit.app.payload;

import java.io.Serializable;

import lombok.Getter;

import com.audit.app.exception.response.ErrorDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseResource implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
    private String message;
	
	public ResponseResource(final ErrorDescription error){
		this.message= error.getMessage();
	}

}
