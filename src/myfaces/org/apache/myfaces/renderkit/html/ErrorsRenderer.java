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
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.NullIterator;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * ErrorsRenderer as specified in JSF.7.6.5
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ErrorsRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------
    private static final Log log = LogFactory.getLog(ErrorsRenderer.class);

    public static final String  TYPE         = "Errors";
    private static final String IN_FIELD_MSG = ErrorsRenderer.class.getName() + ".IN_FIELD";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext facescontext, UIComponent uiComponent)
    throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        ResponseWriter writer  = facesContext.getResponseWriter();
        Iterator       it;
        String         forAttr = (String) uiComponent.getAttributes().get(JSFAttr.FOR_ATTR);

        if (forAttr == null)
        {
            //All messages
            it = facesContext.getMessages();
            renderErrorsList(facesContext, writer, uiComponent, it);
        }
        else if (forAttr.length() == 0)
        {
            //All component messages
            it = facesContext.getMessages(null);
            renderErrorsList(facesContext, writer, uiComponent, it);
        }
        else
        {
            //All messages for this component
            UIComponent comp = null;

            try
            {
                //FIXME
                //comp = facesContext.getTree().getRoot().findComponent(forAttr);
            }
            catch (IllegalArgumentException e)
            {
            }

            if (comp != null)
            {
                //FIXME
                //it = facesContext.getMessages(comp);
                it = null;
            }
            else
            {
                log.warn("Attribute 'for' of component '" + uiComponent.getClientId(facesContext)
                         + "' references unknown component '" + forAttr + "'.");
                it = NullIterator.instance();
            }

            renderSingleComponentErrors(writer, uiComponent, it);
        }
    }

    /**
     * Returns a Map that maps each Message in the current FacesContext to
     * it's corresponding UIComponent. If a label (i.e. component
     * with RendererType "Label") is found, the Message is mapped to the label
     * instead of the component itself.
     * @param facesContext
     * @return
     */
    private Map getMessageComponentMap(FacesContext facesContext)
    {
        Map msgMap   = new HashMap(); //maps Message --> message component
        Map labelMap = new HashMap(); //maps clientId --> label component

        //FIXME
        /*
        for (Iterator it = TreeUtils.treeIterator(facesContext.getTree()); it.hasNext();)
        {
            UIComponent comp = (UIComponent) it.next();

            if (
                (comp.getRendererType() != null)
                        && comp.getRendererType().equals(LabelRenderer.TYPE))
            {
                //map pointed clientId with this Label component
                String forAttr = (String) comp.getAttribute(JSFAttr.FOR_ATTR);

                if (forAttr != null)
                {
                    labelMap.put(forAttr, comp);
                }
            }

            Iterator msgIt = facesContext.getMessages(comp);

            while (msgIt.hasNext())
            {
                Message msg = (Message) msgIt.next();
                msgMap.put(msg, comp);
            }
        }
        */

        //now we replace the mapped components by their associated labels
        for (Iterator it = msgMap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry   entry    = (Map.Entry) it.next();
            UIComponent msgComp  = (UIComponent) entry.getValue();
            String      clientId = msgComp.getClientId(facesContext);
            UIComponent label    = (UIComponent) labelMap.get(clientId);

            if (label != null)
            {
                entry.setValue(label);
            }
        }

        return msgMap;
    }

    private void renderErrorsList(
        FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent, Iterator it)
    throws IOException
    {
        if (!it.hasNext())
        {
            return;
        }

        Map msgCompMap = getMessageComponentMap(facesContext);

        writer.write("\n<ul");

        HTMLUtil.renderStyleClass(writer, uiComponent);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);

        writer.write('>');

        //FIXME
        /*
        MessageResources   mr =
            facesContext.getApplication().getMessageResources(MessageResources.FACES_IMPL_MESSAGES);
        */

        while (it.hasNext())
        {
            FacesMessage msg     = (FacesMessage) it.next();
            String  summary = msg.getSummary();
            String  detail  = msg.getDetail();

            writer.write("\n\t<li>");

            if (summary != null)
            {
                writer.write(summary);

                UIComponent msgComp = (UIComponent) msgCompMap.get(msg);

                if (
                    (msgComp != null) && (msgComp.getRendererType() != null)
                            && msgComp.getRendererType().equals(LabelRenderer.TYPE))
                {
                    String labelText;
                    String key = (String) msgComp.getAttributes().get(JSFAttr.KEY_ATTR);

                    if (key != null)
                    {
                        labelText =
                            BundleUtils.getString(
                                facesContext,
                                (String) msgComp.getAttributes().get(JSFAttr.BUNDLE_ATTR),
                                key);
                    }
                    else
                    {
                        if (msgComp instanceof javax.faces.component.UIOutput)
                        {
                            labelText =
                                getStringValue(
                                    facesContext, (javax.faces.component.UIOutput) msgComp);
                        }
                        else
                        {
                            log.warn("Label component " + msgComp.getClientId(facesContext)
                                     + " is no UIOutput.");
                            labelText = "???";
                        }
                    }

                    if ((labelText != null) && (labelText.length() > 0))
                    {
                        //special Message " in <label>"
                        //FIXME
                        /*
                        labelText =
                            mr.getMessage(facesContext, IN_FIELD_MSG, labelText).getSummary();
                            */
                        writer.write(HTMLEncoder.encode(labelText, false, true));
                    }
                }
            }

            if (
                (summary != null) && (detail != null) && (summary.length() > 0)
                        && (detail.length() > 0))
            {
                writer.write(": ");
            }

            if (detail != null)
            {
                writer.write(detail);
            }

            writer.write("</li>");
        }

        writer.write("\n</ul>");
    }

    private void renderSingleComponentErrors(
        ResponseWriter writer, UIComponent uiComponent, Iterator it)
    throws IOException
    {
        while (it.hasNext())
        {
            writer.write("<span");
            HTMLUtil.renderStyleClass(writer, uiComponent);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
            writer.write('>');

            FacesMessage msg     = (FacesMessage) it.next();
            String  summary = msg.getSummary();
            String  detail  = msg.getDetail();

            if (summary != null)
            {
                writer.write(summary);
            }

            if (
                (summary != null) && (detail != null) && (summary.length() > 0)
                        && (detail.length() > 0))
            {
                writer.write(": ");
            }

            if (detail != null)
            {
                writer.write(detail);
            }

            writer.write("</span>");

            if (it.hasNext())
            {
                writer.write("<br>");
            }
        }
    }
}
