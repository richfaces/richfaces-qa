package org.richfaces.tests.metamer.bean.issues.rf12291;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractAction.class);

    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {

        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getPartialViewContext().isAjaxRequest()) {
            // Iterate over all unhandled exceptions
            Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();

            while (it.hasNext()) {// w

                Throwable t = it.next().getContext().getException();

                long ts = System.currentTimeMillis();
                logger.error("Exception in CustomExceptionHandler was handled [" + ts + "]", t);

                FacesContext ctx = FacesContext.getCurrentInstance();
                context.setViewRoot(context.getApplication().getViewHandler().createView(context, "/module/core/error.xhtml"));
                context.getPartialViewContext().setRenderAll(true);

                ctx.renderResponse();
                it.remove();
            }// w
        }

        getWrapped().handle();

    }

}
