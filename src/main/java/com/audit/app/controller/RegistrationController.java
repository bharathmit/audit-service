/*package com.audit.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.dto.UserDto;
import com.audit.app.exception.ResponseResource;
import com.audit.app.service.UserService;

@RestController
@RequestMapping("/user")
public class RegistrationController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public UserDto registration(@RequestBody UserDto userDto) {
		return userService.saveUser(userDto);
	}
	
	
	//email verify
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public UserDto updateUser(@RequestBody UserDto userDto) {
		return userService.saveUser(userDto);
	}
	
	//resend verification link
	
	
	
	//forgot password   ---> email send & reset password flag db
	
	//change password  ---->  redirect to login page & reset password flag to false db
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public ResponseResource changePassword(@RequestBody UserDto userDto) {
		return userService.changePassword(userDto);
	}
	
	
	//update profile

	
	

}
*/