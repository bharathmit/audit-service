package com.audit.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.dto.UserDto;
import com.audit.app.exception.InvalidRequestException;
import com.audit.app.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	
	@Autowired
	UserService userService;
	
	@RequestMapping(method=RequestMethod.POST)
	public UserDto  saveUser(@RequestBody @Valid UserDto reqObject, BindingResult bindingResult){
		//same we have to do in custom validation
		if (bindingResult.hasErrors()){
			throw new InvalidRequestException("Exception", bindingResult);
		}
		return userService.saveUser(reqObject);	
	}
	
	/*//mobile & email unique ===> user exit with status flag msg
	@RequestMapping(value="/userexit",method=RequestMethod.POST)
	public boolean  exitUser(@RequestBody UserDto reqObject){
		return userService.exitEmail(reqObject.getEmailId());
	}
	
	@RequestMapping(value="/userlist",method=RequestMethod.POST)
	public Object getUser(@RequestBody UserSearch searchObject){
		return userService.getUser(searchObject);
	}
	
	@RequestMapping(value="/deleteuser",method=RequestMethod.POST)
	public Object deleteUser(@RequestBody UserSearch searchObject){
		return userService.deleteUser(searchObject);
	}
	
	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	public Object  changePassword(@RequestBody UserDto reqObject){
		return userService.changePassword(reqObject);
	}*/
	
	
	
	
	
	
	// user login ===> update login time & if reset password flag is true, change the password page. if email id not verified error msg & able to generate new email verify link.
	

}
