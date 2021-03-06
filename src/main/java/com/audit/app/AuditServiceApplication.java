package com.audit.app;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.jasypt.digest.PooledStringDigester;
import org.jasypt.digest.StringDigester;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableSwagger2
@EncryptablePropertySource({"application.yml"})
public class AuditServiceApplication {
	
	@PostConstruct
	void started() {		
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.regex("/.*"))
				.build().securitySchemes(Arrays.asList(apiKey())).apiInfo(apiEndPointsInfo());
	}
	
	private ApiKey apiKey() {
		return new ApiKey("Bearer", "Authorization", "header");
	}
	
	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("Spring Boot Ecosystem API").description("All the Spring boot Ecosystem feature Example")
				.contact(new Contact("Bharath Mannaperumal", "www.jsoftgroup.wordpress.com", "bharathkumar.feb14@gmail.com"))
				.license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").version("1.0.0")
				.build();
	}

	@Bean
	public SpringTemplateEngine springTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		return templateEngine;
	}

	@Bean
	public SpringResourceTemplateResolver htmlTemplateResolver() {
		SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
		emailTemplateResolver.setPrefix("classpath:/templates/");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
		emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		return emailTemplateResolver;
	}

	@Bean
	StringDigester PasswordHash() {
		final PooledStringDigester stringDigester = new PooledStringDigester();
		stringDigester.setAlgorithm("SHA-256");
		stringDigester.setIterations(100000);
		stringDigester.setSaltSizeBytes(10);
		stringDigester.setPoolSize(4);
		stringDigester.initialize();
		return stringDigester;
	}

	public static void main(String[] args) {
		SpringApplication.run(AuditServiceApplication.class, args);		
	}

}
