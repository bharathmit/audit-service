package com.audit.app.controller;

import javax.validation.Valid;

import org.jasypt.digest.StringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.dto.ResponseResource;
import com.audit.app.dto.UserDto;
import com.audit.app.exception.BusinessException;
import com.audit.app.exception.InvalidRequestException;
import com.audit.app.exception.response.ErrorDescription;
import com.audit.app.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	StringDigester stringDigester;

	@RequestMapping(method = RequestMethod.POST)
	public UserDto saveUser(@RequestBody @Valid UserDto reqObject, BindingResult bindingResult) {
		// same we have to do in custom validation
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Exception", bindingResult);
		}
		return userService.saveUser(reqObject);
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.PUT)
	public ResponseResource changePassword(@RequestParam("emailId") final String emailId,@RequestParam("oldPassword") final String oldPassword, @RequestParam("password") final String password) {
		UserDto user = userService.findByEmailId(emailId);
		if (!stringDigester.matches(oldPassword, user.getPassword())) {
			throw new BusinessException(ErrorDescription.INVALID_PASSWORD.getMessage());
		}
		return userService.changePassword(emailId,password);
	}

	/*
	 * @RequestMapping(value="/userlist",method=RequestMethod.POST) public Object
	 * getUser(@RequestBody UserSearch searchObject){ return
	 * userService.getUser(searchObject); }
	 * 
	 * @RequestMapping(value="/deleteuser",method=RequestMethod.POST) public Object
	 * deleteUser(@RequestBody UserSearch searchObject){ return
	 * userService.deleteUser(searchObject); }
	 */

}
