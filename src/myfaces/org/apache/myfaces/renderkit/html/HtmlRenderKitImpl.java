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
package org.apache.myfaces.renderkit.html;

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
            log.warn("Unsupported component-family/renderer-type: " + componentFamily + "/" + rendererType);
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
