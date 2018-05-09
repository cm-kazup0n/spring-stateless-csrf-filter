package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.junit.Test;

import static org.junit.Assert.*;

public class CookieSessionTest {

    @Test
    public void testNoneOrEmptyOne() {
        assertTrue(CookieSession.noneOrEmptyOne(true).isPresent());
        assertFalse(CookieSession.noneOrEmptyOne(false).isPresent());
    }

    @Test
    public void testPutAndGet() {
        CookieSession session = (CookieSession) CookieSession.noneOrEmptyOne(true).get().put("key", "value");
        assertTrue(session.get("key").isPresent());
        assertFalse(session.get("nokey").isPresent());
    }

    @Test
    public void testSerDe() {
        final TokenSigner signer = new TokenSigner("SECRET");
        final CookieSession session =
                (CookieSession) CookieSession.noneOrEmptyOne(true).get()
                        .put("key-1", "value")
                        .put("key-2", "value")
                        .put("key-3", "value");

        final String serialized = CookieSession.SerDe.serialize(signer, session);

        final CookieSession restored = CookieSession.SerDe.deserialize(signer, serialized);

        assertEquals(session.get("key-1").get(), restored.get("key-1").get());
        assertEquals(session.get("key-2").get(), restored.get("key-1").get());
        assertEquals(session.get("key-3").get(), restored.get("key-1").get());
    }


}