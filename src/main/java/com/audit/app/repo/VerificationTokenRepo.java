package com.audit.app.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.audit.app.entity.VerificationToken;



public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    public VerificationToken findByToken(String token);

}
