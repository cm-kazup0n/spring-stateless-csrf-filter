package jp.classmethod.spring_stateless_csrf_filter.thymeleaf2;

import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class CsrfTokenDialectTest {

    private CsrfTokenDialect dialect;

    @Before
    public void setUp() {
        dialect = new CsrfTokenDialect(null);
    }


    @Test
    public void returnsCsrfTokenElementProcessor() {
        final Set<IProcessor> actual = dialect.getProcessors();
        assertThat(actual.toArray(), is(arrayWithSize(1)));
        assertThat(actual.toArray(), is(array(instanceOf(CsrfTokenElementProcessor.class))));
    }
}