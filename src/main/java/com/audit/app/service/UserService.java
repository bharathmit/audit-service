
package com.audit.app.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.jasypt.digest.StringDigester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.audit.app.constants.Status;
import com.audit.app.dto.UserDto;
import com.audit.app.dto.UserRoleDto;
import com.audit.app.entity.User;
import com.audit.app.entity.UserRole;
import com.audit.app.exception.BusinessException;
import com.audit.app.exception.response.ErrorDescription;
import com.audit.app.repo.UserJPARepo;
import com.audit.app.repo.UserRoleJPARepo;
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
    
    
    @Transactional
    public UserDto saveUser(UserDto userObject) {
    	
    	UserDto user=findUser(userObject.getEmailId(),userObject.getMobile());
        
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
    

    public UserDto findUser(String emailId,String mobile) {
        UserDto resultObject = new UserDto();
        User entityObject = userRepo.findByEmailIdOrMobile(emailId.trim(),mobile.trim());
        resultObject = (UserDto) ModelEntityMapper.converObjectToPoJo(entityObject, UserDto.class);
        return resultObject;
    }
    
    public UserDto createUserActivationToken(String emailId) {
    	
    	UserDto user=exitEmail(emailId);
    	
    	if (StringUtils.isEmpty(user)) {
    		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
        }
    	
    	if(user.getStatus()==Status.Active)) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_ACTIVE.getMessage());
    	}
    	
    	
    	emailUserActivationToken(userEntity);
    	return user;
    }
    
    
    public UserDto confirmUserActivationToken(String token){
		final VerificationToken verificationToken = getVerificationToken(token);
		if (verificationToken == null) {
			return new BusinessException(ErrorDescription.INVALID_TOKEN.getMessage());
		}

		final Customer customer = verificationToken.getCustomer();
		final Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new BusinessException(ErrorDescription.TIME_OUT.getMessage());
		}
		
		if(user.getStatus()==Status.Active)) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_ACTIVE.getMessage());
    	}
		
		customer.setStatus(Status.Active);
		customerRepo.saveAndFlush(customer);
		return new ResponseResource(ErrorCodeDescription.TRANSACTION_SUCCESS);
	}
    
    public VerificationToken getVerificationToken(String VerificationToken){
		return tokenRepository.findByToken(VerificationToken);
	}
    
    
    @Async
    public boolean emailUserActivationToken(User userEntity){
		String token = UUID.randomUUID().toString();
		createUserActivationToken(userEntity,token);
		
		
		EmailDto emailRequest=new EmailDto();
		
		emailRequest.setTo(customerEntity.getEmailId());
		emailRequest.setSubject("Welcome Email");
		emailRequest.setTemplateLocation("templates/welcomeEmail.vm");
		
		Map model=new HashMap();
		String appUrl = "http://localhost:8095/customer/regitrationConfirm?token=" + token;;
		model.put("api", appUrl);
		
		emailRequest.setModel(model);
		
		return emailService.constructEmailMessage(emailRequest);
	}
    
    
    public void createUserActivationToken(final User userEntity, final String token) {
        final VerificationToken myToken = new VerificationToken(token, userEntity,TokenType.AccountActivation);
        tokenRepository.save(myToken);
    }
    
    
	public CustomerDto changePassword(CustomerDto customerObject) {
		Customer customerEntity = (Customer) ModelEntityMapper.converObjectToPoJo(customerObject, Customer.class);
		customerRepo.save(customerEntity);
		return customerObject;
	}
	
	public UserDto createUserPasswordToken(String emailId) {
    	
    	UserDto user=exitEmail(emailId);
    	
    	if (StringUtils.isEmpty(user)) {
    		throw new BusinessException(ErrorDescription.USER_NOT_EXIT.getMessage());
        }
    	
    	if(user.getStatus()==Status.Blocked)) {
    		throw new BusinessException(ErrorDescription.USER_ACCOUNT_BLOCKED.getMessage());
    	}
    	
    	
    	emailForgotPasswordToken(userEntity);
    	return user;
    }
	
	@Async
	public boolean emailForgotPasswordToken(Customer customerEntity){
		String token = UUID.randomUUID().toString();
		createForgotPasswordTokenForCustomer(customerEntity,token);
		
		
		EmailDto emailRequest=new EmailDto();
		
		emailRequest.setTo(customerEntity.getEmailId());
		emailRequest.setSubject("Welcome Email");
		emailRequest.setTemplateLocation("templates/welcomeEmail.vm");
		
		Map model=new HashMap();
		String appUrl = "http://localhost:8095/customer/passwordConfirm?token=" + token;;
		model.put("api", appUrl);
		
		emailRequest.setModel(model);
		
		
		
		return emailService.constructEmailMessage(emailRequest);
	}
	
    
	 public UserDto confirmUserPasswordToken(String token){
			final VerificationToken verificationToken = getVerificationToken(token);
			if (verificationToken == null) {
				return new BusinessException(ErrorDescription.INVALID_TOKEN.getMessage());
			}

			final Customer customer = verificationToken.getCustomer();
			final Calendar cal = Calendar.getInstance();
			if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				return new BusinessException(ErrorDescription.TIME_OUT.getMessage());
			}			
			return new ResponseResource(ErrorCodeDescription.TRANSACTION_SUCCESS);
		}
    
    
    public UserDto exitEmail(String emailId) {
    	UserDto resultObject = new UserDto();
        User entityObject = userRepo.findByEmailId(emailId.trim());
        resultObject = (UserDto) ModelEntityMapper.converObjectToPoJo(entityObject, UserDto.class);
        return resultObject;
    }

    //not used
    public UserDto exitMobile(String mobileNumber) {
    	UserDto resultObject = new UserDto();
        User entityObject = userRepo.findByMobile(mobileNumber.trim());
        resultObject = (UserDto) ModelEntityMapper.converObjectToPoJo(entityObject, UserDto.class);
        return resultObject;
    }
    
    //not used
    @Transactional
    public boolean loginUpdate(UserDto user){
    	if(userRepo.loginUpdate(user.getUserId(),new Date())>0){
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
    
    public ResponseResource changePassword(UserDto userDto){
		try {

			UserDto user = findEmailId(userDto.getEmailId());
			log.info("Check the Password");
			if(!stringDigester.matches(userDto.getPassword(),user.getPassword())){
				return new ResponseResource(ErrorCodeDescription.INVALID_PASSWORD);
			}
		
			
			if(userRepo.passwordUpdate(userDto.getUserId(),stringDigester.digest(userDto.getConfirmPassword()))>0){
				return new ResponseResource(ErrorCodeDescription.TRANSACTION_SUCCESS);
			}
            
        } catch (Exception e) {
            throw new RestException(ErrorCodeDescription.TRANSACTION_FAILED);
        }
		return new ResponseResource(ErrorCodeDescription.TRANSACTION_FAILED);
	}*/
    
    

}
