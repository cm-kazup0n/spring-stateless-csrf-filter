package jp.classmethod.spring_stateless_csrf_filter.e2e.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CsrfConfig {

    @Value("${csrf.secret}")
    private String secret;

    @Value("${csrf.cookieName}")
    private String cookieName;

    public String getSecret(){
        return this.secret;
    }

    public String getCookieName(){
        return this.cookieName;
    }


}
