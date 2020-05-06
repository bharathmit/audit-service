package com.audit.app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import com.audit.app.payload.UserDto;
import com.audit.app.payload.UserRoleDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {
	
	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret"; // secret key
	public static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";
	
	public static String createToken(String emailId,UserDto user) {
		String JWTtoken = Jwts
				.builder()
				.setSubject(emailId)
				.claim("user", user)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return JWTtoken;
	}
	
	static Authentication getAuthentication(HttpServletRequest request) throws IOException {
		//log.error("URL = " + request.getRequestURL());
		String token = request.getHeader(HEADER_STRING);
		if (!StringUtils.isEmpty(token)  && token.startsWith(TOKEN_PREFIX) ) {
			// parse the token.
			Claims claims = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
			LinkedHashMap userObj= (LinkedHashMap) claims.get("user");
			ObjectMapper mapper=new ObjectMapper();
			String json= mapper.writeValueAsString(userObj);
			UserDto user = mapper.readValue(json, UserDto.class);
			
			List<GrantedAuthority> authorities = new ArrayList<>();;
	    	for (UserRoleDto role : user.getUserRoles()) {
	    		authorities.add(new SimpleGrantedAuthority(role.getRole().getRoleName()));
	    	}
			
			return claims != null ? new UsernamePasswordAuthenticationToken(user,
					user, authorities) : null;
		}
		return null;
	}
	
	
		


}
