package jp.classmethod.spring_stateless_csrf_filter.thymeleaf2;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SuppressWarnings("Duplicates")
public class CsrfTokenElementProcessorTest {

    @Mock
    private CsrfTokenFacade csrfTokenFacade;

    private TestExecutor executor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //mock facade
        final Optional<String> token = Optional.of("DUMMY_TOKEN_VALUE");
        when(csrfTokenFacade.getCsrfTokenName()).thenReturn("CSRF_TOKEN");
        when(csrfTokenFacade.populateCsrfToken(any(), any(), eq(true))).thenReturn(token);

        //setup TestExecutor
        executor = new TestExecutor();
        executor.setDialects(Arrays.asList(new CsrfTokenDialect(csrfTokenFacade)));

        //setup mock req/res in RequestContextHolder
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest(), new MockHttpServletResponse()));
    }


    @Test
    public void doProcessAppendInputTag() {
        executor.execute("classpath:thtest/csrf.thtest");
        Assert.assertTrue(executor.isAllOK());
    }

}