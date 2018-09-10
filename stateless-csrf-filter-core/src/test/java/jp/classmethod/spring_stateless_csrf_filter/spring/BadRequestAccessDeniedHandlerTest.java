package jp.classmethod.spring_stateless_csrf_filter.spring;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;

public class BadRequestAccessDeniedHandlerTest {

    @Test
    public void test_handleRequest(){
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        new BadRequestAccessDeniedHandler().handleRequest(request, response);
        assertEquals(400, response.getStatus());
    }

}