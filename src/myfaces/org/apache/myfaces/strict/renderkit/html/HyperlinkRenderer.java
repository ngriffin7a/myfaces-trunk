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
package net.sourceforge.myfaces.strict.renderkit.html;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.FacesUtils;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * HyperlinkRenderer:
 * MyFaces HyperlinkRenderer decodes nested UIParameters and stores the
 * values as attributes in the UICommand.
 *
 * TODO: once code stabilizes, remove all commented out code
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HyperlinkRenderer
    extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Hyperlink";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        //super.decode must not be called, because value never comes from request
        UICommand uiCommand           = (UICommand) uiComponent;

        Map       requestParameterMap = FacesUtils.getRequestParameterMap(facesContext);

        String    paramName           = uiCommand.getClientId(facesContext);
        String    paramValue          = (String) requestParameterMap.get(paramName);

        if ((paramValue != null) && (paramValue.length() > 0))
        {
            uiCommand.fireActionEvent(facesContext);
        }

        uiCommand.setValid(true);
    }

    public void encodeBegin(FacesContext context, UIComponent uiComponent)
    throws IOException
    {
        super.encodeBegin(context, uiComponent);

        //because of possibly embedded UIParameter components, that would not
        //yet have been created, everything is done in encodeEnd
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        UICommand      uiCommand = (UICommand) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();

        if (!isEnabledOnUserRole(facesContext, uiCommand))
        {
            return;
        }

        writer.write("<a href=\"");

        String href = (String) uiCommand.getAttributes().get(HTML.HREF_ATTR);

        if (href != null)
        {
            writer.write(href);

            encodeNestedParameters(
                facesContext,
                uiCommand.getChildren(),
                href.indexOf('?') < 0);

            writer.write('"');
        }
        else
        {
            // must use this javascript--using hidden var (as in JFS EA4) has the following problem:
            //   1. click a link; 2. click browser back; 3. click another link
            //   both commands for (1) and (3) are executed on second submit, 
            //   because modern browsers save input values on "history back"
            // TODO: develop a standard .js file and include it, 
            //        instead of generating this script for every link 
            //        (could be a problem on forms with many command links).
            //        Alternatively, generate the script once per page
            UIForm form = UIComponentUtils.findForm(facesContext, uiCommand);

            if (form == null)
            {
                throw new FacesException(
                    "'command_hyperlink' must be enclosed in a 'form' tag, or 'href' must be specified");
            }

            String formName = form.getFormName();
            writer.write("javascript:var url = document.forms['");
            writer.write(formName);
            writer.write("'].action; url += (url.indexOf('?') >= 0) ? '&' : '?'; url += '");
            writer.write(uiCommand.getClientId(facesContext));
            writer.write("=1");

            encodeNestedParameters(
                facesContext,
                uiCommand.getChildren(),
                false);

            writer.write("'; document.forms['");
            writer.write(formName);
            writer.write("'].action = url; document.forms['");
            writer.write(formName);
            writer.write("'].submit()\"");
        }

        HTMLUtil.renderCssClass(writer, uiComponent, JSFAttr.COMMAND_CLASS_ATTR);

        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.COMMON_PASSTROUGH_ATTRIBUTES);
        writer.write('>');

        // write image, if any
        String image = (String) uiComponent.getAttributes().get("image");

        if (image != null)
        {
            writer.write("<img src=\"");
            writer.write(image);
            writer.write("\">");
        }

        //write link text
        String key  = (String) uiCommand.getAttributes().get(JSFAttr.KEY_ATTR);
        String text =
            (key != null)
            ? BundleUtils.getString(
                facesContext, (String) uiComponent.getAttributes().get(JSFAttr.BUNDLE_ATTR), key)
            : (String) uiComponent.getAttributes().get(JSFAttr.LABEL_ATTR);

        writer.write(HTMLEncoder.encode(text, true, true));

        // close anchor
        writer.write("</a>");
    }

//    protected void decodeNestedParameter(
//        FacesContext facesContext, UICommand uiCommand, UIParameter uiParameter)
//    throws IOException
//    {
//        String name = uiParameter.getName();
//
//        if (name == null)
//            name = uiParameter.getClientId(facesContext);
//
//        String strV =
//            ((ServletRequest)facesContext.getExternalContext().getRequest())
//                .getParameter(name);
//
//        if (strV != null) {
//            Object objValue;
//
//            find converter
//            Converter conv = null;
//            String    type =
//                ((ServletRequest)facesContext.getExternalContext().getRequest())
//                    .getParameter(name + TYPE_SUFFIX);
//
//            if (type != null)
//                conv = getApplication().getConverter(type);
//            else
//                conv =
//                    ConverterUtils.findValueConverter(
//                        facesContext, uiParameter);
//
//            if (conv != null) {
//                try {
//                    objValue =
//                        conv.getAsObject(facesContext, uiParameter, strV);
//                } catch (ConverterException e) {
//                    objValue = null;
//                    DebugUtils.getLogger().severe(
//                        "Could not reconvert hyperlink parameter " + name
//                        + " to Object.");
//                }
//            } else
//                objValue = ConverterUtils.deserializeAndDecodeBase64(strV);
//
//            uiCommand.setAttribute(name, objValue);
//        }
//    }
    protected void encodeNestedParameters(
        FacesContext facesContext, Iterator it, boolean firstParam)
    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        while (it.hasNext())
        {
            UIParameter uiParameter = (UIParameter) it.next();
            Object      objValue = uiParameter.currentValue(facesContext);

            if (objValue != null)
            {
                writer.write(firstParam ? '?' : '&');
                writer.write(UIComponentUtils.getUIParameterName(facesContext, uiParameter));
                writer.write('=');
                writer.write(urlEncode(UIComponentUtils.getAsString(facesContext, uiParameter)));
                firstParam = false;
            }
        }
    }
}
