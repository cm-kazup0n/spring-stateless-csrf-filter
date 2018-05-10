package jp.classmethod.spring_stateless_csrf_filter.thymeleaf3;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IElementTag;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * CSRFトークン用のタグを生成するプロセッサー
 */
public class CsrfTokenElementProcessor extends AbstractElementTagProcessor {

    private final CsrfTokenFacade csrfTokenFacade;

    public CsrfTokenElementProcessor(CsrfTokenFacade csrfTokenFacade, String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, "csrf", true, null, false, 0);
        this.csrfTokenFacade = csrfTokenFacade;
    }


    private Optional<String> getCsrfToken() {
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = attributes.getRequest();
        final HttpServletResponse response = attributes.getResponse();
        return csrfTokenFacade.populateCsrfToken(request, response, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {

        final Optional<String> token = getCsrfToken();
        if (token.isPresent()) {
            IModelFactory factory = context.getModelFactory();
            Map<String, String> attributes = new HashMap<>();
            attributes.put("name", csrfTokenFacade.getCsrfTokenName());
            attributes.put("type", "hidden");
            attributes.put("value", token.get());
            final IElementTag input = factory.createStandaloneElementTag("input", attributes, AttributeValueQuotes.DOUBLE, false, true);
            structureHandler.replaceWith(factory.createModel(input), false);
        }

    }
}
