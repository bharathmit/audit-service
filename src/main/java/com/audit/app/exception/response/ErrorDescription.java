package com.audit.app.exception.response;

import lombok.Getter;

public enum ErrorDescription {
	
	SERVER_ERROR("Appliction Server Error, Please try again after some time."),
	TRANSACTION_SUCCESS("your transaction is successful"),
	TRANSACTION_FAILED("Something went wrong, Please try again later"),
	
	
	USER_EXIT("Email Id (OR) User Already Exit"),
	USER_NOT_EXIT("Email Id (OR) User Not Registered"),
	USER_ACCOUNT_ALREADY_ACTIVE("This email id already activated"),
	USER_ACCOUNT__ACTIVE("This email id activated"),
	USER_ACCOUNT_BLOCKED("This email id is Blocked Or In active"),
	
	INVALID_USER("Incorrect UserName"),
	INVALID_PASSWORD("Old Password Incorrect"),
	MOBILE_EXIT("Mobile Number Already Exit"),
	INVALID_TOKEN("Incoorrect Confirmation Token"),
	TOKEN_EXPIRED("Given Confirmation Token Expired"),
	
	USER_ACTIVATE_EMAIL("Activation link sent to your email id."),
	USER_PASSWORD_CHANGE("Your login password changed successfully"),
	USER_PASSWORD_EMAIL("Forgotpassword link sent to your email id."),
	USER_PASSWORD_TOKEN("Forgotpassword link Token Verified successfully."),
	;
	
	@Getter
    private String message;

    private ErrorDescription(String message) {
        this.message = message;
    }

}
