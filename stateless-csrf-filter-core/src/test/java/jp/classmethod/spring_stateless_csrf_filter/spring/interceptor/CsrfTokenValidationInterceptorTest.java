package jp.classmethod.spring_stateless_csrf_filter.spring.interceptor;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CsrfTokenValidationInterceptorTest {

    private CsrfTokenValidationInterceptor interceptor;

    @Mock
    private CsrfTokenFacade csrfTokenFacade;
    @Mock
    private AccessDeniedHandler accessDeniedHandler;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private final Controller controller = new Controller();

    @Before
    public void setUp(){
        interceptor = new CsrfTokenValidationInterceptor(csrfTokenFacade, accessDeniedHandler);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void preHandle_検証が成功する() throws Exception {
        //バリデーション成功
        when(csrfTokenFacade.validate(request)).thenReturn(Optional.of(true));

        interceptor.preHandle(request, response, controller.getMethod("shouldInspect"));

        verify(csrfTokenFacade).validate(request);
        verify(csrfTokenFacade).populateCsrfToken(request, response, true);

        //ハンドラが実行される
        verify(accessDeniedHandler, never()).handleRequest(request, response);
    }

    @Test
    public void preHandle_検証が失敗する() throws Exception {
        //バリデーション成功
        when(csrfTokenFacade.validate(request)).thenReturn(Optional.of(false));

        interceptor.preHandle(request, response, controller.getMethod("shouldInspect"));

        verify(csrfTokenFacade).validate(request);
        verify(csrfTokenFacade).populateCsrfToken(request, response, true);

        //ハンドラが実行されない
        verify(accessDeniedHandler).handleRequest(request, response);
    }

    @Test
    public void preHandle_セッションが存在しない() throws Exception {
        //バリデーション成功
        when(csrfTokenFacade.validate(request)).thenReturn(Optional.empty());

        interceptor.preHandle(request, response, controller.getMethod("shouldInspect"));

        verify(csrfTokenFacade).validate(request);
        verify(csrfTokenFacade).populateCsrfToken(request, response, true);

        //ハンドラが実行される
        verify(accessDeniedHandler).handleRequest(request, response);
    }


    @Test
    public void preHandle_検証が行われない() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, controller.getMethod("shoudNotInspect"));

        verify(csrfTokenFacade, never()).validate(request);
        verify(csrfTokenFacade, never()).populateCsrfToken(request, response, true);
        verify(accessDeniedHandler, never()).handleRequest(request, response);
    }

    @Test
    public void requestNeedsInspect() throws NoSuchMethodException {
        assertEquals(true,
                interceptor.requestNeedsInspect(controller.getMethod("shouldInspect"))
        );

        assertEquals(false,
                interceptor.requestNeedsInspect(controller.getMethod("shoudNotInspect"))
        );

    }


    static class Controller {

        @ProtectedByCsrfFilter
        public void shouldInspect(){}

        public void shoudNotInspect(){}

        HandlerMethod getMethod(String name) throws NoSuchMethodException {
            return new HandlerMethod(this, Controller.class.getMethod(name));
        }

    }

}