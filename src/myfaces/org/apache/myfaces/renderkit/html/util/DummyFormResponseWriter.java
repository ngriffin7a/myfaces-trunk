package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.renderkit.html.HTML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DummyFormResponseWriter
        extends ResponseWriterWrapper
{
    private static final Log log = LogFactory.getLog(DummyFormResponseWriter.class);

    public static final String DUMMY_FORM_NAME = "linkDummyForm";
    private static final String DUMMY_FORM_ID = "linkDummyForm";
    private static final String DUMMY_RESPONSE_WRITER_REQ_PARAM
            = DummyFormResponseWriter.class.getName() + ".INSTANCE";

    private Set _hiddenParams = new HashSet();

    public DummyFormResponseWriter(ResponseWriter responseWriter)
    {
        super(responseWriter);
    }

    private DummyFormResponseWriter(ResponseWriter responseWriter,
                                    Set hiddenParams)
    {
        super(responseWriter);
        _hiddenParams = hiddenParams;
    }

    public void addHiddenParam(String paramName)
    {
        _hiddenParams.add(paramName);
    }

    public ResponseWriter cloneWithWriter(Writer writer)
    {
        return new DummyFormResponseWriter(_responseWriter.cloneWithWriter(writer),
                                           _hiddenParams);
    }

    public void endDocument() throws IOException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String actionURL;
        String contextPath = externalContext.getRequestContextPath();
        String viewId = facesContext.getViewRoot().getViewId();
        if (contextPath == null)
        {
            actionURL = viewHandler.getViewIdPath(facesContext, viewId);
        }
        else
        {
            actionURL = contextPath + viewHandler.getViewIdPath(facesContext, viewId);
        }

        //write out dummy form
        _responseWriter.startElement(HTML.FORM_ELEM, null);
        _responseWriter.writeAttribute(HTML.ID_ATTR, DUMMY_FORM_ID, null);
        _responseWriter.writeAttribute(HTML.NAME_ATTR, DUMMY_FORM_NAME, null);
        _responseWriter.writeAttribute(HTML.STYLE_ATTR, "display:inline", null);
        _responseWriter.writeAttribute(HTML.METHOD_ATTR, "post", null);
        _responseWriter.writeAttribute(HTML.ACTION_ATTR, externalContext.encodeActionURL(actionURL), null);
        _responseWriter.flush();
        for (Iterator it = _hiddenParams.iterator(); it.hasNext(); )
        {
            renderHiddenParam(_responseWriter, (String)it.next());
        }

        StateManager stateManager = facesContext.getApplication().getViewHandler().getStateManager();
        if (stateManager.isSavingStateInClient(facesContext))
        {
            //render state parameters
            //TODO: Optimize saveSerializedView call, because serialized view is built twice!
            StateManager.SerializedView serializedView = stateManager.saveSerializedView(facesContext);
            stateManager.writeState(facesContext, serializedView);
        }

        _responseWriter.endElement(HTML.FORM_ELEM);

        super.endDocument();
    }

    private static void renderHiddenParam(ResponseWriter writer, String paramName)
        throws IOException
    {
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, paramName, null);
        writer.endElement(HTML.INPUT_ELEM);
    }



    /**
     * Replaces current ResponseWriter by a DummyFormResponseWriter.
     * @param facesContext
     * @return
     */
    public static DummyFormResponseWriter installDummyFormResponseWriter(FacesContext facesContext)
    {
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        DummyFormResponseWriter writer = (DummyFormResponseWriter)requestMap.get(DUMMY_RESPONSE_WRITER_REQ_PARAM);
        if (writer == null)
        {
            writer = new DummyFormResponseWriter(facesContext.getResponseWriter());
            facesContext.setResponseWriter(writer);
            requestMap.put(DUMMY_RESPONSE_WRITER_REQ_PARAM, writer);
            log.debug("DummyFormResponseWriter installed");
        }
        return writer;
    }


}
