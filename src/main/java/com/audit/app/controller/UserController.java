package com.audit.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.jasypt.digest.StringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.exception.BusinessException;
import com.audit.app.exception.InvalidRequestException;
import com.audit.app.exception.response.ErrorDescription;
import com.audit.app.payload.ResponseResource;
import com.audit.app.payload.UserDto;
import com.audit.app.payload.UserSearch;
import com.audit.app.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	StringDigester stringDigester;
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE } )
	public UserDto saveUser(@RequestBody @Valid UserDto reqObject, BindingResult bindingResult) {
		// same we have to do in custom validation
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Exception", bindingResult);
		}
		return userService.saveUser(reqObject);
	}
	
	@PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
	@RequestMapping(method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserDto editUser(@RequestBody @Valid UserDto reqObject, BindingResult bindingResult) {
		// same we have to do in custom validation
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Exception", bindingResult);
		}
		return userService.saveUser(reqObject);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<UserDto> getAllUser() {
		UserSearch searchObject=new UserSearch();
		return userService.getUser(searchObject);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(path="/{emailId}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseResource deleteUser(@PathVariable("emailId") String emailId) {
		return userService.deleteUser(emailId);
	}
	 
	@PreAuthorize("hasRole('ADMIN') && hasRole('USER')")
	@RequestMapping(value = "/change-password", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseResource changePassword(@RequestParam("emailId") final String emailId,@RequestParam("oldPassword") final String oldPassword, @RequestParam("password") final String password) {
		UserDto user = userService.findByEmailId(emailId);
		if (!stringDigester.matches(oldPassword, user.getPassword())) {
			throw new BusinessException(ErrorDescription.INVALID_PASSWORD.getMessage());
		}
		return userService.changePassword(emailId,password);
	}
	
	
	

}
