package com.audit.app.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.audit.app.entity.User;




public interface UserJPARepo extends JpaRepository< User, Long>{
	
	public User findByEmailId(String emailId);
	
	@Modifying(clearAutomatically = true)
	@Query("update User a SET a.lastLoginDate=:lastLoginDate  where a.userId = :userId")
    public int loginUpdate(@Param("userId") Long userId,@Param("lastLoginDate") Date lastLoginDate);
	
	@Modifying(clearAutomatically = true)
	@Query("update User a SET a.password=:password , a.passwordChangeDate=:passwordChangeDate where a.emailId = :emailId")
    public int passwordUpdate(@Param("emailId") String emailId,@Param("password") String password,@Param("passwordChangeDate") Date passwordChangeDate);


}
