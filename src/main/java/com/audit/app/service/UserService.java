
package com.audit.app.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.jasypt.digest.StringDigester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.audit.app.constants.Status;
import com.audit.app.constants.TokenType;
import com.audit.app.dto.EmailDto;
import com.audit.app.dto.ResponseResource;
import com.audit.app.dto.UserDto;
import com.audit.app.dto.UserRoleDto;
import com.audit.app.entity.User;
import com.audit.app.entity.UserRole;
import com.audit.app.entity.VerificationToken;
import com.audit.app.exception.BusinessException;
import com.audit.app.exception.response.ErrorDescription;
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
    EmailNotificationService emailService;
    
    
    @Transactional
    public UserDto saveUser(UserDto userObject) {
    	
    	UserDto user=findByEmailId(userObject.getEmailId());
        
    	if (!StringUtils.isEmpty(user)) {
    		throw new BusinessException(ErrorDescription.USER_EXIT.getMessage());
        }

        /** Password Encry */
        userObject.setPassword(stringDigester.digest(userObject.getPassword()));
        
        if(userObject.getStatus()==Status.InActive){
        	userObject.setLockDate(new Date());	
        }

        User userEntity = (User) ModelEntityMapper.converObjectToPoJo(userObject, User.class);

        userEntity = userRepo.save(userEntity);

        userObject.setUserId(userEntity.getUserId());

        deleteUserRole(userObject.getUserId());
        
        for (UserRoleDto roleUser : userObject.getUserRoles()) {

            UserRole userRoleEntity = (UserRole) ModelEntityMapper.converObjectToPoJo(roleUser, UserRole.class);

            userRoleEntity.setUser(userEntity);

            userRoleRepo.save(userRoleEntity);
        }
        return userObject;
    }
    
    public ResponseResource createUserActivationToken(String emailId) {
    	
    	User user = userRepo.findByEmailId(emailId.trim());
    	
    	if (StringUtils.isEmpty(user)) {
    		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
        }
    	
    	if(user.getStatus()==Status.Active || user.getStatus()==Status.Blocked) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_INACTIVE.getMessage());
    	}
    	
    	emailUserActivationToken(user);
    	return new ResponseResource(ErrorDescription.USER_ACTIVATE_EMAIL);
    }
    
    
    public UserDto confirmUserActivationToken(String token){
		final VerificationToken verificationToken = getVerificationToken(token);
		if (StringUtils.isEmpty(verificationToken)) {
			throw new BusinessException(ErrorDescription.INVALID_TOKEN.getMessage());
		}

		final User user = verificationToken.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			throw new BusinessException(ErrorDescription.TIME_OUT.getMessage());
		}
		
		if(user.getStatus()==Status.Active || user.getStatus()==Status.Blocked) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_INACTIVE.getMessage());
    	}

		user.setStatus(Status.Active);
		userRepo.saveAndFlush(user);
		
		UserDto userDto = (UserDto) ModelEntityMapper.converObjectToPoJo(user, UserDto.class);
		return userDto;
	}
    
    public VerificationToken getVerificationToken(String VerificationToken){
		return tokenRepository.findByToken(VerificationToken);
	}
    
    
    @Async
    public void emailUserActivationToken(User userEntity){
		String token = UUID.randomUUID().toString();
		createUserToken(userEntity,token,TokenType.AccountActivation);
		
		
		EmailDto emailRequest=new EmailDto();
		
		emailRequest.setTo(userEntity.getEmailId());
		emailRequest.setSubject("Welcome Email");
		emailRequest.setTemplateLocation("email-template");
		emailRequest.setFrom("bharathkumar.feb14@gmail.com");
		
		Map model=new HashMap();
		String appUrl = "http://localhost:8080/user/confirm-account?token=" + token;;
		model.put("api", appUrl);
		
		model.put("name", "Memorynotfound.com");
	    model.put("location", "Belgium");
	    model.put("signature", "https://memorynotfound.com");
		
		
		emailRequest.setModel(model);
		
		emailService.sendEmailMessage(emailRequest);
	}
    
    
    public void createUserToken(final User userEntity, final String token,TokenType tokenType) {
        final VerificationToken myToken = new VerificationToken(token, userEntity,tokenType);
        tokenRepository.save(myToken);
    }
    
    
	public ResponseResource changePassword(UserDto userObject) {
		userRepo.passwordUpdate(userObject.getUserId(),userObject.getPassword(),new Date());
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
	public boolean emailForgotPasswordToken(User userEntity){
		String token = UUID.randomUUID().toString();
		createUserToken(userEntity,token,TokenType.ForgotPassword);
		
		
		/*EmailDto emailRequest=new EmailDto();
		
		emailRequest.setTo(customerEntity.getEmailId());
		emailRequest.setSubject("Welcome Email");
		emailRequest.setTemplateLocation("templates/welcomeEmail.vm");
		
		Map model=new HashMap();
		String appUrl = "http://localhost:8095/customer/passwordConfirm?token=" + token;;
		model.put("api", appUrl);
		
		emailRequest.setModel(model);
	
		return emailService.constructEmailMessage(emailRequest);*/
		return true;
	}
	
    
	 public UserDto confirmUserPasswordToken(String token){
			final VerificationToken verificationToken = getVerificationToken(token);
			if (StringUtils.isEmpty(verificationToken)) {
				throw new BusinessException(ErrorDescription.INVALID_TOKEN.getMessage());
			}

			final User user = verificationToken.getUser();
			final Calendar cal = Calendar.getInstance();
			if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				throw new BusinessException(ErrorDescription.TIME_OUT.getMessage());
			}	
			
			UserDto userDto = (UserDto) ModelEntityMapper.converObjectToPoJo(user, UserDto.class);
			return userDto;
		}
    
    
    public UserDto findByEmailId(String emailId) {
    	UserDto resultObject = new UserDto();
        User entityObject = userRepo.findByEmailId(emailId.trim());
        resultObject = (UserDto) ModelEntityMapper.converObjectToPoJo(entityObject, UserDto.class);
        return resultObject;
    }
    
    @Transactional
    public boolean loginUpdate(Long userId){
    	if(userRepo.loginUpdate(userId,new Date())>0){
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

   
    
    /*@Transactional
    public List<UserDto> getUser(UserSearch search) {
        try {
            List<UserDto> userList = new ArrayList<UserDto>();
            Session session = entityManager.unwrap(Session.class);

            Criteria criteria = session.createCriteria(User.class);

            if (search.getUserId() != null && search.getUserId() > 0l) {
                criteria.add(Restrictions.eq("userId", search.getUserId()));
            }

            List<User> list = criteria.list();

            for (User entityObject : list) {
                UserDto userDto = (UserDto) ModelEntityMapper.converObjectToPoJo(entityObject, UserDto.class);
                userList.add(userDto);
            }

            return userList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public ResponseResource deleteUser(UserSearch search) {
        try {

            userRepo.delete(search.getUserId());
            return new ResponseResource(ErrorCodeDescription.TRANSACTION_SUCCESS);
        } catch (Exception e) {
            throw new RestException(ErrorCodeDescription.TRANSACTION_FAILED);
        }
    }
    
   */
    
    

}
