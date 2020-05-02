package com.audit.app.security.oauth2;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.audit.app.constants.AuthProvider;
import com.audit.app.constants.Status;
import com.audit.app.entity.Role;
import com.audit.app.exception.BusinessException;
import com.audit.app.payload.RoleDto;
import com.audit.app.payload.UserDto;
import com.audit.app.payload.UserRoleDto;
import com.audit.app.repo.RoleJPARepo;
import com.audit.app.security.UserPrincipal;
import com.audit.app.security.oauth2.provider.OAuth2UserInfo;
import com.audit.app.security.oauth2.provider.OAuth2UserInfoFactory;
import com.audit.app.service.UserService;
import com.audit.app.utils.ModelEntityMapper;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;
    
    @Autowired
    RoleJPARepo roleJPARepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
        	throw new BusinessException("Email not found from OAuth2 provider");
        }

        UserDto user = userService.findByEmailId(oAuth2UserInfo.getEmail());
    
        AuthProvider provider=AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        
        if(!StringUtils.isEmpty(user)) { 
        	userService.loginUpdate(user.getUserId(),provider);           
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private UserDto registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        UserDto user = new UserDto();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFirstName(oAuth2UserInfo.getName());
        user.setEmailId(oAuth2UserInfo.getEmail());
        // later will download and save
        //user.setImageUrl(oAuth2UserInfo.getImageUrl());
        
        user.setLastLoginDate(new Date());
        user.setStatus(Status.Active);
        
        UserRoleDto userRole=new UserRoleDto();
        Role roleEntity=roleJPARepo.findByRoleName("User");
        RoleDto roleObject = (RoleDto) ModelEntityMapper.converObjectToPoJo(roleEntity, RoleDto.class);
        userRole.setStatus(Status.Active);
        userRole.setRole(roleObject);
        user.getUserRoles().add(userRole);
        
        return userService.saveUser(user);
    }

}
