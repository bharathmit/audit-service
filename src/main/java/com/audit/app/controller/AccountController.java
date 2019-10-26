package com.audit.app.controller;

import org.jasypt.digest.StringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.dto.ResponseResource;
import com.audit.app.dto.UserDto;
import com.audit.app.exception.BusinessException;
import com.audit.app.exception.response.ErrorDescription;
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

	@RequestMapping(value = "/password-creation", method = RequestMethod.POST)
	public ResponseResource passwordCreation(@RequestBody UserDto userDto) {
		return userService.changePassword(userDto);
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
