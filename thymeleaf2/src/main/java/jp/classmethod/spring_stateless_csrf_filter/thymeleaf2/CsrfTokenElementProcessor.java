package jp.classmethod.spring_stateless_csrf_filter.thymeleaf2;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * CSRFトークン用のタグを生成するプロセッサー
 */
public class CsrfTokenElementProcessor extends AbstractElementProcessor {

    private final CsrfTokenFacade csrfTokenFacade;

    protected CsrfTokenElementProcessor(CsrfTokenFacade csrfTokenFacade) {
        super("csrf");
        this.csrfTokenFacade = csrfTokenFacade;
    }


    private Optional<String> getCsrfToken() {
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = attributes.getRequest();
        final HttpServletResponse response = attributes.getResponse();
        return csrfTokenFacade.populateCsrfToken(request, response, true);
    }


    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element) {
        return getCsrfToken().map(token -> {

            final Element hidden = new Element("input");
            hidden.setAttribute("type", "hidden");
            hidden.setAttribute("name", csrfTokenFacade.getCsrfTokenName());
            hidden.setAttribute("value", token);
            //マーカーとinputを入れ替える
            element.getParent().addChild(hidden);
            //elementはマーカーなので削除する
            element.getParent().removeChild(element);

            return ProcessorResult.ok();
        }).orElseThrow(() -> new CsrfTokenElementProcessorException("Cannot append hidden CSRF token tag."));
    }

    @Override
    public int getPrecedence() {
        return 0;
    }
}
