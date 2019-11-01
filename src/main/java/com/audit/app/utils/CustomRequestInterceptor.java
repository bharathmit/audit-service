package com.audit.app.utils;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


@Component
public class CustomRequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CustomRequestInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		long startTime = Instant.now().toEpochMilli();
		request.setAttribute("startTime", startTime);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		long startTime = (Long) request.getAttribute("startTime");
		
		String uRL=request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
		
		logger.info("URL=" + uRL + ", Method="+request.getMethod()+", Status="+response.getStatus()+", Duration="+ (Instant.now().toEpochMilli() - startTime));
	}
}
