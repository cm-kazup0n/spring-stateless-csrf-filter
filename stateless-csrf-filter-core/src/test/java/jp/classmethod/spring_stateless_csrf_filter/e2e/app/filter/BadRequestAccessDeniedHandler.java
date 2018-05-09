package jp.classmethod.spring_stateless_csrf_filter.e2e.app.filter;

import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BadRequestAccessDeniedHandler implements AccessDeniedHandler{
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }
}
