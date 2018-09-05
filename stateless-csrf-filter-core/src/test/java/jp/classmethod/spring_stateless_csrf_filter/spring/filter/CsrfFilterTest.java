package jp.classmethod.spring_stateless_csrf_filter.spring.filter;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.session.Session;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CsrfFilterTest {

    private CsrfFilter csrfFilter;

    @Mock
    private RequireCsrfProtectionRequestMatcher matcher;

    @Mock
    private CsrfTokenFacade csrfTokenFacade;

    @Mock
    private AccessDeniedHandler accessDeniedHandler;

    @Mock
    private FilterChain filterChain;

    @Before
    public void setUp() {
        csrfFilter = new CsrfFilter(matcher, csrfTokenFacade, accessDeniedHandler);
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
        verify(csrfTokenFacade, never()).validate(any());
        verify(csrfTokenFacade).populateCsrfToken(request, response, true);
    }

    @Test
    public void accessDeniedHandlerIsCalledIfTokenIsInvalid_noTokenInRequest() throws ServletException, IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();

        // request matches
        when(matcher.matches(request)).thenReturn(true);

        // token is invalid
        when(csrfTokenFacade.validate(request)).thenReturn(Optional.of(false));

        //no token in request
        csrfFilter.doFilterInternal(request, response, filterChain);

        //verify
        verify(filterChain, never()).doFilter(request, response);
        verify(csrfTokenFacade).validate(any());
        verify(csrfTokenFacade).populateCsrfToken(request, response, true);
    }

    @Test
    public void accessDeniedHandlerIsCalledIfTokenIsInvalid_NoTokenInSession() throws ServletException, IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();

        // request matches
        when(matcher.matches(request)).thenReturn(true);

        // no session
        when(csrfTokenFacade.validate(request)).thenReturn(Optional.empty());

        //token in request
        csrfFilter.doFilterInternal(request, response, filterChain);

        //verify
        verify(filterChain, never()).doFilter(request, response);
        verify(csrfTokenFacade).populateCsrfToken(request, response, true);
    }


}