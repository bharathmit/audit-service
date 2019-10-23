package com.audit.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.audit.app.constants.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="role")
public class Role extends Audit implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	@Getter	@Setter	
	protected Long roleId;

	@Column(nullable=false, length=250)
	@Getter	@Setter	
	protected String roleName;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	
	private Status status;
	
	@Transient
	public Long getId() {
		return this.roleId.longValue();
	}

	@Transient
	public String getLogDeatil() {
		StringBuilder sb = new StringBuilder();
		sb.append(" Id : ").append(roleId)
		.append(" Role Name : ").append(roleName);
 
		return sb.toString();
	}
	
	
	
}
