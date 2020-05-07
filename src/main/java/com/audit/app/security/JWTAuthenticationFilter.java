package com.audit.app.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean  {
	
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
        	logger.info("JWT OAuth Fillter Security context setting");
    		Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest)request);
       	 	SecurityContextHolder.getContext().setAuthentication(authentication);
        	
        } catch (Throwable e) {
        	logger.error("Could not set user authentication in security context", e);
        }
        chain.doFilter(request, response);
    }

}