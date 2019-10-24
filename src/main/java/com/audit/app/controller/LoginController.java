package com.audit.app.controller;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.dto.LoginDto;
import com.audit.app.dto.UserDto;
import com.audit.app.exception.InvalidRequestException;
import com.audit.app.service.LoginService;
import com.cable.rest.dto.LoginResponseDto;
import com.cable.rest.exception.BadRequestException;
import com.cable.rest.response.ErrorResource;
import com.cable.rest.security.TokenAuthenticationService;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	/**initialize the AtomicInteger class*/
    AtomicInteger ctr=new AtomicInteger(1);
    
    @Autowired
    LoginService LoginService;
	
	 /** This method validates whether the provided user is authorized*/
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public @ResponseBody UserDto login(@RequestBody @Valid LoginDto loginDto,BindingResult bindingResult, HttpServletResponse JWTresponse) 
    {
    	
    	if (bindingResult.hasErrors()){
			//log.info("Http Request Validation Called");
			throw new InvalidRequestException("Exception", bindingResult);
		}
		
    	UserDto userDto=LoginService.loginUser(loginDto);

    	String JWTtoken=TokenAuthenticationService.addAuthentication(loginRespDto.getUser().getLoginId(), loginRespDto.getUser());
    	JWTresponse.addHeader(TokenAuthenticationService.HEADER_STRING, TokenAuthenticationService.TOKEN_PREFIX + " " + JWTtoken);
        
        //log.info("LoginController.login method End");
        
        return loginRespDto;	
    }
	
    
    /** This method invalidate the session whether the provided user is*/
    // authorized
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request) {
        log.info("Logged out called");
        HttpSession session = request.getSession(true);
        session.invalidate();
    }
	
	
	

}
