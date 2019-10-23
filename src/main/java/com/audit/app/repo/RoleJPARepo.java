package com.audit.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.audit.app.entity.Role;
import com.audit.app.entity.User;



public interface RoleJPARepo extends JpaRepository< Role, Long>{
	
	public Role findByRoleName(String roleName);

}
