package com.audit.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.audit.app.entity.UserRole;



public interface UserRoleJPARepo extends JpaRepository< UserRole, Long>{

}
