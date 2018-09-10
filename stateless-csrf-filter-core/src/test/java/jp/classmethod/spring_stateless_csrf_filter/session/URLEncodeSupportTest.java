package jp.classmethod.spring_stateless_csrf_filter.session;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class URLEncodeSupportTest {

    @Test
    public void encode_then_decode() {
        final String expected = "アイウエオ";

        final String encoded = URLEncodeSupport.encode(expected);

        assertEquals("%E3%82%A2%E3%82%A4%E3%82%A6%E3%82%A8%E3%82%AA", encoded);

        assertEquals(expected, URLEncodeSupport.decode(encoded));
    }

}