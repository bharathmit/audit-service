package com.audit.app.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean  {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
        	HttpServletRequest req = (HttpServletRequest) request;
        	String path = req.getRequestURI().substring(req.getContextPath().length());
        	
        	if(!path.contains("/login/") || !path.contains("/user/")){
        		Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest)request);
           	 	SecurityContextHolder.getContext().setAuthentication(authentication);
        	}
        	
        } catch (Throwable e) {
            //log.error("Exception ", e);
        }
        chain.doFilter(request, response);
    }

}