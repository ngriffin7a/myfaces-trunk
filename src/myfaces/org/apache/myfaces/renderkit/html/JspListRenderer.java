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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.attr.ListRendererAttributes;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspListRenderer
        extends AbstractPanelRenderer
        implements ListRendererAttributes
{
    public static final String TYPE = "JspList";
    public static final String RENDERKIT_ATTR = JspListRenderer.class.getName() + ".renderkit";
    public static final String ACTUAL_ROW_ATTR = JspListRenderer.class.getName() + ".actrow";

    public static final String ITERATOR_ATTR    = JspListRenderer.class.getName() + ".iterator";

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIPanel.TYPE);
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        if (uicomponent.getRendererType().equals(JspListRenderer.TYPE))
        {
            writer.write("<table");
            String style = (String)uicomponent.getAttribute(PANEL_CLASS_ATTR);
            if (style != null && style.length() > 0)
            {
                writer.write(" class=\"");
                writer.write(style);
                writer.write("\"");
            }
            writer.write(">\n");

            storeRenderKit(context);
        }
        else if (uicomponent.getRendererType().equals(DataRenderer.TYPE))
        {
            Iterator it = getIterator(context, uicomponent);

            if (it.hasNext())
            {
                uicomponent.setAttribute(DataRenderer.VAR_ATTR, it.next());

                writer.write("<tr>");

            }
            else
            {
                uicomponent.setAttribute(DataRenderer.VAR_ATTR, null);
            }

        }
        else if (uicomponent.getRendererType().equals(GroupRenderer.TYPE))
        {

        }
        else
        {
            if (uicomponent.getParent().getRendererType().equals(DataRenderer.TYPE))
            {
                String style = calcStyle(getStyles(uicomponent),
                                         getNextRow(context),
                                         0,
                                         false);
                writer.write("<td");

                if (style != null && style.length() > 0)
                {
                    writer.write(" class=\"");
                    writer.write(style);
                    writer.write("\"");
                }
                writer.write(">");
            }

            // renderer can never be null ;)
            Renderer renderer = getOriginalRenderer(context, uicomponent);
            renderer.encodeBegin(context, uicomponent);
        }

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (uiComponent.getRendererType().equals(TYPE))
        {
            writer.write("</table>\n");
            restoreRenderKit(facesContext);
        }
        else if (uiComponent.getRendererType().equals(DataRenderer.TYPE) ||
                 uiComponent.getRendererType().equals(GroupRenderer.TYPE))
        {
            writer.write("</tr>}\n");
        }
        else
        {
            // renderer can never be null ;)
            Renderer renderer = getOriginalRenderer(facesContext, uiComponent);
            renderer.encodeEnd(facesContext, uiComponent);

            if (uiComponent.getParent().getRendererType().equals(DataRenderer.TYPE))
            {
                writer.write("</td>\n");
            }

        }
    }

    private boolean isLastComponent(FacesContext context, UIComponent uicomponent)
    {
        for (Iterator it = uicomponent.getParent().getChildren(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child.getComponentId().equals(uicomponent.getCompoundId()))
            {
                return it.hasNext() ? false : true;
            }
        }
        throw new IllegalStateException("Component " + uicomponent.getComponentId() + " not found in tree.");
    }

    private int getNextRow(FacesContext context)
    {
        ServletRequest request = context.getServletRequest();
        Integer value = (Integer)request.getAttribute(ACTUAL_ROW_ATTR);
        value = value == null ? new Integer(0) : new Integer(value.intValue() + 1);
        request.setAttribute(ACTUAL_ROW_ATTR, value);
        return value.intValue();
    }

    private void storeRenderKit(FacesContext context)
    {
        String renderKitId = context.getResponseTree().getRenderKitId();

        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(renderKitId, context);
        RenderKit listRenderKit;
        try
        {
            listRenderKit = renderkitFactory.getRenderKit(JspListRenderKitImpl.ID);
        }
        catch (Exception e)
        {
            listRenderKit = new JspListRenderKitImpl(this, renderKit);
            renderkitFactory.addRenderKit(JspListRenderKitImpl.ID, listRenderKit);
        }

        context.getResponseTree().setRenderKitId(JspListRenderKitImpl.ID);

        // TODO: doesn't work with nested lists (save attribute with list-component)
        context.getServletRequest().setAttribute(RENDERKIT_ATTR, renderKitId);
    }

    private void restoreRenderKit(FacesContext context)
    {
        String renderKitId = getOriginalRenderKitId(context);
        context.getResponseTree().setRenderKitId(renderKitId);
    }

    private Renderer getOriginalRenderer(FacesContext context, UIComponent component)
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(getOriginalRenderKitId(context), context);
        return renderKit.getRenderer(component.getRendererType());
    }

    private String getOriginalRenderKitId(FacesContext context)
    {
        // TODO: doesn't work with nested lists (get attribute from list-component)
        return (String)context.getServletRequest().getAttribute(RENDERKIT_ATTR);
    }

    /**
     * Delegates all method-calls except {@link #getRenderer} to the renderKit
     * given in the constructor.
     *
     */
    private static class JspListRenderKitImpl
        extends RenderKit
    {
        private static final String ID = JspListRenderKitImpl.class.getName();

        private RenderKit _renderKit;
        private JspListRenderer _jspListRenderer;

        public JspListRenderKitImpl(JspListRenderer jspListRenderer,
                                    RenderKit renderKit)
        {
            _renderKit = renderKit;
            _jspListRenderer = jspListRenderer;
        }

        public void addComponentClass(Class aClass)
        {
            _renderKit.addComponentClass(aClass);
        }

        public void addRenderer(String s, Renderer renderer)
        {
            _renderKit.addRenderer(s, renderer);
        }

        public Iterator getComponentClasses()
        {
            return _renderKit.getComponentClasses();
        }

        /**
         * Returns always an JspListRenderer.
         * @param s
         * @return JspListRenderer
         */
        public Renderer getRenderer(String s)
        {
            return _jspListRenderer;
        }

        public Iterator getRendererTypes()
        {
            return _renderKit.getRendererTypes();
        }

        public Iterator getRendererTypes(String s)
        {
            return _renderKit.getRendererTypes(s);
        }

        public Iterator getRendererTypes(UIComponent component)
        {
            return _renderKit.getRendererTypes(component);
        }
    }
}


