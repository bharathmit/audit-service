package com.audit.app.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto  implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Email ID can not be null ... ")
	@Getter	@Setter	
	private String emailId;
	
	@NotNull(message = "Password can not be null ... ")
	@Getter	@Setter	
	private String password;
}
