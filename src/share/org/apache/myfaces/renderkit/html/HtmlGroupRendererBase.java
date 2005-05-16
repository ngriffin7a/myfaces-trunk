/**
 * Copyright 2004 by Irian Marinschek & Spiegl Software OEG
 */
package org.apache.myfaces.renderkit.html;

import org.apache.myfaces.renderkit.RendererUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import java.io.IOException;

/**
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
 *          <p/>
 *          $Log: $
 */
public class HtmlGroupRendererBase
        extends HtmlRenderer 
{
    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        boolean span = false;

        if(component.getId()!=null && !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
        {
            span = true;

            writer.startElement(HTML.SPAN_ELEM, component);

            HtmlRendererUtils.writeIdIfNecessary(writer, component, context);

            HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.COMMON_PASSTROUGH_ATTRIBUTES);
        }
        else
        {
            span=HtmlRendererUtils.renderHTMLAttributesWithOptionalStartElement(writer,
                                                                             component,
                                                                             HTML.SPAN_ELEM,
                                                                             HTML.COMMON_PASSTROUGH_ATTRIBUTES);
        }

        RendererUtils.renderChildren(context, component);
        if (span)
        {
            writer.endElement(HTML.SPAN_ELEM);
        }
    }

}
