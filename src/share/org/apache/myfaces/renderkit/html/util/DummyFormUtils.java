/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.renderkit.html.HTML;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
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
        writer.writeURIAttribute(HTML.ACTION_ATTR, actionURL, null);
        writer.flush();
        if (dummyFormParams != null)
        {
            for (Iterator it = dummyFormParams.iterator(); it.hasNext(); )
            {
                writer.startElement(HTML.INPUT_ELEM, null);
                writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
                writer.writeAttribute(HTML.NAME_ATTR, (String)it.next(), null);
                writer.endElement(HTML.INPUT_ELEM);
            }
        }

        StateManager stateManager = facesContext.getApplication().getStateManager();
        if (stateManager.isSavingStateInClient(facesContext))
        {
            //render state parameters
            //TODO: Optimize saveSerializedView call, because serialized view is built twice!
            StateManager.SerializedView serializedView = stateManager.saveSerializedView(facesContext);
            stateManager.writeState(facesContext, serializedView);
        }

        writer.endElement(HTML.FORM_ELEM);
    }


}
