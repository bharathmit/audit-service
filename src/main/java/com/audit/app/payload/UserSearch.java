package com.audit.app.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSearch implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Getter	@Setter	
	private String firstName;
	
	@Getter	@Setter	
	private String emailId;
	
	@Getter	@Setter	
	private String mobile;
	
	@Getter	@Setter	
	private int pageNumber;
	
	@Getter	@Setter	
	private int itemsPerPage;
	
	@Setter	
	private int firstResult;
	
	public int getFirstResult() {
        return (pageNumber - 1) * itemsPerPage;
    }

}
