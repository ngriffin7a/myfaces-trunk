/**
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
package net.sourceforge.myfaces.renderkit.html.state.client;

import net.sourceforge.myfaces.renderkit.html.state.StateSaver;
import net.sourceforge.myfaces.util.StringUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Abstract base class for StateSavers that save state in the client.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class ClientStateSaver
    implements StateSaver
{
    public static final String BODY_CONTENT_REQUEST_ATTR
        = ClientStateSaver.class.getName() + ".BODY_CONTENT";

    protected static final String URL_TOKEN = "__URLSTATE__";
    protected static final String HIDDEN_INPUTS_TOKEN = "__HIDDENINPUTSSTATE__";

    public void init(FacesContext facesContext)
    {
        // do nothing
    }

    /**
     * Writes placeholder tokens, that will be replaced later in the
     * {@link #release} method.
     *
     * @param facesContext
     * @param encodingType
     * @throws IOException
     */
    public void encodeState(FacesContext facesContext, int encodingType) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        switch (encodingType)
        {
            case StateSaver.URL_ENCODING:
                writer.write(URL_TOKEN);
                break;
            case StateSaver.HIDDEN_INPUTS_ENCODING:
                writer.write(HIDDEN_INPUTS_TOKEN);
                break;
            default:
                throw new IllegalArgumentException("Illegal encoding type " + encodingType);
        }
    }

    public void release(FacesContext facesContext)
        throws IOException
    {
        Map requestMap = null; //FIXME: FacesUtils.getRequestMap(facesContext);
        BodyContent bodyContent = (BodyContent)(requestMap.get(ClientStateSaver.BODY_CONTENT_REQUEST_ATTR));
        if (bodyContent == null)
        {
            throw new IllegalStateException("No BodyContent!?");
        }

        StringWriter urlWriter = new StringWriter();
        writeUrlState(facesContext, urlWriter);

        StringWriter hiddenInputsWriter = new StringWriter();
        writeHiddenInputsState(facesContext, hiddenInputsWriter);

        String body = bodyContent.getString();

        body = StringUtils.replace(body, URL_TOKEN, urlWriter.toString());
        body = StringUtils.replace(body, HIDDEN_INPUTS_TOKEN, hiddenInputsWriter.toString());

        bodyContent.getEnclosingWriter().write(body);
    }

    protected abstract void writeUrlState(FacesContext facesContext, Writer writer) throws IOException;

    protected abstract void writeHiddenInputsState(FacesContext facesContext, Writer writer) throws IOException;


}
