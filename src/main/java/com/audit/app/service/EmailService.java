package com.audit.app.service;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.audit.app.dto.EmailDto;

import lombok.extern.log4j.Log4j;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendEmailMessage(final EmailDto mail) {
		try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message,
	                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
	                StandardCharsets.UTF_8.name());
	        
	        //helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));
	        
	        Context context = new Context();
	        context.setVariables(mail.getModel());
	        String html = templateEngine.process(mail.getTemplateLocation(), context);

	        helper.setTo(mail.getTo());
	        helper.setText(html, true);
	        helper.setSubject(mail.getSubject());
	        helper.setFrom(mail.getFrom());

	        mailSender.send(message);



		} catch (Exception e) {
			e.printStackTrace();
			// log.info("Mail method Exception",e);
		}

	}

}
