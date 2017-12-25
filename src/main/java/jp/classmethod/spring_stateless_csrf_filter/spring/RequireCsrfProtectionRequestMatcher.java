package jp.classmethod.spring_stateless_csrf_filter.spring;

import javax.servlet.http.HttpServletRequest;

public interface RequireCsrfProtectionRequestMatcher {

   boolean matches(HttpServletRequest request);

}
