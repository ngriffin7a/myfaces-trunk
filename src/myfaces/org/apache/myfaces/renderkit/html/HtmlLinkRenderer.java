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

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.application.MyfacesViewHandler;
import net.sourceforge.myfaces.component.html.MyFacesHtmlForm;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormResponseWriter;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * HyperlinkRenderer-Extension:
 * MyFaces HyperlinkRenderer decodes nested UIParameters and stores the
 * values as attributes in the UICommand.
 *
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

    private static final String URL_PARAM_VALUE = "1";

    public void decode(FacesContext facesContext, UIComponent component)
    {
        RendererUtils.checkParamValidity(facesContext, component, null);

        if (component instanceof HtmlCommandLink)
        {
            HtmlCommandLink commandLink = (HtmlCommandLink)component;
            String clientId = commandLink.getClientId(facesContext);
            String reqValue = (String)facesContext.getExternalContext().getRequestParameterMap().get(clientId);
            if (reqValue != null && reqValue.equals(URL_PARAM_VALUE))
            {
                commandLink.queueEvent(new ActionEvent(commandLink));
            }
        }
        else if (component instanceof HtmlOutputLink)
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

        if (RendererUtils.isEnabledOnUserRole(facesContext, component))
        {
            if (component instanceof HtmlCommandLink)
            {
                renderCommandLinkStart(facesContext, (HtmlCommandLink)component);
            }
            else if (component instanceof HtmlOutputLink)
            {
                renderOutputLinkStart(facesContext, (HtmlOutputLink)component);
            }
            else
            {
                throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
            }
        }
    }

    public void renderCommandLinkStart(FacesContext facesContext, HtmlCommandLink commandLink)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (MyFacesConfig.isAllowJavascript(facesContext.getExternalContext()))
        {
            renderJavaScriptAnchorStart(facesContext, writer, commandLink);
        }
        else
        {
            renderNonJavaScriptAnchorStart(facesContext, writer, commandLink);
        }

        HTMLUtil.renderHTMLAttributes(writer, commandLink, HTML.ANCHOR_PASSTHROUGH_ATTRIBUTES);

        //MyFaces extension: Render text given by value
        Object value = commandLink.getValue();
        if(value != null)
        {
            writer.writeText(value.toString(), JSFAttr.VALUE_ATTR);
        }
    }

    private void renderNonJavaScriptAnchorStart(FacesContext facesContext,
                                                ResponseWriter writer,
                                                HtmlCommandLink commandLink)
        throws IOException
    {
        String path;
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        ExternalContext externalContext = facesContext.getExternalContext();
        String viewId = facesContext.getViewRoot().getViewId();
        String contextPath = externalContext.getRequestContextPath();
        if (contextPath == null)
        {
            path = viewHandler.getViewIdPath(facesContext, viewId);
        }
        else
        {
            path = contextPath + viewHandler.getViewIdPath(facesContext, viewId);
        }

        StringBuffer hrefBuf = new StringBuffer(path);

        //add clientId parameter for decode
        String clientId = commandLink.getClientId(facesContext);
        if (path.indexOf('?') == -1)
        {
            hrefBuf.append('?');
        }
        else
        {
            hrefBuf.append('&');
        }
        hrefBuf.append(clientId);
        hrefBuf.append('=');
        hrefBuf.append(URL_PARAM_VALUE);

        if (commandLink.getChildCount() > 0)
        {
            addChildParametersToHref(commandLink, hrefBuf,
                                     false, //not the first url parameter
                                     writer.getCharacterEncoding());
        }

        String href = hrefBuf.toString();
        if (viewHandler instanceof MyfacesViewHandler)
        {
            href = ((MyfacesViewHandler)viewHandler).encodeURL(facesContext, href);
        }
        else
        {
            href = externalContext.encodeResourceURL(href);    //TODO: or encodeActionURL ?
        }

        writer.startElement(HTML.ANCHOR_ELEM, commandLink);
        writer.writeURIAttribute(HTML.HREF_ATTR, href, null);
    }

    private void addChildParametersToHref(UIComponent linkComponent,
                                          StringBuffer hrefBuf,
                                          boolean firstParameter,
                                          String charEncoding)
            throws IOException
    {
        for (Iterator it = linkComponent.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                if (name == null)
                {
                    throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
                }
                Object value = ((UIParameter)child).getValue();

                hrefBuf.append(firstParameter ? '?' : '&');
                hrefBuf.append(URLEncoder.encode(name, charEncoding));
                hrefBuf.append('=');
                if (value != null)
                {
                    //UIParameter is no ConvertibleValueHolder, so no conversion possible
                    hrefBuf.append(URLEncoder.encode(value.toString(), charEncoding));
                }
            }
        }
    }


    private void renderJavaScriptAnchorStart(FacesContext facesContext,
                                            ResponseWriter writer,
                                            HtmlCommandLink commandLink)
        throws IOException
    {
        //Find form
        UIComponent parent = commandLink.getParent();
        while (parent != null && !(parent instanceof UIForm))
        {
            parent = parent.getParent();
        }

        boolean insideForm;
        String formName;
        if (parent != null)
        {
            //link is nested inside a form
            UIForm form = (UIForm)parent;
            formName = MyFacesHtmlForm.getFormName(facesContext, form);
            insideForm = true;
        }
        else
        {
            //not nested in form, we must add a dummy form at the end of the document
            //we do this by replacing the current ResponseWriter by a wrapper
            writer = DummyFormResponseWriter.installDummyFormResponseWriter(facesContext);
            formName = DummyFormResponseWriter.DUMMY_FORM_NAME;
            insideForm = false;
        }

        StringBuffer onClick = new StringBuffer();
        if (commandLink.getOnclick() != null)
        {
            onClick.append(commandLink.getOnclick());
            onClick.append(';');
        }

        String jsForm = "document.forms['" + formName + "']";

        //add id parameter for decode
        String clientId = commandLink.getClientId(facesContext);
        onClick.append(jsForm);
        onClick.append(".elements['").append(clientId).append("']");
        onClick.append(".value='").append(URL_PARAM_VALUE).append("';");
        if (insideForm)
        {
            renderHiddenParam(writer, clientId);
        }
        else
        {
            ((DummyFormResponseWriter)writer).addHiddenParam(clientId);
        }

        //add child parameters
        for (Iterator it = commandLink.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                if (name == null)
                {
                    throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
                }
                Object value = ((UIParameter)child).getValue();
                onClick.append(jsForm);
                onClick.append(".elements['").append(name).append("']");
                //UIParameter is no ConvertibleValueHolder, so no conversion possible
                onClick.append(".value='").append(value.toString()).append("';");

                if (insideForm)
                {
                    renderHiddenParam(writer, name);
                }
                else
                {
                    ((DummyFormResponseWriter)writer).addHiddenParam(name);
                }
            }
        }

        //submit
        onClick.append(jsForm);
        onClick.append(".submit();");

        writer.startElement(HTML.ANCHOR_ELEM, commandLink);
        writer.writeURIAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, onClick.toString(), null);
    }


    public void renderOutputLinkStart(FacesContext facesContext, HtmlOutputLink outputLink)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (!RendererUtils.isEnabledOnUserRole(facesContext, outputLink))
        {
            RendererUtils.renderChildren(facesContext, outputLink);
            return;
        }

        //calculate href
        String href = RendererUtils.getStringValue(facesContext, outputLink);
        if (outputLink.getChildCount() > 0)
        {
            StringBuffer hrefBuf = new StringBuffer(href);
            addChildParametersToHref(outputLink, hrefBuf,
                                     (href.indexOf('?') == -1), //first url parameter?
                                     writer.getCharacterEncoding());
            href = hrefBuf.toString();
        }
        href = facesContext.getExternalContext().encodeResourceURL(href);    //TODO: or encodeActionURL ?

        //write anchor
        writer.startElement(HTML.ANCHOR_ELEM, outputLink);
        writer.writeURIAttribute(HTML.HREF_ATTR, href, null);
        HTMLUtil.renderHTMLAttributes(writer, outputLink, HTML.ANCHOR_PASSTHROUGH_ATTRIBUTES);
        writer.flush();
    }


    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.renderChildren(facesContext, component);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (RendererUtils.isEnabledOnUserRole(facesContext, component))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.endElement(HTML.ANCHOR_ELEM);
        }
    }



    private static void renderHiddenParam(ResponseWriter writer, String paramName)
        throws IOException
    {
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, paramName, null);
        writer.endElement(HTML.INPUT_ELEM);
    }


}
