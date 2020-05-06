package com.audit.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.app.payload.ResponseResource;
import com.audit.app.repo.VerificationTokenRepo;
import com.audit.app.service.UserService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	UserService userService;

	@Autowired
	VerificationTokenRepo tokenRepository;

	@RequestMapping(value = "/{emailId}/activation", method = RequestMethod.GET)
	public ResponseResource createUserActivationToken(@PathVariable("emailId") final String emailId) {
		return userService.createUserActivationToken(emailId);
	}

	@RequestMapping(value = "/{token}/confirm-account", method = RequestMethod.GET)
	public ResponseResource confirmUserActivationToken(@PathVariable("token") final String token) {
		return userService.confirmUserActivationToken(token);
	}

	//In Client UI, Account Or forgot password token verified successfully, This API will create the password for the User.
	@RequestMapping(value = "/{emailId}/password-creation", method = RequestMethod.PUT)
	public ResponseResource passwordCreation(@PathVariable("emailId") final String emailId,@RequestParam("password") final String password) {
		return userService.changePassword(emailId,password);
	}

	@RequestMapping(value = "/{emailId}/forgot-password", method = RequestMethod.GET)
	public ResponseResource forgotPassword(@PathVariable("emailId") final String emailId) {
		return userService.createUserPasswordToken(emailId);
	}

	@RequestMapping(value = "/{token}/confirm-password", method = RequestMethod.GET)
	public ResponseResource confirmPassword(@PathVariable("token") final String token) {
		return userService.confirmUserPasswordToken(token);
	}

}
