/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.popup;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.commons.beanutils.BeanUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/11/17 11:25:42  mmarinschek
 * reviewed version of popup
 *
 * Revision 1.1  2004/11/16 16:25:52  mmarinschek
 * new popup - component; not yet finished
 *
 *
 */
public class HtmlPopupRenderer
    extends HtmlRenderer
{
    public static final String RENDERER_TYPE = "org.apache.myfaces.Popup";
    //private static final Log log = LogFactory.getLog(HtmlListRenderer.class);

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {

    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlPopup.class);

        HtmlPopup popup = (HtmlPopup) uiComponent;

        UIComponent popupFacet = popup.getPopup();

        String popupId = writePopupScript(
                facesContext, popup.getClientId(facesContext));

        //writeMouseOverAndOutAttribs(popupId, popupFacet.getChildren());

        writeMouseOverAttribs(popupId, uiComponent.getChildren());

        RendererUtils.renderChildren(facesContext, uiComponent);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM, popup);
        writer.writeAttribute(HTML.STYLE_ATTR,(popup.getStyle()!=null?(popup.getStyle()+
                (popup.getStyle().trim().endsWith(";")?"":";")):"")+
                "position:absolute;display:none;",null);
        writer.writeAttribute(HTML.CLASS_ATTR,popup.getStyleClass(),null);
        writer.writeAttribute(HTML.ID_ATTR, popup.getClientId(facesContext),null);
        writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, new String(popupId+".redisplay();"),null);
        writer.writeAttribute(HTML.ONMOUSEOUT_ATTR, new String(popupId+".hide();"),null);
        RendererUtils.renderChildren(facesContext, popupFacet);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeMouseOverAttribs(String popupId, List children)
    {
        for (int i = 0; i < children.size(); i++)
        {
            UIComponent uiComponent = (UIComponent) children.get(i);

            callMethod(uiComponent,"onmouseover",new String(popupId+".display(event);"));

            writeMouseOverAttribs(popupId, uiComponent.getChildren());
        }
    }

    private String writePopupScript(FacesContext context, String clientId)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        String popupId = (clientId+"Popup").replace(':','_').replaceAll("_",
                "popupIdSeparator");

        writer.startElement(HTML.SCRIPT_ELEM,null);
        writer.writeAttribute(HTML.SCRIPT_LANGUAGE_ATTR,HTML.SCRIPT_LANGUAGE_JAVASCRIPT,null);
        writer.writeText("var "+popupId+"=new orgApacheMyfacesPopup('"+clientId+"');",null);
        writer.endElement(HTML.SCRIPT_ELEM);

        return popupId;
    }

    private void writeMouseOverAndOutAttribs(String popupId, List children)
    {
        for (int i = 0; i < children.size(); i++)
        {
            UIComponent uiComponent = (UIComponent) children.get(i);

            callMethod(uiComponent,"onmouseover",new String(popupId+".redisplay();"));
            callMethod(uiComponent,"onmouseout",new String(popupId+".hide();"));

            writeMouseOverAndOutAttribs(popupId, uiComponent.getChildren());
        }
    }

    private void callMethod(UIComponent uiComponent, String propName, String value)
    {
        uiComponent.getAttributes().put(propName, value);

        try
        {
            BeanUtils.setProperty(uiComponent,propName,value);
        }
        catch (Throwable th)
        {
        }
    }
}
