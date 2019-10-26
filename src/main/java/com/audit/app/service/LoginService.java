package com.audit.app.service;

import org.jasypt.digest.StringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.audit.app.constants.Status;
import com.audit.app.dto.LoginDto;
import com.audit.app.dto.UserDto;
import com.audit.app.exception.BusinessException;
import com.audit.app.exception.response.ErrorDescription;

@Service
public class LoginService {
	
	
	@Autowired
	UserService userService;
	
	@Autowired
	StringDigester stringDigester;
	
	
	public UserDto loginUser(LoginDto loginDto){
		UserDto userDto = userService.findByEmailId(loginDto.getEmailId());

		if (StringUtils.isEmpty(userDto)) {
			throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
		}
		
		if(userDto.getStatus()!=Status.Active) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_BLOCKED.getMessage());
    	}

		if(!stringDigester.matches(loginDto.getPassword(),userDto.getPassword())){
			throw new BusinessException(ErrorDescription.INVALID_PASSWORD.getMessage());
		}
		
		if(!userService.loginUpdate(userDto.getUserId())){
			throw new BusinessException(ErrorDescription.SERVER_ERROR.getMessage());
		}
		return userDto;
	}		
	
	

}
