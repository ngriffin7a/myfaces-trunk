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
package org.apache.myfaces.custom.panelstack;

import org.apache.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;


/**
 * PanelStack tag.
 * 
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.3  2004/10/13 11:50:57  matze
 *          renamed packages to org.apache
 *
 *          Revision 1.2  2004/09/01 18:32:55  mwessendorf
 *          Organize Imports
 *
 *          Revision 1.1  2004/08/15 22:42:12  o_rossmueller
 *          new custom component: HtmlPanelStack
 *
 *          Revision 1.6  2004/08/15 15:28:04  o_rossmueller
 *          new model listener handling to get modified from events which occur outside the scope of a tree request
 *
 *          Revision 1.5  2004/07/01 21:53:06  mwessendorf
 *          ASF switch
 *
 *          Revision 1.4  2004/05/10 01:24:51  o_rossmueller
 *          added iconClass attribute
 *
 *          Revision 1.3  2004/05/05 00:18:57  o_rossmueller
 *          various fixes/modifications in model event handling and tree update
 *
 *          Revision 1.2  2004/04/22 21:59:17  o_rossmueller
 *          added expandRoot attribute
 *
 *          Revision 1.1  2004/04/22 10:20:25  manolito
 *          tree component
 *
 */
public class PanelStackTag
        extends UIComponentTagBase
{

    private String selectedPanel;


    public String getComponentType()
    {
        return "org.apache.myfaces.HtmlPanelStack";
    }


    public String getRendererType()
    {
        return "org.apache.myfaces.PanelStack";
    }


    public String getSelectedPanel()
    {
        return selectedPanel;
    }


    public void setSelectedPanel(String selectedPanel)
    {
        this.selectedPanel = selectedPanel;
    }


    public int doStartTag() throws JspException
    {
        return super.doStartTag();
    }


    /**
     * Applies attributes to the tree component
     */
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        FacesContext context = FacesContext.getCurrentInstance();

        setStringProperty(component, "selectedPanel", selectedPanel);
    }
}
