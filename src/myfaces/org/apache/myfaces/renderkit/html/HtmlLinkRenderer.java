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

import net.sourceforge.myfaces.renderkit.RendererUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlLinkRenderer
    extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlLinkRenderer.class);

    public boolean getRendersChildren()
    {
        // We must be able to render the children without a surrounding anchor
        // if the Link is disabled
        return true;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        RendererUtils.checkParamValidity(facesContext, component, null);

        if (component instanceof UICommand)
        {
            String clientId = component.getClientId(facesContext);
            String reqValue = (String)facesContext.getExternalContext().getRequestParameterMap().get(clientId);
            if (reqValue != null && reqValue.equals(clientId))
            {
                component.queueEvent(new ActionEvent(component));
            }
        }
        else if (component instanceof UIOutput)
        {
            //do nothing
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, null);

        if (component instanceof UICommand)
        {
            HtmlRendererUtils.renderCommandLinkStart(facesContext, component,
                    component.getClientId(facesContext),
                    ((UICommand) component).getValue(),
                    getStyleClass(facesContext, (UICommand)component), null);
        }
        else if (component instanceof UIOutput)
        {
            HtmlRendererUtils.renderOutputLinkStart(facesContext, (UIOutput)component, null);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }


    /**
     * Can be overwritten by derived classes to overrule the style class to be used.
     * TODO: the same for Style
     */
    protected String getStyleClass(FacesContext facesContext, UICommand link)
    {
        if (link instanceof HtmlCommandLink)
        {
            return ((HtmlCommandLink)link).getStyleClass();
        }
        else
        {
            return (String)link.getAttributes().get(HTML.STYLE_CLASS_ATTR);
        }
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.renderChildren(facesContext, component);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        HtmlRendererUtils.renderLinkEnd(facesContext, component);
    }

}
