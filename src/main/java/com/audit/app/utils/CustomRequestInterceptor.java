package com.ford.fcsd.gesb.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;


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
