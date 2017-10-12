package jp.classmethod.spring_stateless_csrf_filter.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccessDeniedHandler {

    void handleRequest(HttpServletRequest request, HttpServletResponse response);
}
