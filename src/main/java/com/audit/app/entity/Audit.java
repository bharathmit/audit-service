package com.audit.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;



@MappedSuperclass
public abstract class Audit implements Serializable{

	//private final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="created_date", nullable=false)
	@Getter	@Setter	
	private Date createdDate;
	
	@Column(name="created_by", nullable=false)
	@Getter	@Setter	
	private Long createdBy;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="last_modified_Date", nullable=true)
	@Getter	@Setter	
	private Date lastModifiedDate;
	
	@Column(name="last_modified_by", nullable=true)
	@Getter	@Setter	
	private Long lastModifiedBy;
	
	@Column(name="time_stamp", nullable=true,columnDefinition="timestamp default current_timestamp on update current_timestamp")
	@Getter	@Setter	
	private Date timeStamp ;


	@PrePersist
	public void prePersist() {
		try {
			/*UserDto userAccountModel = (UserDto) SecurityContextHolder
					.getContext().getAuthentication().getCredentials();*/

			//setCreatedBy(userAccountModel.getUserId());
			setCreatedDate(new Date());
			setCreatedBy(1l);
		} catch (Exception e) {
			//log.error("Audit prePersist method call", e);
		}

	}
	
	
	@PreUpdate
	public void preUpdate() {
		try {
			/*UserDto userAccountModel = (UserDto) SecurityContextHolder
					.getContext().getAuthentication().getCredentials();
			
			setLastModifiedBy(userAccountModel.getUserId());*/			
			setLastModifiedDate(new Date());
			setCreatedBy(1l);
		} catch (Exception e) {
			//log.error("Audit PreUpdate method call", e);
		}

	}

}
