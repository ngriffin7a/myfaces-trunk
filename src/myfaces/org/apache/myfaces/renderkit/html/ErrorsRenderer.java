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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIOutput;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ErrorsRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.LabelRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.tree.TreeUtils;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.FactoryFinder;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ErrorsRenderer as specified in JSF.7.6.5
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ErrorsRenderer
    extends HTMLRenderer
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               ErrorsRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Errors";

    private static final String IN_FIELD_MSG
        = ErrorsRenderer.class.getName() + ".IN_FIELD";


    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIOutput;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIOutput.TYPE);
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", OUTPUT_ERRORS_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_errors", USER_ROLE_ATTRIBUTES);
    }


    public void encodeBegin(FacesContext facescontext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        Iterator it;
        String msgClientId = (String)uiComponent.getAttribute(CLIENT_ID_ATTR);
        if (msgClientId == null)
        {
            //All messages
            it = facesContext.getMessages();
            renderErrorsList(facesContext, writer, uiComponent, it);
        }
        else if (msgClientId.length() == 0)
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
                comp = facesContext.getTree().getRoot().findComponent(msgClientId);
            }
            catch (IllegalArgumentException e) {}
            if (comp != null)
            {
                it = facesContext.getMessages(comp);
            }
            else
            {
                it = Collections.EMPTY_SET.iterator();
            }
            renderSingleComponentErrors(writer, uiComponent, it);
        }
    }


    private void renderErrorsList(FacesContext facesContext,
                                  ResponseWriter writer,
                                  UIComponent uiComponent,
                                  Iterator it)
        throws IOException
    {
        if (!it.hasNext())
        {
            return;
        }

        Map msgCompMap = getMessageComponentMap(facesContext);

        writer.write("\n<ul");

        HTMLUtil.renderCssClass(writer, uiComponent, OUTPUT_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);

        writer.write(">");

        MessageResourcesFactory mrf
            = (MessageResourcesFactory)FactoryFinder.getFactory(FactoryFinder.MESSAGE_RESOURCES_FACTORY);
        MessageResources mr = mrf.getMessageResources(MessageResourcesFactory.FACES_IMPL_MESSAGES);

        while (it.hasNext())
        {
            Message msg = (Message)it.next();
            String summary = msg.getSummary();
            String detail = msg.getDetail();

            writer.write("\n\t<li>");

            if (summary != null)
            {
                writer.write(summary);

                UIComponent msgComp = (UIComponent)msgCompMap.get(msg);
                if (msgComp != null &&
                    msgComp.getRendererType() != null &&
                    msgComp.getRendererType().equals(LabelRenderer.TYPE))
                {
                    String labelText;
                    String key = (String)msgComp.getAttribute(LabelRendererAttributes.KEY_ATTR);
                    if (key != null)
                    {
                        labelText = BundleUtils.getString(facesContext,
                                                     (String)msgComp.getAttribute(LabelRendererAttributes.BUNDLE_ATTR),
                                                     key);
                    }
                    else
                    {
                        labelText = getStringValue(facesContext, msgComp);
                    }

                    if (labelText != null &&
                        labelText.length() > 0)
                    {
                        //special Message " in <label>"
                        labelText = mr.getMessage(facesContext,
                                                  IN_FIELD_MSG,
                                                  labelText).getSummary();
                        writer.write(HTMLEncoder.encode(labelText, false, true));
                    }
                }
            }
            if (summary != null &&
                detail != null&&
                summary.length() > 0 &&
                detail.length() > 0)
            {
                writer.write(": ");
            }
            if (detail != null)
                writer.write(detail);
            writer.write("</li>");
        }
        writer.write("\n</ul>");
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
        Map msgMap = new HashMap();     //maps Message --> message component
        Map labelMap = new HashMap();   //maps clientId --> label component
        for (Iterator it = TreeUtils.treeIterator(facesContext.getTree()); it.hasNext();)
        {
            UIComponent comp = (UIComponent)it.next();

            if (comp.getRendererType() != null &&
                comp.getRendererType().equals(LabelRenderer.TYPE))
            {
                //map pointed clientId with this Label component
                String forAttr = (String)comp.getAttribute(LabelRendererAttributes.FOR_ATTR);
                if (forAttr != null)
                {
                    labelMap.put(forAttr, comp);
                }
            }

            Iterator msgIt = facesContext.getMessages(comp);
            while (msgIt.hasNext())
            {
                Message msg = (Message)msgIt.next();
                msgMap.put(msg, comp);
            }
        }

        //now we replace the mapped components by their associated labels
        for (Iterator it = msgMap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            UIComponent msgComp = (UIComponent)entry.getValue();
            String clientId = msgComp.getClientId(facesContext);
            UIComponent label = (UIComponent)labelMap.get(clientId);
            if (label != null)
            {
                entry.setValue(label);
            }
        }

        return msgMap;
    }


    private void renderSingleComponentErrors(ResponseWriter writer,
                                             UIComponent uiComponent,
                                             Iterator it)
        throws IOException
    {
        while (it.hasNext())
        {
            writer.write("<span");
            HTMLUtil.renderCssClass(writer, uiComponent, OUTPUT_CLASS_ATTR);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
            writer.write(">");

            Message msg = (Message)it.next();
            String summary = msg.getSummary();
            String detail = msg.getDetail();

            if (summary != null)
                writer.write(summary);
            if (summary != null &&
                detail != null &&
                summary.length() > 0 &&
                detail.length() > 0)
            {
                writer.write(": ");
            }
            if (detail != null)
                writer.write(detail);

            writer.write("</span>");

            if (it.hasNext())
            {
                writer.write("<br>");
            }
        }
    }


}
