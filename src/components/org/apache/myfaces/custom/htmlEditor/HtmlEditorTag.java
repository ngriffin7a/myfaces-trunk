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
package org.apache.myfaces.custom.htmlEditor;

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.UIComponentTagBase;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/12/08 04:36:27  svieujot
 * Cancel last *source attributes, and make style and styleClass more modular.
 *
 * Revision 1.6  2004/12/08 04:13:56  svieujot
 * Add styleSource and styleClassSource for the htmlEditor source window.
 *
 * Revision 1.5  2004/12/04 03:26:28  svieujot
 * Various bug fixes
 *
 * Revision 1.4  2004/12/04 00:40:25  svieujot
 * htmlEditor : add style and styleClass attributes.
 *
 * Revision 1.3  2004/12/04 00:20:00  svieujot
 * htmlEditor : Add a formular mode, and more sensible defaults.
 *
 * Revision 1.2  2004/12/03 21:59:09  svieujot
 * Initial set of htmlEditor attributes.
 *
 * Revision 1.1  2004/12/02 22:28:30  svieujot
 * Add an x:htmlEditor based on the Kupu library.
 */
public class HtmlEditorTag extends UIComponentTagBase {
    
    private String style;
    private String styleClass;
    
    private String formularMode;
    
    private String allowEditSource;
    private String addKupuLogo;
    
    private String showPropertiesToolBox;
    private String showLinksToolBox;
    private String showImagesToolBox;
    private String showTablesToolBox;
    private String showDebugToolBox;
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        
        setStringProperty(component, "style", style);
        setStringProperty(component, "styleClass", styleClass);
        
        setBooleanProperty(component, "formularMode", formularMode);
        
        setBooleanProperty(component, "allowEditSource", allowEditSource);
        setBooleanProperty(component, "addKupuLogo", addKupuLogo);
        
        setBooleanProperty(component, "showPropertiesToolBox", showPropertiesToolBox);
        setBooleanProperty(component, "showLinksToolBox", showLinksToolBox);
        setBooleanProperty(component, "showImagesToolBox", showImagesToolBox);
        setBooleanProperty(component, "showTablesToolBox", showTablesToolBox);
        setBooleanProperty(component, "showDebugToolBox", showDebugToolBox);
    }
    
    public String getComponentType() {
        return HtmlEditor.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.apache.myfaces.HtmlEditor";
    }

    public void setStyle(String style){
        this.style = style;
    }
    
    public void setStyleClass(String styleClass){
        this.styleClass = styleClass;
    }
    
    public void setFormularMode(String formularMode){
        this.formularMode = formularMode;
    }

    public void setAllowEditSource(String allowEditSource){
        this.allowEditSource = allowEditSource;
    }
    
    public void setAddKupuLogo(String addKupuLogo){
        this.addKupuLogo = addKupuLogo;
    }
    
    public void setShowPropertiesToolBox(String showPropertiesToolBox){
        this.showPropertiesToolBox = showPropertiesToolBox;
    }
    
    public void setShowLinksToolBox(String showLinksToolBox){
        this.showLinksToolBox = showLinksToolBox;
    }
    
    public void setShowImagesToolBox(String showImagesToolBox){
        this.showImagesToolBox = showImagesToolBox;
    }
    
    public void setShowTablesToolBox(String showTablesToolBox){
        this.showTablesToolBox = showTablesToolBox;
    }
    
    public void setShowDebugToolBox(String showDebugToolBox){
        this.showDebugToolBox = showDebugToolBox;
    }
}