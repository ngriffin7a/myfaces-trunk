/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2004/07/01 22:00:53  mwessendorf
 * ASF switch
 *
 * Revision 1.5  2004/05/18 12:02:29  manolito
 * getActionURL and getResourceURL must not call encodeActionURL or encodeResourceURL
 *
 */
public class DummyFormUtils
{
    private static final Log log = LogFactory.getLog(DummyFormUtils.class);

    private static final String DUMMY_FORM_RESPONSE_WRITER_WRAPPER_REQ_PARAM
            = DummyFormUtils.class.getName() + ".INSTANCE";

    public static final String DUMMY_FORM_NAME = "linkDummyForm";
    private static final String DUMMY_FORM_ID = "linkDummyForm";

    /**
     * Returns a DummyFormResponseWriter.
     * Replaces current ResponseWriter by a DummyFormResponseWriter if necessary.
     * @param facesContext
     * @return a DummyFormResponseWriter instance
     */
    public static DummyFormResponseWriter getDummyFormResponseWriter(FacesContext facesContext)
    {
        ResponseWriter currentWriter = facesContext.getResponseWriter();
        if (currentWriter instanceof DummyFormResponseWriter)
        {
            // current ResponseWriter is a DummyFormResponseWriter
            //((DummyFormResponseWriter)currentWriter).setWriteDummyForm(true);
            return (DummyFormResponseWriter)currentWriter;
        }
        else
        {
            // current ResponseWriter is not a DummyFormResponseWriter
            // and therfore must be wrapped
            Map requestMap = facesContext.getExternalContext().getRequestMap();
            DummyFormResponseWriterWrapper writer
                    = (DummyFormResponseWriterWrapper)requestMap.get(DUMMY_FORM_RESPONSE_WRITER_WRAPPER_REQ_PARAM);
            if (writer != null)
            {
                //There is already a wrapped response writer
                return writer;
            }
            else
            {
                //We must wrap current writer and replace it in the FacesContext
                writer = new DummyFormResponseWriterWrapper(currentWriter);
                facesContext.setResponseWriter(writer);
                requestMap.put(DUMMY_FORM_RESPONSE_WRITER_WRAPPER_REQ_PARAM, writer);
                log.debug("DummyFormResponseWriterWrapper installed");
                return writer;
            }
        }
    }


    public static void writeDummyForm(ResponseWriter writer,
                                      Set dummyFormParams) throws IOException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        String actionURL = viewHandler.getActionURL(facesContext, viewId);

        //write out dummy form
        writer.startElement(HTML.FORM_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, DUMMY_FORM_ID, null);
        writer.writeAttribute(HTML.NAME_ATTR, DUMMY_FORM_NAME, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:inline", null);
        writer.writeAttribute(HTML.METHOD_ATTR, "post", null);
        writer.writeURIAttribute(HTML.ACTION_ATTR,
                                 facesContext.getExternalContext().encodeActionURL(actionURL),
                                 null);
        writer.flush();

        StateManager stateManager = facesContext.getApplication().getStateManager();
        if (stateManager.isSavingStateInClient(facesContext))
        {
            //render state parameters
            //TODO: Optimize saveSerializedView call, because serialized view is built twice!
            StateManager.SerializedView serializedView = stateManager.saveSerializedView(facesContext);
            stateManager.writeState(facesContext, serializedView);
        }

        if (dummyFormParams != null)
        {
            HtmlRendererUtils.renderHiddenCommandFormParams(writer, dummyFormParams);
            HtmlRendererUtils.renderClearHiddenCommandFormParamsFunction(writer,
                                                                         DUMMY_FORM_NAME,
                                                                         dummyFormParams, null);
        }

        writer.endElement(HTML.FORM_ELEM);
    }


}
