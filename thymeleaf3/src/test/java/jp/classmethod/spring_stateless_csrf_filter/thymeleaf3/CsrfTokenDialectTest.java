package jp.classmethod.spring_stateless_csrf_filter.thymeleaf3;

import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

public class CsrfTokenDialectTest {


    private CsrfTokenDialect dialect;

    @Before
    public void setUp(){
        dialect = new CsrfTokenDialect(null);
    }


    @Test
    public void returnsCsrfTokenElementProcessor() {
        final Set<IProcessor> actual = dialect.getProcessors("cm");
        assertThat(actual.toArray(), is(arrayWithSize(1)));
        assertThat(actual.toArray(), is(array(instanceOf(CsrfTokenElementProcessor.class))));
    }

    @Test
    public void returnsWithNoProcessor() {
        final Set<IProcessor> actual = dialect.getProcessors("hoge");
        assertThat(actual, is(empty()));
    }

}