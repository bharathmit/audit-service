package com.audit.app.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.exception.InvalidRequestException;
import com.audit.app.payload.LoginDto;
import com.audit.app.payload.LoginResponseDto;
import com.audit.app.payload.UserDto;
import com.audit.app.security.TokenAuthenticationService;
import com.audit.app.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    LoginService LoginService;
    
	 /** This method validates whether the provided user is authorized*/
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto,BindingResult bindingResult) 
    {
    	log.info("user login controller called");
    	if (bindingResult.hasErrors()){
			log.info("Http Request Validation Error");
			throw new InvalidRequestException("Exception", bindingResult);
		}
    	LoginResponseDto loginRespDto =new LoginResponseDto();
    	UserDto userDto=LoginService.loginUser(loginDto);

    	String JWTtoken=TokenAuthenticationService.addAuthentication(loginDto.getEmailId(), userDto);
    	
    	loginRespDto.setAccess_token(JWTtoken);
    	//loginRespDto.setExpires_in(expires_in);
    	loginRespDto.setUser(userDto);
    	
    	//JWTresponse.addHeader(TokenAuthenticationService.HEADER_STRING, TokenAuthenticationService.TOKEN_PREFIX + " " + JWTtoken);
        
        //log.info("LoginController.login method End");
        
        return loginRespDto;	
    }
	
	
	

}
