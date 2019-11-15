package com.audit.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.dto.ResponseResource;
import com.audit.app.dto.UserDto;
import com.audit.app.repo.VerificationTokenRepo;
import com.audit.app.service.UserService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	UserService userService;

	@Autowired
	VerificationTokenRepo tokenRepository;

	@RequestMapping(value = "/activation", method = RequestMethod.GET)
	public ResponseResource createUserActivationToken(@RequestParam("emailId") final String emailId) {
		return userService.createUserActivationToken(emailId);
	}

	@RequestMapping(value = "/confirm-account", method = RequestMethod.GET)
	public UserDto confirmUserActivationToken(@RequestParam("token") final String token) {
		return userService.confirmUserActivationToken(token);
	}

	@RequestMapping(value = "/password-creation", method = RequestMethod.PUT)
	public ResponseResource passwordCreation(@RequestParam("emailId") final String emailId,@RequestParam("password") final String password) {
		return userService.changePassword(emailId,password);
	}

	@RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	public ResponseResource forgotPassword(@RequestParam("emailId") final String emailId) {
		return userService.createUserPasswordToken(emailId);
	}

	@RequestMapping(value = "/confirm-password", method = RequestMethod.GET)
	public UserDto confirmPassword(@RequestParam("token") final String token) {
		return userService.confirmUserPasswordToken(token);
	}

}
