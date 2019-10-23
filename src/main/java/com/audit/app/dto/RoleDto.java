package com.audit.app.dto;

import java.io.Serializable;

import com.audit.app.constants.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Getter	@Setter	
	Long roleId;
	
	@Getter	@Setter	
	String roleName;
	
	@Getter	@Setter	
	Status status;

}
