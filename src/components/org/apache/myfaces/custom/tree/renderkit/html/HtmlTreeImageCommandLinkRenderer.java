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
package net.sourceforge.myfaces.custom.tree.renderkit.html;

import net.sourceforge.myfaces.custom.tree.HtmlTreeImageCommandLink;
import net.sourceforge.myfaces.custom.tree.HtmlTreeNode;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlLinkRendererBase;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.5  2004/07/01 21:53:10  mwessendorf
 *          ASF switch
 *
 *          Revision 1.4  2004/06/09 20:32:16  o_rossmueller
 *          fix: removed duplicate output of value
 *
 *          Revision 1.3  2004/06/03 12:57:03  o_rossmueller
 *          modified link renderer to use one hidden field for all links according to 1.1 renderkit docs
 *          added onclick=clear_XXX to button
 *
 *          Revision 1.2  2004/05/27 15:06:39  manolito
 *          bugfix: node labels not rendered any more
 *
 *          Revision 1.1  2004/04/22 10:20:24  manolito
 *          tree component
 *
 */
public class HtmlTreeImageCommandLinkRenderer
        extends HtmlLinkRendererBase
{

    private static final Integer ZERO = new Integer(0);


    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
        String clientId = component.getClientId(facesContext);
        String reqValue = (String)facesContext.getExternalContext().getRequestParameterMap().get(HtmlRendererUtils.getHiddenCommandLinkFieldName(HtmlRendererUtils.getFormName(component, facesContext)));
        if (reqValue != null && reqValue.equals(clientId))
        {
            HtmlTreeNode node = (HtmlTreeNode)component.getParent();

            node.toggleExpanded();
        }
    }


    protected void renderCommandLinkStart(FacesContext facesContext,
                                          UIComponent component,
                                          String clientId,
                                          Object value,
                                          String style,
                                          String styleClass) throws IOException
    {

        super.renderCommandLinkStart(facesContext, component, clientId, value, style, styleClass);

        String url = ((HtmlTreeImageCommandLink)component).getImage();

        if ((url != null) && (url.length() > 0))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement(HTML.IMG_ELEM, component);

            String src;
            if (url.startsWith(HTML.HREF_PATH_SEPARATOR))
            {
                String path = facesContext.getExternalContext().getRequestContextPath();
                src = path + url;
            }
            else
            {
                src = url;
            }
            //Encode URL
            //Although this is an url url, encodeURL is no nonsense, because the
            //actual url url could also be a dynamic servlet request:
            src = facesContext.getExternalContext().encodeResourceURL(src);
            writer.writeAttribute(HTML.SRC_ATTR, src, null);
            writer.writeAttribute(HTML.BORDER_ATTR, ZERO, null);

            HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.IMG_PASSTHROUGH_ATTRIBUTES);

            writer.endElement(HTML.IMG_ELEM);
        }
    }
}
