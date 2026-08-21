package com.example.P09_FilterInterceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String MVC_START_TIME = "mvcStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long mvcStartTime = System.currentTimeMillis();
        request.setAttribute(MVC_START_TIME, mvcStartTime);

        log.info("INTERCEPTOR preHandle");
        log.info("INTERCEPTOR handler={}", describeHandler(handler));

        if ("true".equalsIgnoreCase(request.getHeader("X-Block"))) {
            log.info("INTERCEPTOR preHandle -> X-Block=true, return false, controller will not run");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("""
                    {"status":403,"message":"Blocked by LoggingInterceptor because X-Block=true"}
                    """);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) {
        log.info("INTERCEPTOR postHandle");
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception
    ) {
        Object startTime = request.getAttribute(MVC_START_TIME);
        long duration = startTime instanceof Long value
                ? System.currentTimeMillis() - value
                : -1;

        log.info("INTERCEPTOR afterCompletion");
        log.info("INTERCEPTOR mvcDurationMs={}", duration);

        if (exception != null) {
            log.info("INTERCEPTOR exception={}", exception.getClass().getSimpleName());
        }
    }

    private String describeHandler(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return handlerMethod.getBeanType().getSimpleName() + "#" + handlerMethod.getMethod().getName();
        }
        return handler.toString();
    }
}
