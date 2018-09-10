package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.spring.BeanBasedCookieBaker;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import javax.servlet.http.Cookie;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CookieSessionProviderTest {

    private CookieSessionProvider cookieSessionProvider;
    private SessionCookieBaker baker;

    private TokenSigner signer;
    private MockHttpServletRequest request;


    private final String cookieName = "__CSRF";

    @Before
    public void setUp(){
        signer = new TokenSigner("secret");
        baker = new BeanBasedCookieBaker(cookieName, true);

        cookieSessionProvider = new CookieSessionProvider(baker, signer);

        request = new MockHttpServletRequest();
    }

    @Test
    public void get_既存のセッションあり(){
        //セッションを作ってクッキーとしてセットアップしておく
        final String sessionCookie = CookieSession.SerDe.serialize(signer, (CookieSession) CookieSession.create().put("hoge", "foo"));
        request.setCookies(new Cookie(cookieName, sessionCookie));

        //既存のセッションがあればそれが返される
       final Session session = cookieSessionProvider.get(request, false).get();
       assertEquals("foo", session.get("hoge").get());
    }

    @Test
    public void get_既存のセッションない場合(){
        assertFalse(cookieSessionProvider.get(request, false).isPresent());
    }

    @Test
    public void get_既存のセッションない場合_forceCreate(){
        assertTrue(cookieSessionProvider.get(request, true).isPresent());
    }


    @Test
    public void flush(){
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Session session = CookieSession.create().put("foo", "bar");
        cookieSessionProvider.flush(response, session);
        //レスポンスにcookieがセットされる
        final String cookie = response.getCookie(cookieName).getValue();

        CookieSession saved = CookieSession.SerDe.deserialize(signer, cookie);

        assertEquals("bar", saved.get("foo").get());
    }
}