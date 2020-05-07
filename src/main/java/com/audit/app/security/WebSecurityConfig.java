package com.audit.app.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.audit.app.security.oauth2.CustomOAuth2UserService;
import com.audit.app.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.audit.app.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.audit.app.security.oauth2.OAuth2AuthenticationSuccessHandler;




@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JWTAuthenticationEntryPoint unauthenticationEntryPoint;
	
	@Autowired
	CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	
	/*
    By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
    the authorization request. But, since our service is stateless, we can't save it in
    the session. We'll save the request in a Base64 encoded cookie instead.
	*/
	@Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors().and()
				// don't create session
			    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			    // we don't need CSRF because our token is invulnerable
				.csrf().disable()
			    .formLogin().disable()
			    .httpBasic().disable()
			    .headers().frameOptions().disable()
			    // Call our errorHandler if authentication/authorisation fails
                .and().exceptionHandling().authenticationEntryPoint(unauthenticationEntryPoint)
			    // permitted all other urls
			    .and().authorizeRequests()
                .antMatchers("/",
                    "/error",
                    "/favicon.ico",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/*.svg",
                    "/**/*.jpg",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js")
                    .permitAll()
                .antMatchers("/api/auth/**","/api/account/**", "/api/oauth2/**")
                    .permitAll()
                 // api authenticate with jwt token
                .antMatchers("/api/**").authenticated()    			  
                //OAuth2 Social Login
				.and().oauth2Login()
		        .authorizationEndpoint()
		            .baseUri("/token/authorize")
		            .authorizationRequestRepository(cookieAuthorizationRequestRepository)
		            .and()
		        .redirectionEndpoint()
		            .baseUri("/oauth2/callback/*")
		            .and()
		        .userInfoEndpoint()
		            .userService(customOAuth2UserService)
		            .and()
		        .successHandler(oAuth2AuthenticationSuccessHandler)
		        .failureHandler(oAuth2AuthenticationFailureHandler);
		
				
		// And filter other requests to check the presence of JWT in header
		http.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
