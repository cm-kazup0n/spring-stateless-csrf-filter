package jp.classmethod.spring_stateless_csrf_filter.spring;

import jp.classmethod.spring_stateless_csrf_filter.session.CookieSession;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionCookieBaker;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BeanBasedCookieBakerTest {

    private TokenSigner tokenSigner;

    @Before
    public void setUp() {
        tokenSigner = new TokenSigner("secret");
    }


    @Test
    public void test_AddCookie() {
        final SessionCookieBaker baker = new BeanBasedCookieBaker("CSRF__", true);
        final CookieSession cookieSession = (CookieSession) CookieSession.create().put("CSRF__", "token");


        final MockHttpServletResponse response = new MockHttpServletResponse();

        baker.addCookie(response, tokenSigner, cookieSession);

        final List<Cookie> cookies = Arrays.asList(response.getCookies());
        final Cookie sessionCookie = cookies.get(0);
        assertEquals("CSRF__", sessionCookie.getName());
        assertEquals("CSRF__=token",
                sessionCookie.getValue().split("-")[2]);

    }

}