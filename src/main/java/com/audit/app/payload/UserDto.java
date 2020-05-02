package com.audit.app.payload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.audit.app.constants.Gender;
import com.audit.app.constants.MaritalStatus;
import com.audit.app.constants.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	
	@Getter	@Setter	
	private Long userId;
	
	@Getter	@Setter	
	private String firstName;
	
	@Getter	@Setter	
	private String middleName;
	
	@Getter	@Setter	
	private String lastName;
	
	@Getter(onMethod = @__( @JsonIgnore ))
	@Setter	
	private String password;
	
	@Getter	@Setter	
	private String mobile;

	@Getter	@Setter	
	private String emailId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") 
	@Getter	@Setter	
	private Date dob;
	
	@Getter	@Setter	
	private Gender gender;
	
	@Getter	@Setter	
	private MaritalStatus maritalStatus;
	
	@Getter	@Setter	
	private String address;
	
	@Getter	@Setter	
	private String pinCode;
	
	@Getter	@Setter	
	private Status status;
	
	@Getter	@Setter	
	private List<UserRoleDto> userRoles=new ArrayList<UserRoleDto>();
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	@Getter	@Setter	
	private Date lastLoginDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	@Getter	@Setter	
	private Date passwordChangeDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	@Getter	@Setter	
	private Date lockDate;
	
	@Getter	@Setter	
	private byte[] photo;
	
	
}
