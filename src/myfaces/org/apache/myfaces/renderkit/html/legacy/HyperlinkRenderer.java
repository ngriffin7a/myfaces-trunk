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
package net.sourceforge.myfaces.renderkit.html.legacy;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.Iterator;

/**
 * HyperlinkRenderer-Extension:
 * MyFaces HyperlinkRenderer decodes nested UIParameters and stores the
 * values as attributes in the UICommand.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HyperlinkRenderer
    extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HyperlinkRenderer.class);

    public static final String TYPE = "Hyperlink";

    //private static final String TYPE_SUFFIX = ".TYPE";
    private static final String URL_PARAM_VALUE = "1";

    public String getRendererType()
    {
        return TYPE;
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        //super.decode must not be called, because value never comes from request

        UICommand uiCommand = (UICommand)uiComponent;

        String paramName = uiComponent.getClientId(facesContext);
        String paramValue = ((ServletRequest)facesContext.getExternalContext().getRequest()).getParameter(paramName);
        if (paramValue != null && paramValue.equals(URL_PARAM_VALUE))
        {
            /*
            //nested parameters
            Iterator children = uiComponent.getChildren();
            while (children.hasNext())
            {
                UIComponent child = (UIComponent)children.next();
                if (child instanceof UIParameter)
                {
                    decodeNestedParameter(facesContext, uiCommand, (UIParameter)child);
                }
            }
            */

            //FIXME
            //uiCommand.fireActionEvent(facesContext);
        }

        //FIXME
        //uiCommand.setValid(true);
    }


    /*
    protected void decodeNestedParameter(FacesContext facesContext,
                                         UICommand uiCommand,
                                         UIParameter uiParameter) throws IOException
    {
        String name = uiParameter.getName();
        if (name == null)
        {
            name = uiParameter.getClientId(facesContext);
        }
        String strV = ((ServletRequest)facesContext.getExternalContext().getRequest()).getParameter(name);
        if (strV != null)
        {
            Object objValue;
            //find converter
            Converter conv = null;
            String type = ((ServletRequest)facesContext.getExternalContext().getRequest()).getParameter(name + TYPE_SUFFIX);
            if (type != null)
            {
                conv = getApplication().getConverter(type);
            }
            else
            {
                conv = ConverterUtils.findValueConverter(facesContext, uiParameter);
            }

            if (conv != null)
            {
                try
                {
                    objValue = conv.getAsObject(facesContext, uiParameter, strV);
                }
                catch (ConverterException e)
                {
                    objValue = null;
                    DebugUtils.getLogger().severe("Could not reconvert hyperlink parameter " + name + " to Object.");
                }
            }
            else
            {
                objValue = ConverterUtils.deserializeAndDecodeBase64(strV);
            }

            uiCommand.setAttribute(name, objValue);
        }
    }
    */


    public void encodeBegin(FacesContext context, UIComponent uiComponent) throws IOException
    {
        //because of possibly embedded UIParameter components, that would not
        //yet have been created, everything is done in encodeEnd
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();

        if (!isEnabledOnUserRole(facesContext, uiComponent))
        {
            //write out body content
            BodyContent bodyContent = getBodyContent(facesContext, uiComponent);
            bodyContent.writeOut(writer);
            return;
        }

        writer.write("<a href=\"");
        String href = (String)uiComponent.getAttributes().get(HTML.HREF_ATTR);
        if (href == null)
        {
            //Modify URL for the faces servlet mapping:
            ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
            //FIXME
            /*
            ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
            ServletMapping sm = smf.getServletMapping(servletContext);
            String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getTree().getTreeId());

            href = request.getContextPath() + treeURL;
            */
            throw new UnsupportedOperationException("fixme");
        }

        //Encode URL...
        //FIXME
        //href = facesContext.getExternalContext().encodeURL(href);

        writer.write(href);

        //value
        if (href.indexOf('?') == -1)
        {
            writer.write('?');
        }
        else
        {
            writer.write('&');
        }
        writer.write(uiComponent.getClientId(facesContext));
        writer.write('=');
        writer.write(URL_PARAM_VALUE);

        //nested parameters
        //FIXME
        //Iterator children = uiComponent.getChildren();
        Iterator children = null;
        while (children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UIParameter)
            {
                encodeNestedParameter(facesContext, (UIParameter)child);
            }
        }

        //state:
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        //FIXME
        //RenderKit renderKit = rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        RenderKit renderKit = null;
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        writer.write("\"");

        HTMLUtil.renderStyleClass(writer, uiComponent);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.ANCHOR_ATTRIBUTES);

        writer.write(">");

        //write link text
        String key = (String)uiComponent.getAttributes().get(JSFAttr.KEY_ATTR);
        if (key != null)
        {
            String text = BundleUtils.getString(facesContext,
                                                (String)uiComponent.getAttributes().get(JSFAttr.BUNDLE_ATTR),
                                                key);
            writer.write(HTMLEncoder.encode(text, true, true));
        }

        //write out body content
        BodyContent bodyContent = getBodyContent(facesContext, uiComponent);
        //FIXME
        //if (bodyCon)
        //    bodyContent.writeOut(writer);

        //close anchor
        writer.write("</a>");
    }


    protected void encodeNestedParameter(FacesContext facesContext,
                                         UIParameter uiParameter) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        //FIXME
        //Object objValue = uiParameter.currentValue(facesContext);
        Object objValue = null;
        if (objValue != null)
        {
            String name = uiParameter.getName();
            if (name == null)
            {
                name = uiParameter.getClientId(facesContext);
            }
            writer.write('&');
            writer.write(name);
            writer.write('=');

            String strValue;

            //FIXME
            //Converter conv = ConverterUtils.findValueConverter(facesContext, uiParameter);
            Converter conv = null;
            if (conv != null)
            {
                try
                {
                    strValue = conv.getAsString(facesContext, uiParameter, objValue);

                    /*
                    if (uiParameter.getAttribute(CONVERTER_ATTR) == null &&
                        uiParameter.getConverter() == null)
                    {
                        //send type of parameter
                        writer.write('&');
                        writer.write(name + TYPE_SUFFIX);
                        writer.write('=');
                        writer.write(objValue.getClass().getName());
                    }
                    */
                }
                catch (ConverterException e)
                {
                    log.error("Could not convert hyperlink parameter " + name + " to String.");
                    strValue = objValue.toString();
                }
            }
            else
            {
                //writer.write(urlEncode(ConverterUtils.serializeAndEncodeBase64(objValue)));
                strValue = objValue.toString();
            }

            writer.write(urlEncode(strValue));
        }
    }

}
