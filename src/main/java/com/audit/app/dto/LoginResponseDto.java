package com.audit.app.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	String access_token; 
    
    @Getter @Setter
    String token_type="Bearer"; 
    
    @Getter @Setter
    String expires_in; 
    
    @Getter @Setter
    UserDto user; 

}
