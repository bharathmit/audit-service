package com.audit.app.security.oauth2.provider;

import java.util.Map;

import com.audit.app.constants.AuthProvider;
import com.audit.app.exception.BusinessException;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
        	throw new BusinessException("Sorry! Login with " + registrationId + " is not supported yet.");            
        }
    }
}
