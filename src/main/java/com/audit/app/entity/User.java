package com.audit.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.audit.app.constants.AuthProvider;
import com.audit.app.constants.Gender;
import com.audit.app.constants.MaritalStatus;
import com.audit.app.constants.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user")
public class User extends Audit implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false)
	@Getter	@Setter	
	private Long userId;
	
	@Column(nullable=false)
	@Getter	@Setter	
	private String firstName;
	
	@Column
	@Getter	@Setter	
	private String middleName;
	
	@Column
	@Getter	@Setter	
	private String lastName;
	
	@Column(updatable = false)
	@Getter	@Setter	
	private String password;
	
	@Column
	@Getter	@Setter	
	private String mobile;
	
	@Column(unique=true,nullable=false,updatable = false)
	@Getter	@Setter	
	private String emailId;
	
	@Column
	@NotNull
    @Enumerated(EnumType.STRING)
	@Getter	@Setter	
    private AuthProvider provider;
	
	@Column
	@Getter	@Setter	
	private String providerId;
	
	@Temporal( TemporalType.DATE)
	@Column
	@Getter	@Setter	
	private Date dob;
	
	@Column
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	
	private Gender gender;
	
	@Column
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	
	private MaritalStatus maritalStatus;
	
	@Column
	@Getter	@Setter	
	private String otherName;
	
	@Column(length = 65535,columnDefinition="Text")
	@Getter	@Setter	
	private String address;
	
	@Column
	@Getter	@Setter	
	private String pinCode;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	
	private Status status;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="user")
	@Getter	@Setter	
	private List<UserRole> userRoles;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@Getter	@Setter	
	private Date lastLoginDate;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@Getter	@Setter	
	private Date passwordChangeDate;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@Getter	@Setter	
	private Date lockDate;
	
	@Column
	@Getter	@Setter	
	private byte[] photo;
	
	

}
