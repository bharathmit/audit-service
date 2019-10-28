package com.audit.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JWTAuthenticationEntryPoint unauthenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// we don't need CSRF because our token is invulnerable
				.csrf().disable()
                //	api authenticate with jwt token			
				.authorizeRequests().antMatchers("/api/**").authenticated()
				//permitted all other urls
				.anyRequest().permitAll()
				// don't create session
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				// Call our errorHandler if authentication/authorisation fails
				.and().exceptionHandling().authenticationEntryPoint(unauthenticationEntryPoint)
				// And filter other requests to check the presence of JWT in header
				.and().addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().frameOptions().disable();

	}

}
