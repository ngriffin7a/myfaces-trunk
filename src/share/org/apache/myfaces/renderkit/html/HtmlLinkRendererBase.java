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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.faces.application.ViewHandler;
import javax.faces.component.*;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormResponseWriter;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormUtils;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/05/12 01:41:32  o_rossmueller
 * fix #951896: added state params to link URLs for ALLOW_JAVASCRIPT=false
 *
 * Revision 1.6  2004/05/04 06:36:21  manolito
 * Bugfix #947302
 *
 * Revision 1.5  2004/04/29 19:34:38  o_rossmueller
 * javascript for 'target' attribute handling
 *
 * Revision 1.4  2004/04/27 10:32:24  manolito
 * clear hidden inputs javascript function
 *
 * Revision 1.3  2004/04/05 11:14:05  manolito
 * removed isVisibleOnUserRole
 *
 * Revision 1.2  2004/03/31 14:50:34  manolito
 * bug fix
 *
 * Revision 1.1  2004/03/31 11:58:44  manolito
 * custom component refactoring
 *
 */
public abstract class HtmlLinkRendererBase
    extends HtmlRenderer
{

    public static final String URL_STATE_MARKER      = "JSF_URL_STATE_MARKER=DUMMY";
    public static final int    URL_STATE_MARKER_LEN  = URL_STATE_MARKER.length();
    //private static final Log log = LogFactory.getLog(HtmlLinkRenderer.class);

    public boolean getRendersChildren()
    {
        // We must be able to render the children without a surrounding anchor
        // if the Link is disabled
        return true;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);  //check for NP

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
        super.encodeBegin(facesContext, component);  //check for NP

        if (component instanceof UICommand)
        {
            renderCommandLinkStart(facesContext, component,
                                   component.getClientId(facesContext),
                                   ((UICommand)component).getValue(),
                                   getStyle(facesContext, component),
                                   getStyleClass(facesContext, component));
        }
        else if (component instanceof UIOutput)
        {
            renderOutputLinkStart(facesContext, (UIOutput)component);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }


    /**
     * Can be overwritten by derived classes to overrule the style to be used.
     */
    protected String getStyle(FacesContext facesContext, UIComponent link)
    {
        if (link instanceof HtmlCommandLink)
        {
            return ((HtmlCommandLink)link).getStyle();
        }
        else
        {
            return (String)link.getAttributes().get(HTML.STYLE_ATTR);
        }
    }

    /**
     * Can be overwritten by derived classes to overrule the style class to be used.
     */
    protected String getStyleClass(FacesContext facesContext, UIComponent link)
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
        super.encodeEnd(facesContext, component);  //check for NP
        renderLinkEnd(facesContext, component);
    }

    protected void renderCommandLinkStart(FacesContext facesContext, UIComponent component,
                                          String clientId,
                                          Object value,
                                          String style,
                                          String styleClass)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (RendererUtils.isEnabledOnUserRole(facesContext, component)) //TODO: Move to extended renderer
        {
            if (MyFacesConfig.isAllowJavascript(facesContext.getExternalContext()))
            {
                renderJavaScriptAnchorStart(facesContext, writer, component, clientId);
            }
            else
            {
                renderNonJavaScriptAnchorStart(facesContext, writer, component, clientId);
            }

            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
            HtmlRendererUtils.renderHTMLAttributes(writer, component,
                                                   HTML.ANCHOR_PASSTHROUGH_ATTRIBUTES_WITHOUT_STYLE);
            HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_ATTR, HTML.STYLE_ATTR,
                                                  style);
            HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR,
                                                  styleClass);
        }

        //MyFaces extension: Render text of given by value.  TODO: Move to extended renderer
        if(value != null)
        {
            writer.writeText(value.toString(), JSFAttr.VALUE_ATTR);
        }
    }

    protected void renderJavaScriptAnchorStart(FacesContext facesContext,
                                               ResponseWriter writer,
                                               UIComponent component,
                                               String clientId)
        throws IOException
    {
        //Find form
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIForm))
        {
            parent = parent.getParent();
        }

        UIForm nestingForm = null;
        String formName;
        DummyFormResponseWriter dummyFormResponseWriter;
        if (parent != null)
        {
            //link is nested inside a form
            nestingForm = (UIForm)parent;
            formName = nestingForm.getClientId(facesContext);
            dummyFormResponseWriter = null;
        }
        else
        {
            //not nested in form, we must add a dummy form at the end of the document
            formName = DummyFormUtils.DUMMY_FORM_NAME;
            dummyFormResponseWriter = DummyFormUtils.getDummyFormResponseWriter(facesContext);
            dummyFormResponseWriter.setWriteDummyForm(true);
        }

        StringBuffer onClick = new StringBuffer();

        String commandOnclick;
        if (component instanceof HtmlCommandLink)
        {
            commandOnclick = ((HtmlCommandLink)component).getOnclick();
        }
        else
        {
            commandOnclick = (String)component.getAttributes().get(HTML.ONCLICK_ATTR);
        }
        if (commandOnclick != null)
        {
            onClick.append(commandOnclick);
            onClick.append(';');
        }

        //call the clear_<formName> method
        onClick.append(HtmlRendererUtils.getClearHiddenCommandFormParamsFunctionName(formName)).append("();");

        String jsForm = "document.forms['" + formName + "']";

        //add id parameter for decode
        onClick.append(jsForm);
        onClick.append(".elements['").append(clientId).append("']");
        onClick.append(".value='").append(clientId).append("';");
        if (nestingForm != null)
        {
            //renderHiddenParam(writer, clientId);
            HtmlFormRendererBase.addHiddenCommandParameter(nestingForm, clientId);
        }
        else
        {
            dummyFormResponseWriter.addDummyFormParameter(clientId);
        }

        //add child parameters
        for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                Object value = ((UIParameter)child).getValue();

                renderLinkParameter(dummyFormResponseWriter, name, value, onClick, jsForm, nestingForm);
            }
        }

        // target
        String target = ((HtmlCommandLink)component).getTarget();
        if (target != null && target.trim().length() > 0) {
            onClick.append(jsForm);
            onClick.append(".target='");
            onClick.append(target);
            onClick.append("';");
        }
        //submit
        onClick.append(jsForm);
        onClick.append(".submit();return false;");  //return false, so that browser does not handle the click

        writer.startElement(HTML.ANCHOR_ELEM, component);
        writer.writeURIAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, onClick.toString(), null);
    }

    protected void renderNonJavaScriptAnchorStart(FacesContext facesContext,
                                                  ResponseWriter writer,
                                                  UIComponent component,
                                                  String clientId)
        throws IOException
    {
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        String path = viewHandler.getActionURL(facesContext, viewId);

        StringBuffer hrefBuf = new StringBuffer(path);

        //add clientId parameter for decode

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
        hrefBuf.append(clientId);
        hrefBuf.append("&");
        hrefBuf.append(URL_STATE_MARKER);

        if (component.getChildCount() > 0)
        {
            addChildParametersToHref(component, hrefBuf,
                                     false, //not the first url parameter
                                     writer.getCharacterEncoding());
        }

        hrefBuf.append("&");
        hrefBuf.append(URL_STATE_MARKER);

        String href = hrefBuf.toString();
        writer.startElement(HTML.ANCHOR_ELEM, component);
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
                Object value = ((UIParameter)child).getValue();

                addParameterToHref(name, value, hrefBuf, firstParameter, charEncoding);
                firstParameter = false;
            }
        }
    }

    public void renderOutputLinkStart(FacesContext facesContext, UIOutput output)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (!RendererUtils.isEnabledOnUserRole(facesContext, output))
        {
            //if link is disabled we render the nested components without the anchor
            RendererUtils.renderChildren(facesContext, output);
            return;
        }

        //calculate href
        String href = RendererUtils.getStringValue(facesContext, output);
        if (output.getChildCount() > 0)
        {
            StringBuffer hrefBuf = new StringBuffer(href);
            addChildParametersToHref(output, hrefBuf,
                                     (href.indexOf('?') == -1), //first url parameter?
                                     writer.getCharacterEncoding());
            href = hrefBuf.toString();
        }
        href = facesContext.getExternalContext().encodeResourceURL(href);    //TODO: or encodeActionURL ?

        //write anchor
        writer.startElement(HTML.ANCHOR_ELEM, output);
        writer.writeAttribute(HTML.ID_ATTR, output.getClientId(facesContext), null);
        writer.writeURIAttribute(HTML.HREF_ATTR, href, null);
        HtmlRendererUtils.renderHTMLAttributes(writer, output, HTML.ANCHOR_PASSTHROUGH_ATTRIBUTES);
        writer.flush();
    }

    private void renderLinkParameter(DummyFormResponseWriter dummyFormResponseWriter,
                                     String name,
                                     Object value,
                                     StringBuffer onClick,
                                     String jsForm,
                                     UIForm nestingForm)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
        }
        onClick.append(jsForm);
        onClick.append(".elements['").append(name).append("']");
        //UIParameter is no ValueHolder, so no conversion possible
        String strParamValue = value != null ? value.toString() : ""; //TODO: Use Converter?
        onClick.append(".value='").append(strParamValue).append("';");

        if (nestingForm != null)
        {
            //renderHiddenParam(writer, name);
            HtmlFormRendererBase.addHiddenCommandParameter(nestingForm, name);
        }
        else
        {
            dummyFormResponseWriter.addDummyFormParameter(name);
        }
    }

    private static void addParameterToHref(String name, Object value, StringBuffer hrefBuf, boolean firstParameter, String charEncoding)
            throws UnsupportedEncodingException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
        }

        hrefBuf.append(firstParameter ? '?' : '&');
        hrefBuf.append(URLEncoder.encode(name, charEncoding));
        hrefBuf.append('=');
        if (value != null)
        {
            //UIParameter is no ConvertibleValueHolder, so no conversion possible
            hrefBuf.append(URLEncoder.encode(value.toString(), charEncoding));
        }
    }

    /*
    private static void renderHiddenParam(ResponseWriter writer, String paramName)
        throws IOException
    {
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, paramName, null);
        writer.endElement(HTML.INPUT_ELEM);
    }
    */

    private static void renderLinkEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        if (RendererUtils.isEnabledOnUserRole(facesContext, component))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.endElement(HTML.ANCHOR_ELEM);
        }
    }


}
