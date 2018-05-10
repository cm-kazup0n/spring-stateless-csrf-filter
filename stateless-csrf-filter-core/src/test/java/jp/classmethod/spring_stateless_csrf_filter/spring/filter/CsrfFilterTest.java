package jp.classmethod.spring_stateless_csrf_filter.spring.filter;

import jp.classmethod.spring_stateless_csrf_filter.session.Session;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CsrfFilterTest {

    private CsrfFilter csrfFilter;
    private RequireCsrfProtectionRequestMatcher matcher;
    private SessionProvider sessionProvider;
    private Session session;
    private TokenSigner tokenSigner;
    private AccessDeniedHandler accessDeniedHandler;
    private FilterChain filterChain;
    private Token token = Token.Builder.generate();
    private String rawToken;

    @Before
    public void setUp() {

        matcher = mock(RequireCsrfProtectionRequestMatcher.class);
        sessionProvider = mock(SessionProvider.class);
        tokenSigner = new TokenSigner("SECRET");
        accessDeniedHandler = mock(AccessDeniedHandler.class);
        filterChain = mock(FilterChain.class);

        session = mock(Session.class);
        when(session.put(anyString(), anyString())).thenReturn(session);

        when(sessionProvider.get(any(HttpServletRequest.class), anyBoolean())).thenReturn(Optional.of(session));


        csrfFilter = new CsrfFilter(matcher, sessionProvider, tokenSigner, accessDeniedHandler);


        rawToken = Token.SerDe.signAndEncode(tokenSigner, token);
    }

    @Test
    public void nonMatchedRequest() throws ServletException, IOException {
        //subsequent filter will be called if request is not matched.
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();
        //request does not matches
        when(matcher.matches(request)).thenReturn(false);

        //exercise
        csrfFilter.doFilterInternal(request, response, filterChain);

        //verify
        verify(filterChain).doFilter(request, response);
        verify(sessionProvider, never()).flush(response, session);
    }

    @Test
    public void accessDeniedHandlerIsCalledIfTokenIsInvalid_noTokenInRequest() throws ServletException, IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();

        // request matches
        when(matcher.matches(request)).thenReturn(true);

        // token in session
        when(session.get(anyString())).thenReturn(Optional.of(rawToken));
        //no token in request
        csrfFilter.doFilterInternal(request, response, filterChain);

        //verify
        verify(filterChain, never()).doFilter(request, response);
        verify(sessionProvider).flush(response, session);
    }

    @Test
    public void accessDeniedHandlerIsCalledIfTokenIsInvalid_NoTokenInSession() throws ServletException, IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();

        // request matches
        when(matcher.matches(request)).thenReturn(true);

        // token in session
        when(session.get(anyString())).thenReturn(Optional.empty());

        ((MockHttpServletRequest) request).setParameter(CsrfFilter.CSRF_TOKEN_NAME, rawToken);

        //token in request
        csrfFilter.doFilterInternal(request, response, filterChain);

        //verify
        verify(filterChain, never()).doFilter(request, response);
        verify(sessionProvider).flush(response, session);
    }


}