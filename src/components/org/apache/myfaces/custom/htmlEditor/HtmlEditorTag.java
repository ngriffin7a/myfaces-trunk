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

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.taglib.UIComponentTagBase;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.15  2005/03/18 15:38:15  svieujot
 * Bugfix for weblogic.
 *
 * Revision 1.14  2005/03/15 05:24:03  svieujot
 * Add a fallback textarea mode to the htmlEditor.
 *
 * Revision 1.13  2005/03/09 04:07:22  svieujot
 * htmlEditor : Kupu 1.2rc2 update
 *
 * Revision 1.12  2005/02/18 17:19:30  matzew
 * added release() to tag clazzes.
 *
 * Revision 1.11  2005/02/06 19:45:32  svieujot
 * Add allowExternalLinks attribute.
 *
 * Revision 1.10  2005/02/05 18:51:21  svieujot
 * x:htmlEditor : Upgrade to Kupu 1.2rc1, remove formularMode (too experimental), bugfixes.
 *
 * Revision 1.9  2005/01/02 20:39:16  svieujot
 * HtmlEditor can now process HTML documents and HTML fragments.
 *
 * Revision 1.8  2004/12/10 02:16:26  svieujot
 * Start implementing UserRoleAware.
 *
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

    private String fallback;
    private String type;
    
    private String allowEditSource;
    private String allowExternalLinks;
    private String addKupuLogo;
    
    private String showPropertiesToolBox;
    private String showLinksToolBox;
    private String showImagesToolBox;
    private String showTablesToolBox;
	private String showCleanupExpressionsToolBox;
    private String showDebugToolBox;
    
    private String enabledOnUserRole;
    private String visibleOnUserRole;
	
    private String immediate;
    private String required;
    private String validator;
    private String valueChangeListener;
    
    public void release() {
        super.release();
        style=null;
        styleClass=null;
		fallback=null;
        type=null;
        allowEditSource=null;
        allowExternalLinks=null;
        addKupuLogo=null;
        showPropertiesToolBox=null;
        showLinksToolBox=null;
        showImagesToolBox=null;
        showTablesToolBox=null;
		showCleanupExpressionsToolBox=null;
        showDebugToolBox=null;
        enabledOnUserRole=null;
        visibleOnUserRole=null;
		
		immediate=null;
        required=null;
        validator=null;
        valueChangeListener=null;
    }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        
        setStringProperty(component, "style", style);
        setStringProperty(component, "styleClass", styleClass);
        
		setStringProperty(component, "fallback", fallback);
        setBooleanProperty(component, "type", type);
        
        setBooleanProperty(component, "allowEditSource", allowEditSource);
        setBooleanProperty(component, "allowExternalLinks", allowExternalLinks);
        setBooleanProperty(component, "addKupuLogo", addKupuLogo);
        
        setBooleanProperty(component, "showPropertiesToolBox", showPropertiesToolBox);
        setBooleanProperty(component, "showLinksToolBox", showLinksToolBox);
        setBooleanProperty(component, "showImagesToolBox", showImagesToolBox);
        setBooleanProperty(component, "showTablesToolBox", showTablesToolBox);
		setBooleanProperty(component, "showCleanupExpressionsToolBox", showCleanupExpressionsToolBox);
        setBooleanProperty(component, "showDebugToolBox", showDebugToolBox);
        
        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, visibleOnUserRole);
		
		setBooleanProperty(component, JSFAttr.IMMEDIATE_ATTR, immediate);
        setBooleanProperty(component, JSFAttr.REQUIRED_ATTR, required);
        setValidatorProperty(component, validator);
        setValueChangedListenerProperty(component, valueChangeListener);
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
	
    public void setFallback(String fallback){
        this.fallback = fallback;
    }

    public void setType(String type){
        this.type = type;
    }
    
    public void setAllowEditSource(String allowEditSource){
        this.allowEditSource = allowEditSource;
    }
    
    public void setAllowExternalLinks(String allowExternalLinks){
        this.allowExternalLinks = allowExternalLinks;
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
    
	public void setShowCleanupExpressionsToolBox(String showCleanupExpressionsToolBox){
        this.showCleanupExpressionsToolBox = showCleanupExpressionsToolBox;
    }
	
    public void setShowDebugToolBox(String showDebugToolBox){
        this.showDebugToolBox = showDebugToolBox;
    }
    
    public void setEnabledOnUserRole(String enabledOnUserRole){
        this.enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole){
        this.visibleOnUserRole = visibleOnUserRole;
    }
	
	public void setImmediate(String immediate){
        this.immediate = immediate;
    }

    public void setRequired(String required){
        this.required = required;
    }

    public void setValidator(String validator){
        this.validator = validator;
    }

    public void setValueChangeListener(String valueChangeListener){
        this.valueChangeListener = valueChangeListener;
    }
}