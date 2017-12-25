package jp.classmethod.spring_stateless_csrf_filter.spring;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;

public class DefaultRequestMatacherTest {

    @Test
    public void testMethodMatches(){
        final RequireCsrfProtectionRequestMatcher matcher = DefaultRequestMatcher.Builder.create("/*");
        //POST, PUT, DELETE, PATCH are to inspect.
        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/")));
        assertTrue(matcher.matches(new MockHttpServletRequest("PUT", "/")));
        assertTrue(matcher.matches(new MockHttpServletRequest("DELETE", "/")));
        assertTrue(matcher.matches(new MockHttpServletRequest("PATCH", "/")));

        //GET,HEAD,TRACE,OPTIONS are excluded.
        assertFalse(matcher.matches(new MockHttpServletRequest("GET", "/")));
        assertFalse(matcher.matches(new MockHttpServletRequest("HEAD", "/")));
        assertFalse(matcher.matches(new MockHttpServletRequest("OPTIONS", "/")));
        assertFalse(matcher.matches(new MockHttpServletRequest("TRACE", "/")));
    }

    @Test
    public void testPatternMatches(){
        final RequireCsrfProtectionRequestMatcher matcher = DefaultRequestMatcher.Builder.create(
                "/person/*/items",
                "/cat/",
                "/dog/*",
                "/lion/**"
        );
        assertFalse(matcher.matches(new MockHttpServletRequest("POST", "/")));
        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/person/1234/items")));
        assertFalse(matcher.matches(new MockHttpServletRequest("POST", "/person/items")));


        assertFalse(matcher.matches(new MockHttpServletRequest("POST", "/cat")));
        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/cat/")));

        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/dog/")));
        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/dog/aaaa")));
        assertFalse(matcher.matches(new MockHttpServletRequest("POST", "/dog/aaa/foo")));

        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/lion/")));
        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/lion/aaaa")));
        assertTrue(matcher.matches(new MockHttpServletRequest("POST", "/lion/aaa/foo")));
    }



}