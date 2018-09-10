package jp.classmethod.spring_stateless_csrf_filter.spring;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BAD_REQUEST(400)で応答するハンドラ
 */
public class BadRequestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }
}
