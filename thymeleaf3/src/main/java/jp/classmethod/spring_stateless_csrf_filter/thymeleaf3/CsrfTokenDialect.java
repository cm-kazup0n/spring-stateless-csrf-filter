package jp.classmethod.spring_stateless_csrf_filter.thymeleaf3;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

public class CsrfTokenDialect extends AbstractProcessorDialect {

    private final static String PREFIX = "cm";

    private final CsrfTokenFacade csrfTokenFacade;


    public CsrfTokenDialect(CsrfTokenFacade csrfTokenFacade){
        super("csrf", "cm", 0);
        this.csrfTokenFacade = csrfTokenFacade;
    }


    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        if(getPrefix().matches(dialectPrefix)){
            final CsrfTokenElementProcessor processor = new CsrfTokenElementProcessor(csrfTokenFacade, getPrefix());
            processors.add(processor);
        }
        return  processors;
    }
}
