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
package net.sourceforge.myfaces.custom.panelstack;

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;
import net.sourceforge.myfaces.custom.tabbedpane.HtmlPanelTabbedPane;
import net.sourceforge.myfaces.custom.tabbedpane.HtmlPanelTab;
import net.sourceforge.myfaces.custom.tabbedpane.TabChangeEvent;

import javax.faces.application.ViewHandler;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/08/15 22:42:11  o_rossmueller
 *          new custom component: HtmlPanelStack
 *
 */
public class HtmlPanelStackRenderer extends HtmlRenderer
{

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
    }


    public boolean getRendersChildren()
    {
        return true;
    }


    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException
    {
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlPanelStack.class);

        HtmlPanelStack panelStack = (HtmlPanelStack) uiComponent;
        String selectedPanel = panelStack.getSelectedPanel();
        UIComponent childToRender = null;

        if (selectedPanel == null)
        {
            // render the first child
            if (panelStack.getChildCount() > 0) {
                childToRender = (UIComponent) panelStack.getChildren().get(0);
            }
        } else
        {
            // render the selected child
            childToRender = panelStack.findComponent(selectedPanel);
            if (childToRender == null)
            {
                // if not found, render the first child
                if (panelStack.getChildCount() > 0) {
                    childToRender = (UIComponent) panelStack.getChildren().get(0);
                }
            }
        }

        if (childToRender != null)
        {
            RendererUtils.renderChild(facesContext, childToRender);
        }
    }

}
