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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;
import java.io.IOException;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ButtonRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Button";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        //super.decode must not be called, because value is handled here
        UICommand      uiCommand      = (UICommand) uiComponent;

        ServletRequest servletRequest =
            (ServletRequest) facesContext.getExternalContext().getRequest();

        String         paramName      = uiCommand.getClientId(facesContext);
        String         paramValue     = servletRequest.getParameter(paramName);
        boolean        submitted      = false;

        if (paramValue == null)
        {
            if (servletRequest.getParameter(paramName + ".x") != null) //image button
            {
                submitted = true;
            }
        }
        else
        {
            if (paramValue.length() > 0)
            {
                submitted = true;
            }
        }

        if (submitted)
        {
            //FIXME
            //uiCommand.fireActionEvent(facesContext);
        }

        //FIXME
        //uiCommand.setValid(true);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        UICommand      uiCommand = (UICommand) uiComponent;

        ResponseWriter writer = facesContext.getResponseWriter();

        //boolean hiddenParam = true;
        writer.write("<input type=");

        String imageSrc = (String) uiComponent.getAttributes().get(JSFAttr.IMAGE_ATTR);

        if (imageSrc != null)
        {
            writer.write("\"image\" src=\"");
            writer.write(imageSrc);
            writer.write("\" name=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write("\" id=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write('"');
        }
        else
        {
            String type = (String) uiComponent.getAttributes().get(JSFAttr.TYPE_ATTR);

            if (type == null)
            {
                type = "submit";
            }

            writer.write('"');
            writer.write(type);
            writer.write("\" name=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write("\" id=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write("\" value=\"");

            String label;
            String key = (String) uiComponent.getAttributes().get(JSFAttr.KEY_ATTR);

            if (key != null)
            {
                label =
                    BundleUtils.getString(
                        facesContext, (String) uiComponent.getAttributes().get(JSFAttr.BUNDLE_ATTR), key);
            }
            else
            {
                label = (String) uiComponent.getAttributes().get(JSFAttr.LABEL_ATTR);
            }

            if (label == null)
            {
                //FIXME
                //label = uiCommand.getCommandName();
            }

            writer.write(HTMLEncoder.encode(label, false, false));
            writer.write('"');
        }

        HTMLUtil.renderStyleClass(writer, uiComponent);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.BUTTON_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(facesContext, uiComponent);

        writer.write('>');

        /*
        if (hiddenParam)
        {
            writer.write("<input type=\"hidden\" name=\"");
            writer.write(getHiddenValueParamName(facesContext, uiComponent));
            writer.write("\" value=\"");
            String strVal = getStringValue(facesContext, uiComponent);
            writer.write(HTMLEncoder.encode(strVal, false, false));
            writer.write("\">");
        }
        */
    }
}
