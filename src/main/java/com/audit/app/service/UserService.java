
package com.audit.app.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jasypt.digest.StringDigester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.audit.app.constants.AuthProvider;
import com.audit.app.constants.Status;
import com.audit.app.constants.TokenType;
import com.audit.app.entity.User;
import com.audit.app.entity.UserRole;
import com.audit.app.entity.VerificationToken;
import com.audit.app.exception.BusinessException;
import com.audit.app.exception.response.ErrorDescription;
import com.audit.app.payload.EmailDto;
import com.audit.app.payload.ResponseResource;
import com.audit.app.payload.UserDto;
import com.audit.app.payload.UserRoleDto;
import com.audit.app.payload.UserSearch;
import com.audit.app.repo.UserJPARepo;
import com.audit.app.repo.UserRoleJPARepo;
import com.audit.app.repo.VerificationTokenRepo;
import com.audit.app.utils.ModelEntityMapper;

@Service
public class UserService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
    
	@PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserJPARepo userRepo;

    @Autowired
    UserRoleJPARepo userRoleRepo;

    @Autowired
    StringDigester stringDigester;
    
    @Autowired
    VerificationTokenRepo tokenRepository;
    
    @Autowired
    EmailService emailService;
    
    @Value("${webapp.api}")
    String apiUrl; 
    
    
    @Transactional
    public UserDto saveUser(UserDto userObject) {
    	boolean newUser = false;
    	
    	// New user Always User Id will be Zero
    	if(StringUtils.isEmpty(userObject.getUserId())) {
    		UserDto user=findByEmailId(userObject.getEmailId());
            
        	if (!StringUtils.isEmpty(user)) {
        		throw new BusinessException(ErrorDescription.USER_EXIT.getMessage());
            }
        	newUser=true;
    	}
    	
        if(userObject.getStatus()==Status.InActive){
        	userObject.setLockDate(new Date());	
        }

        User userEntity = (User) ModelEntityMapper.converObjectToPoJo(userObject, User.class);

        deleteUserRole(userObject.getUserId());
        
        userEntity = userRepo.save(userEntity);

        userObject.setUserId(userEntity.getUserId());
        
        for (UserRoleDto roleUser : userObject.getUserRoles()) {

            UserRole userRoleEntity = (UserRole) ModelEntityMapper.converObjectToPoJo(roleUser, UserRole.class);

            userRoleEntity.setUser(userEntity);

            userRoleRepo.save(userRoleEntity);
        }
        
        if(newUser && userObject.getProvider().equals(AuthProvider.springeco)) {
        	emailUserActivationToken(userEntity);
        }
        
        return userObject;
    }
    
    public ResponseResource createUserActivationToken(String emailId) {
    	
    	User user = userRepo.findByEmailId(emailId.trim());
    	
    	if (StringUtils.isEmpty(user)) {
    		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
        }
    	
    	if(user.getStatus().equals(Status.Active)) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_ALREADY_ACTIVE.getMessage());
    	}
    	
    	emailUserActivationToken(user);
    	return new ResponseResource(ErrorDescription.USER_ACTIVATE_EMAIL);
    }
    
    
    public ResponseResource confirmUserActivationToken(String token){
		final VerificationToken verificationToken = getVerificationToken(token);
		if (StringUtils.isEmpty(verificationToken)) {
			throw new BusinessException(ErrorDescription.INVALID_TOKEN.getMessage());
		}

		final User user = verificationToken.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			throw new BusinessException(ErrorDescription.TOKEN_EXPIRED.getMessage());
		}
		
		if(user.getStatus().equals(Status.Active)) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_ALREADY_ACTIVE.getMessage());
    	}

		user.setStatus(Status.Active);
		userRepo.saveAndFlush(user);
		
		return new ResponseResource(ErrorDescription.USER_ACCOUNT__ACTIVE);
	}
    
    public VerificationToken getVerificationToken(String VerificationToken){
		return tokenRepository.findByToken(VerificationToken);
	}
    
    
    @Async
	public void emailUserActivationToken(User userEntity) {
		try {
			String token = UUID.randomUUID().toString();
			createUserToken(userEntity, token, TokenType.AccountActivation);

			EmailDto emailRequest = new EmailDto();

			emailRequest.setTo(userEntity.getEmailId());
			emailRequest.setSubject("Verify Email Address");
			emailRequest.setTemplateLocation("emailverify-template");
			emailRequest.setFrom("no-reply@gstp.com");

			Map model = new HashMap();
			String appUrl = apiUrl + "account/confirm-account?token=" + token;
			model.put("appUrl", appUrl);

			emailRequest.setModel(model);

			emailService.sendEmailMessage(emailRequest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}    
    
    public void createUserToken(final User userEntity, final String token,TokenType tokenType) {
        final VerificationToken myToken = new VerificationToken(token, userEntity,tokenType);
        tokenRepository.save(myToken);
    }
    
    @Transactional
	public ResponseResource changePassword(String emailId,String password) {
    	UserDto user = findByEmailId(emailId);
    	
    	if(user.getStatus()!=Status.Active) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_BLOCKED.getMessage());
    	}
		userRepo.passwordUpdate(emailId,stringDigester.digest(password),new Date());
		return new ResponseResource(ErrorDescription.USER_PASSWORD_CHANGE);
	}
	
	public ResponseResource createUserPasswordToken(String emailId) {
    	
		User user = userRepo.findByEmailId(emailId.trim());
    	
    	if (StringUtils.isEmpty(user)) {
    		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
        }
    	
    	if(user.getStatus()!=Status.Active) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_BLOCKED.getMessage());
    	}
    	
    	
    	emailForgotPasswordToken(user);
    	
    	return new ResponseResource(ErrorDescription.USER_PASSWORD_EMAIL);
    }
	
	@Async
	public void emailForgotPasswordToken(User userEntity) {
		try {
			String token = UUID.randomUUID().toString();
			createUserToken(userEntity, token, TokenType.ForgotPassword);

			EmailDto emailRequest = new EmailDto();

			emailRequest.setTo(userEntity.getEmailId());
			emailRequest.setSubject("Reset your GSTP Cloud password");
			emailRequest.setTemplateLocation("forgot-template");
			emailRequest.setFrom("no-reply@gstp.com");

			Map model = new HashMap();
			String appUrl = apiUrl + "account/confirm-password?token=" + token;
			model.put("appUrl", appUrl);

			emailRequest.setModel(model);

			emailService.sendEmailMessage(emailRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    
	 public ResponseResource confirmUserPasswordToken(String token){
			final VerificationToken verificationToken = getVerificationToken(token);
			if (StringUtils.isEmpty(verificationToken)) {
				throw new BusinessException(ErrorDescription.INVALID_TOKEN.getMessage());
			}

			final User user = verificationToken.getUser();
			final Calendar cal = Calendar.getInstance();
			if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				throw new BusinessException(ErrorDescription.INVALID_TOKEN.getMessage());
			}	
			
			if (StringUtils.isEmpty(user)) {
	    		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
	        }
	    	
	    	if(user.getStatus()!=Status.Active) {
	    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_BLOCKED.getMessage());
	    	}
			
	    	return new ResponseResource(ErrorDescription.USER_PASSWORD_TOKEN);
		}
    
    
    public UserDto findByEmailId(String emailId) {
    	UserDto resultObject = new UserDto();
        User entityObject = userRepo.findByEmailId(emailId.trim());
        resultObject = (UserDto) ModelEntityMapper.converObjectToPoJo(entityObject, UserDto.class);
        if (StringUtils.isEmpty(resultObject)) {
    		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
        }
        return resultObject;
    }
    
    @Transactional
    public boolean loginUpdate(Long userId,AuthProvider provider){
    	if(userRepo.loginUpdate(userId,new Date(),provider)>0){
    		return true;
    	}
		return false;
    }
    
  

    @Transactional
    public boolean deleteUserRole(Long userId) {
        Session session = (Session) entityManager.getDelegate();
        String hsql = " delete from UserRole where user_Id= " + userId + " ";
        int row = session.createQuery(hsql).executeUpdate();
        log.info("Number of User Role Delete from DB {} ", row);
        return true;
    }

    public List<UserDto> getUser(UserSearch search) {
    	 List<UserDto> userList = new ArrayList<UserDto>();
    	 int totalCount;
    	try {           
            Session session = entityManager.unwrap(Session.class);

            Criteria criteria = session.createCriteria(User.class);

            if (!StringUtils.isEmpty(search.getFirstName())) {
                criteria.add(Restrictions.eq("firstName", search.getFirstName()));
            }
            
            if (!StringUtils.isEmpty(search.getEmailId())) {
                criteria.add(Restrictions.eq("emailId", search.getEmailId()));
            }
            
            if (!StringUtils.isEmpty(search.getMobile())) {
                criteria.add(Restrictions.eq("mobile", search.getMobile()));
            }

            totalCount=criteria.list().size();
            
            List<User> list = criteria.setFirstResult(search.getFirstResult()).setMaxResults(search.getItemsPerPage()).list();
            
            

            for (User entityObject : list) {
                UserDto userDto = (UserDto) ModelEntityMapper.converObjectToPoJo(entityObject, UserDto.class);
                userList.add(userDto);
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Transactional
    public ResponseResource deleteUser(String emailId) {
        try {
        	UserDto user=findByEmailId(emailId);
        	if (StringUtils.isEmpty(user)) {
        		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
            }
        	deleteUserRole(user.getUserId());
            userRepo.deleteByEmailId(emailId);
            return new ResponseResource(ErrorDescription.TRANSACTION_SUCCESS);
        } catch (Exception e) {
        	throw new BusinessException(ErrorDescription.TRANSACTION_FAILED.getMessage());
        }
    }
    
   
    
    

}
