package com.audit.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.audit.app.entity.Role;



public interface RoleJPARepo extends JpaRepository< Role, Long>{
	
	public Role findByRoleName(String roleName);

}
