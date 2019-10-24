package com.audit.app.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.audit.app.constants.TokenType;

import lombok.Getter;
import lombok.Setter;

@Entity
public class VerificationToken implements Serializable{
	private static final long serialVersionUID = 1L;

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter @Getter
    private Long id;
    
    @Setter @Getter
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @Setter @Getter
    private User user;
    
    @Setter @Getter
    private Date expiryDate;
    
    @Column(nullable=false)
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	
	private TokenType type;

    public VerificationToken() {
        super();
    }

    public VerificationToken(String token) {
        super();

        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public VerificationToken(String token, User user, TokenType type) {
        super();

        this.token = token;
        this.user = user;
        this.type=type;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

}
