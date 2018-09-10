package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CsrfTokenFacadeTest {

    private CsrfTokenFacade csrfTokenFacade;

    @Mock
    private SessionProvider sessionProvider;
    private TokenSigner tokenSigner;

    private final String csrfTokenName = "__CSRF";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        tokenSigner = new TokenSigner("key");
        csrfTokenFacade = new CsrfTokenFacade(sessionProvider, tokenSigner, csrfTokenName);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void getCsrfTokenName() {
        assertEquals("__CSRF", csrfTokenFacade.getCsrfTokenName());
    }

    @Test
    public void populateCsrfToken_既存のセッションがない場合_なければ作成する() {
        final boolean create = true;
        when(sessionProvider.get(request, create)).thenReturn(Optional.of(CookieSession.create()));

        final String token = csrfTokenFacade.populateCsrfToken(request, response, create).get();
        assertEquals(79, token.length());
        verify(sessionProvider).flush(eq(response), any());
    }

    @Test
    public void populateCsrfToken_既存のセッションがない場合() {
        final boolean create = false;
        when(sessionProvider.get(request, create)).thenReturn(Optional.empty());

        assertEquals(false, csrfTokenFacade.populateCsrfToken(request, response, create).isPresent());
        verify(sessionProvider, never()).flush(eq(response), any());
    }


    @Test
    public void populateCsrfToken_既存のセッションがある場合(){
        final boolean create = false;
        when(sessionProvider.get(request, create)).thenReturn(Optional.of(CookieSession.create()));

        final String token = csrfTokenFacade.populateCsrfToken(request, response, create).get();
        assertEquals(79, token.length());
        verify(sessionProvider).flush(eq(response), any());
    }


    @Test
    public void validate() {
        final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
        request.setParameter(csrfTokenName, token);

        final Session session = CookieSession.create().put(csrfTokenName, token);
        when(sessionProvider.get(request, false)).thenReturn(Optional.of(session));


        final Optional<Boolean> result = csrfTokenFacade.validate(request);
        assertEquals(true, result.get());
    }

    @Test
    public void validate_リクエストパラメータがセットされていない(){
         final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());

        final Session session = CookieSession.create().put(csrfTokenName, token);
        when(sessionProvider.get(request, false)).thenReturn(Optional.of(session));


        final Optional<Boolean> result = csrfTokenFacade.validate(request);
        assertFalse(result.isPresent());
    }

    @Test
    public void validate_リクエストパラメータが不正(){
        final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
        request.setParameter(csrfTokenName, token + "______"); //不正な値

        final Session session = CookieSession.create().put(csrfTokenName, token);
        when(sessionProvider.get(request, false)).thenReturn(Optional.of(session));

        final Optional<Boolean> result = csrfTokenFacade.validate(request);
        assertFalse(result.isPresent());
    }


    @Test
    public void validate_sessionがない場合() {
        final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
        request.setParameter(csrfTokenName, token);

        when(sessionProvider.get(request, false)).thenReturn(Optional.empty());


        final Optional<Boolean> result = csrfTokenFacade.validate(request);
        assertFalse(result.isPresent());
    }


    @Test
    public void validate_sessionがあるがトークンがない場合() {
        final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
        request.setParameter(csrfTokenName, token);

        final Session session = CookieSession.create();
        when(sessionProvider.get(request, false)).thenReturn(Optional.of(session));


        final Optional<Boolean> result = csrfTokenFacade.validate(request);
        assertFalse(result.isPresent());
    }


    @Test
    public void validate_sessionにトークンがあるが無効な場合() {
        final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
        request.setParameter(csrfTokenName, token);

        final Session session = CookieSession.create().put(csrfTokenName, token + "abc");
        when(sessionProvider.get(request, false)).thenReturn(Optional.of(session));


        final Optional<Boolean> result = csrfTokenFacade.validate(request);
        assertFalse(result.isPresent());
    }

}