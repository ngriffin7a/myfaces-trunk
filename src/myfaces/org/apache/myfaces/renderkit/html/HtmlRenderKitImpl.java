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
package net.sourceforge.myfaces.renderkit.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlRenderKitImpl
    extends RenderKit
{
    private static final Log log = LogFactory.getLog(HtmlRenderKitImpl.class);

    //~ Instance fields ----------------------------------------------------------------------------

    private Map _renderers;
    private ResponseStateManager _responseStateManager;

    //~ Constructors -------------------------------------------------------------------------------

    public HtmlRenderKitImpl()
    {
        _renderers = new HashMap();
        _responseStateManager = new HtmlResponseStateManager();
    }

    //~ Methods ------------------------------------------------------------------------------------

    private String key(String componentFamily, String rendererType)
    {
        return componentFamily + "." + rendererType;
    }


    public Renderer getRenderer(String componentFamily, String rendererType)
    {
        Renderer renderer = (Renderer) _renderers.get(key(componentFamily, rendererType));

        if (renderer == null)
        {
            throw new IllegalArgumentException("Unsupported component family / renderer type: " + componentFamily + " / " + rendererType);
        }

        return renderer;
    }

    public void addRenderer(String componentFamily, String rendererType, Renderer renderer)
    {
        _renderers.put(key(componentFamily, rendererType), renderer);
    }

    public ResponseStateManager getResponseStateManager()
    {
        return _responseStateManager;
    }

    public ResponseWriter createResponseWriter(Writer writer,
                                               String contentTypeList,
                                               String characterEncoding)
    {
        if (contentTypeList == null)
        {
            if (log.isInfoEnabled()) log.info("No content type list given, creating HtmlResponseWriterImpl with default content type.");
            return new HtmlResponseWriterImpl(writer, null, characterEncoding);
        }

        StringTokenizer st = new StringTokenizer(contentTypeList, ",");
        while (st.hasMoreTokens())
        {
            String contentType = st.nextToken().trim();
            if (HtmlResponseWriterImpl.supportsContentType(contentType))
            {
                return new HtmlResponseWriterImpl(writer, contentType, characterEncoding);
            }
        }

        throw new IllegalArgumentException("ContentTypeList does not contain a supported content type: " + contentTypeList);
    }

    public ResponseStream createResponseStream(OutputStream outputstream)
    {
        throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
    }
}
