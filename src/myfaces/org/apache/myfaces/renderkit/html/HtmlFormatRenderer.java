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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutputFormat;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class HtmlFormatRenderer
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlFormatRenderer.class);

    private static final Object[] EMPTY_ARGS = new Object[0];

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlOutputFormat.class);
        if (!RendererUtils.isVisibleOnUserRole(facesContext, component)) return;

        String text = getOutputFormatText(facesContext, component);
        boolean isEscape;
        if (component instanceof HtmlOutputFormat)
        {
            isEscape = ((HtmlOutputFormat)component).isEscape();
        }
        else
        {
            isEscape = RendererUtils.getBooleanAttribute(component, JSFAttr.ESCAPE_ATTR, true);
        }
        HtmlTextRenderer.renderOutputText(facesContext, component, text, isEscape);
    }

    private String getOutputFormatText(FacesContext facesContext,
                                       UIComponent htmlOutputFormat)
    {
        String pattern = RendererUtils.getStringValue(facesContext, htmlOutputFormat);
        Object[] args;
        if (htmlOutputFormat.getChildCount() == 0)
        {
            args = EMPTY_ARGS;
        }
        else
        {
            List argsList = new ArrayList();
            for (Iterator it = htmlOutputFormat.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (child instanceof UIParameter)
                {
                    argsList.add(((UIParameter)child).getValue());
                }
            }
            args = argsList.toArray(new Object[argsList.size()]);
        }

        MessageFormat format = new MessageFormat(pattern, facesContext.getViewRoot().getLocale());
        try
        {
            return format.format(args);
        }
        catch (Exception e)
        {
            log.error("Error formatting message of component " + htmlOutputFormat.getClientId(facesContext));
            return "";
        }
    }

}
