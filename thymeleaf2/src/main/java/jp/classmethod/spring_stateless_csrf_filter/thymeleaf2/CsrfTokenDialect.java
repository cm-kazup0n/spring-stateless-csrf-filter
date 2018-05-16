package jp.classmethod.spring_stateless_csrf_filter.thymeleaf2;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;


public class CsrfTokenDialect extends AbstractDialect {

    private final CsrfTokenFacade csrfTokenFacade;

    public CsrfTokenDialect(final CsrfTokenFacade csrfTokenFacade) {
        this.csrfTokenFacade = csrfTokenFacade;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = new HashSet<>();
        processors.add(new CsrfTokenElementProcessor(csrfTokenFacade));
        return processors;
    }

    @Override
    public String getPrefix() {
        return "cm";
    }
}
