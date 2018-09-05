package jp.classmethod.spring_stateless_csrf_filter.spring.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * このアノテーションを付与したcontrollerのメソッドはCSRFトークンによる検証が行われる
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ProtectedByCsrfFilter {
}