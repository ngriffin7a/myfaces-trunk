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

import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.commons.beanutils.BeanUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.8  2004/12/17 13:19:10  mmarinschek
 * new component jsValueChangeListener
 *
 * Revision 1.7  2004/12/03 20:52:11  svieujot
 * Defer script loading for additional performance.
 *
 * Revision 1.6  2004/12/02 22:26:23  svieujot
 * Simplify the AddResource methods
 *
 * Revision 1.5  2004/12/01 16:32:03  svieujot
 * Convert the Multipart filter in an ExtensionsFilter that provides an additional facility to include resources in a page.
 * Tested only with javascript resources right now, but should work fine with images too.
 * Some work to do to include css resources.
 * The popup component has been converted to use this new Filter.
 *
 * Revision 1.4  2004/11/25 08:41:25  matzew
 * removed unused import-statements
 *
 * Revision 1.3  2004/11/23 23:24:04  mmarinschek
 * Popup tag has now more attributes
 *
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
                facesContext, popup.getClientId(facesContext),
                popup.getDisplayAtDistanceX(),popup.getDisplayAtDistanceY());

        //writeMouseOverAndOutAttribs(popupId, popupFacet.getChildren());

        writeMouseOverAttribs(popupId, uiComponent.getChildren(),
            popup.getClosePopupOnExitingElement()==null ||
                    popup.getClosePopupOnExitingElement().booleanValue());

        RendererUtils.renderChildren(facesContext, uiComponent);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM, popup);
        writer.writeAttribute(HTML.STYLE_ATTR,(popup.getStyle()!=null?(popup.getStyle()+
                (popup.getStyle().trim().endsWith(";")?"":";")):"")+
                "position:absolute;display:none;",null);
        writer.writeAttribute(HTML.CLASS_ATTR,popup.getStyleClass(),null);
        writer.writeAttribute(HTML.ID_ATTR, popup.getClientId(facesContext),null);
        writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, new String(popupId+".redisplay();"),null);

        Boolean closeExitPopup = popup.getClosePopupOnExitingPopup();

        if(closeExitPopup==null || closeExitPopup.booleanValue())
            writer.writeAttribute(HTML.ONMOUSEOUT_ATTR, new String(popupId+".hide();"),null);

        RendererUtils.renderChildren(facesContext, popupFacet);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeMouseOverAttribs(String popupId, List children, boolean renderMouseOut)
    {
        for (int i = 0; i < children.size(); i++)
        {
            UIComponent uiComponent = (UIComponent) children.get(i);

            callMethod(uiComponent,"onmouseover",new String(popupId+".display(event);"));

            if(renderMouseOut)
                callMethod(uiComponent,"onmouseout",new String(popupId+".hide(event);"));

            writeMouseOverAttribs(popupId, uiComponent.getChildren(),renderMouseOut);
        }
    }

    private String writePopupScript(FacesContext context, String clientId,
                                    Integer displayAtDistanceX, Integer displayAtDistanceY)
        throws IOException
    {
        AddResource.addJavaScriptToHeader(HtmlPopupRenderer.class, "JSPopup.js", true, context);

        String popupId = (clientId+"Popup").replace(':','_').replaceAll("_",
                "popupIdSeparator");

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.SCRIPT_ELEM,null);
        writer.writeAttribute(HTML.SCRIPT_LANGUAGE_ATTR,HTML.SCRIPT_LANGUAGE_JAVASCRIPT,null);
        writer.writeText("var "+popupId+"=new orgApacheMyfacesPopup('"+clientId+"',"+
                (displayAtDistanceX==null?-5:displayAtDistanceX.intValue())+","+
                (displayAtDistanceY==null?-5:displayAtDistanceY.intValue())+");",null);
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
        Object oldValue = uiComponent.getAttributes().get(propName);

        if(oldValue != null)
        {
            String oldValueStr = oldValue.toString().trim();

            //check if method call has already been added...
            if(oldValueStr.indexOf(value)!=-1)
                return;

            if(oldValueStr.length()>0 && !oldValueStr.endsWith(";"))
                oldValueStr +=";";

            value = oldValueStr + value;

        }

        uiComponent.getAttributes().put(propName, value);
    }
}
