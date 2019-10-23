package com.audit.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.audit.app.constants.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user_role")
public class UserRole extends Audit implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter	@Setter	
	private Long userRoleId;
	
	@ManyToOne
	@JoinColumn(name="user_Id", nullable=false)
	@Getter	@Setter	
	private User user;
	
	@ManyToOne
	@JoinColumn(name="role_Id", nullable=false)
	@Getter	@Setter	
	private Role role;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	
	private Status status;
	
	
	
	

}
