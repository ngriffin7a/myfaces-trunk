/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.attr.ext.LayoutRendererAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LayoutRenderer
    extends HTMLRenderer
    implements CallbackRenderer, LayoutRendererAttributes
{
    private static final Log log = LogFactory.getLog(LayoutRenderer.class);

    static final String HEADER = "LayoutHeader";
    static final String NAVIGATION = "LayoutNavigation";
    static final String BODY = "LayoutBody";
    static final String FOOTER = "LayoutFooter";

    private static final String BEGIN_TOKEN_PREFIX = "[";
    private static final String BEGIN_TOKEN_SUFFIX = "-BEGIN]";
    private static final String END_TOKEN_PREFIX = "[";
    private static final String END_TOKEN_SUFFIX = "-END]";

    public static final String CLASSIC_LAYOUT = "classic";
    public static final String NAV_RIGHT_LAYOUT = "navigationRight";
    public static final String UPSIDE_DOWN_LAYOUT = "upsideDown";

    public static final String HEADER_FACET = "header";
    public static final String NAVIGATION_FACET = "navigation";
    public static final String BODY_FACET = "body";
    public static final String FOOTER_FACET = "footer";

    public static final String TYPE = "Layout";
    public String getRendererType()
    {
        return TYPE;
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        CallbackSupport.addChildrenCallbackRenderer(facesContext, uiComponent, this);
    }


    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent instanceof UIPanel)
        {
            UIComponent parent = uiComponent.getParent();
            if (uiComponent == parent.getFacet(HEADER_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(beginToken(LayoutRenderer.HEADER));
            }
            else if (uiComponent == parent.getFacet(NAVIGATION_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(beginToken(LayoutRenderer.NAVIGATION));
            }
            else if (uiComponent == parent.getFacet(BODY_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(beginToken(LayoutRenderer.BODY));
            }
            else if (uiComponent == parent.getFacet(FOOTER_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(beginToken(LayoutRenderer.FOOTER));
            }
        }
    }

    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent instanceof UIPanel)
        {
            UIComponent parent = uiComponent.getParent();
            if (uiComponent == parent.getFacet(HEADER_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(endToken(LayoutRenderer.HEADER));
            }
            else if (uiComponent == parent.getFacet(NAVIGATION_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(endToken(LayoutRenderer.NAVIGATION));
            }
            else if (uiComponent == parent.getFacet(BODY_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(endToken(LayoutRenderer.BODY));
            }
            else if (uiComponent == parent.getFacet(FOOTER_FACET))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(endToken(LayoutRenderer.FOOTER));
            }
        }
    }

    protected static String beginToken(String part)
    {
        return BEGIN_TOKEN_PREFIX + part + BEGIN_TOKEN_SUFFIX;
    }

    protected static String endToken(String part)
    {
        return END_TOKEN_PREFIX + part + END_TOKEN_SUFFIX;
    }





    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        writeBody(facesContext, uiComponent);
        CallbackSupport.removeCallbackRenderer(facesContext, uiComponent, this);
    }




    protected void writeBody(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        BodyContent bodyContent = getBodyContent(facesContext, uiComponent);
        if (bodyContent == null)
        {
            throw new IllegalStateException("No BodyContent!?");
        }

        String layout = (String)((UIPanel)uiComponent).currentValue(facesContext);
        if (layout == null)
        {
            log.warn("Component " + uiComponent.getClientId(facesContext) + " has no layout attribute!");
            layout = CLASSIC_LAYOUT;
        }

        if (layout.equals(CLASSIC_LAYOUT))
        {
            writeClassicLayout(facesContext, uiComponent, bodyContent);
        }
        else if (layout.equals(NAV_RIGHT_LAYOUT))
        {
            writeNavRightLayout(facesContext, uiComponent, bodyContent);
        }
        else if (layout.equals(UPSIDE_DOWN_LAYOUT))
        {
            writeUpsideDownLayout(facesContext, uiComponent, bodyContent);
        }
        else
        {
            log.error("Layout '" + layout + "' is not supported.");
            ResponseWriter writer = facesContext.getResponseWriter();
            bodyContent.writeOut(writer);
            return;
        }
    }


    /*
    protected String findChildClassAttribute(UIComponent uiComponent,
                                             String cssClassAttribute)
    {
        for (Iterator it = uiComponent.getChildren(); it.hasNext();)
        {
            String cssClass = (String)((UIComponent)it.next()).getAttribute(cssClassAttribute);
            if (cssClass != null)
            {
                return cssClass;
            }
        }
        return null;
    }
    */


    protected void writePartAsTd(ResponseWriter writer,
                                 BodyContent bodyContent,
                                 String part,
                                 UIComponent uiComponent,
                                 String cssClassAttribute,
                                 int colSpan)
        throws IOException
    {
        writer.write("<td colspan=\"" + colSpan +"\"");
        String cssClass = (String)uiComponent.getAttributes().get(cssClassAttribute);
        if (cssClass != null)
        {
            writer.write(" class=\"" + cssClass + "\"");
        }
        writer.write(">");
        writePart(writer, bodyContent, part);
        writer.write("</td>");
    }



    protected void writePart(ResponseWriter writer,
                             BodyContent bodyContent,
                             String part)
        throws IOException
    {
        String bodyString = bodyContent.getString();

        String beginToken = beginToken(part);

        int startIdx = bodyString.indexOf(beginToken);
        if (startIdx == -1)
        {
            //Part not present
            return;
        }

        int endIdx = bodyString.indexOf(endToken(part), startIdx);
        if (endIdx < startIdx)
        {
            throw new IllegalArgumentException("Curious tokens.");
        }

        writer.write(bodyString.substring(startIdx + beginToken.length(), endIdx));
    }



    protected void writeClassicLayout(FacesContext facesContext,
                                      UIComponent uiComponent,
                                      BodyContent bodyContent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table");
        String cssClass = (String)uiComponent.getAttributes().get(JSFAttr.PANEL_CLASS_ATTR);
        if (cssClass != null)
        {
            writer.write(" class=\"" + cssClass + "\"");
        }
        writer.write(">");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, HEADER, uiComponent, HEADER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, NAVIGATION, uiComponent, NAVIGATION_CLASS_ATTR, 1);
        writePartAsTd(writer, bodyContent, BODY, uiComponent, BODY_CLASS_ATTR, 1);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, FOOTER, uiComponent, FOOTER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("</table>");
    }

    protected void writeNavRightLayout(FacesContext facesContext,
                                       UIComponent uiComponent,
                                       BodyContent bodyContent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table");
        String cssClass = (String)uiComponent.getAttributes().get(JSFAttr.PANEL_CLASS_ATTR);
        if (cssClass != null)
        {
            writer.write(" class=\"" + cssClass + "\"");
        }
        writer.write(">");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, HEADER, uiComponent, HEADER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, BODY, uiComponent, BODY_CLASS_ATTR, 1);
        writePartAsTd(writer, bodyContent, NAVIGATION, uiComponent, NAVIGATION_CLASS_ATTR, 1);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, FOOTER, uiComponent, FOOTER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("</table>");
    }

    protected void writeUpsideDownLayout(FacesContext facesContext,
                                         UIComponent uiComponent,
                                         BodyContent bodyContent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table");
        String cssClass = (String)uiComponent.getAttributes().get(JSFAttr.PANEL_CLASS_ATTR);
        if (cssClass != null)
        {
            writer.write(" class=\"" + cssClass + "\"");
        }
        writer.write(">");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, FOOTER, uiComponent, FOOTER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, NAVIGATION, uiComponent, NAVIGATION_CLASS_ATTR, 1);
        writePartAsTd(writer, bodyContent, BODY, uiComponent, BODY_CLASS_ATTR, 1);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, HEADER, uiComponent, HEADER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("</table>");
    }



}
