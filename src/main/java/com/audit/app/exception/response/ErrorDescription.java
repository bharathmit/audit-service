package com.audit.app.exception.response;

import lombok.Getter;

public enum ErrorDescription {
	
	SERVER_ERROR("Appliction Server Error, Please try again after some time."),
	TRANSACTION_SUCCESS("your transaction is successful"),
	
	
	USER_EXIT("Email Id Already Exit"),
	USER_NOT_EXIT("Email Id Not Registered"),
	USER_ACCOUNT_INACTIVE("This email id already activated Or Blocked"),
	USER_ACCOUNT_BLOCKED("This email id is Blocked Or In active"),
	
	INVALID_USER("Incorrect UserName"),
	INVALID_PASSWORD("Incorrect Password"),
	MOBILE_EXIT("Mobile Number Already Exit"),
	INVALID_TOKEN("Incoorrect Registration confirmation Token"),
	TIME_OUT("Registration confirmation Token Expiryed"),
	
	USER_ACTIVATE_EMAIL("Activation link sent to your email id."),
	USER_PASSWORD_CHANGE("Your login password changed successfully"),
	USER_PASSWORD_EMAIL("Forgotpassword link sent to your email id."),
	
	;
	
	@Getter
    private String message;

    private ErrorDescription(String message) {
        this.message = message;
    }

}
