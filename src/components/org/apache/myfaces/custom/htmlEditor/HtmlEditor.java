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

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * HTML Editor using the kupu library.
 * http://kupu.oscom.org/
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
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
 *
 * 
 */
public class HtmlEditor extends UIInput {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlEditor";

    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.HtmlEditor";
    
    private String _style;
    private String _styleClass;
    
    private Boolean _allowEditSource;
    private Boolean _addKupuLogo;
    
    private Boolean _showPropertiesToolBox;
    private Boolean _showLinksToolBox;
    private Boolean _showImagesToolBox;
    private Boolean _showTablesToolBox;
    private Boolean _showDebugToolBox;
    
    private Boolean _enableFlexiTools; // Formular mode

    public HtmlEditor() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        
        String[] display = new String[2];
        display[0] = _style;
        display[1] = _styleClass;
        
        values[1] = display;
        
        Boolean toolBarButtons[] = new Boolean[2];
        toolBarButtons[0] = _allowEditSource;
        toolBarButtons[1] = _addKupuLogo;
        
        values[2] = toolBarButtons;
        
        Boolean toolBoxes[] = new Boolean[5];
        toolBoxes[0] = _showPropertiesToolBox;
        toolBoxes[1] = _showLinksToolBox;
        toolBoxes[2] = _showImagesToolBox;
        toolBoxes[3] = _showTablesToolBox;
        toolBoxes[4] = _showDebugToolBox;
        
        values[3] = toolBoxes;
        
        Boolean tools[] = new Boolean[1];
        tools[0] = _enableFlexiTools;
        
        values[4] = tools;
        
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        
        String[] display = (String[]) values[1];
        _style = display[0];
        _styleClass = display[1];
        
        Boolean[] toolBarButtons = (Boolean[]) values[2];
        _allowEditSource = toolBarButtons[0];
        _addKupuLogo = toolBarButtons[1];
        
        Boolean[] toolBoxes = (Boolean[]) values[3];
        _showPropertiesToolBox = toolBoxes[0];
        _showLinksToolBox = toolBoxes[1];
        _showImagesToolBox = toolBoxes[2];
        _showTablesToolBox = toolBoxes[3];
        _showDebugToolBox = toolBoxes[4];
        
        Boolean[] tools = (Boolean[]) values[4];
        _enableFlexiTools = tools[0];
    }
    
    public String getStyle(){
   		if (_style != null)
   		    return _style;
    	ValueBinding vb = getValueBinding("style");
   		return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    
    public String getStyleClass(){
   		if (_styleClass != null)
   		    return _styleClass;
    	ValueBinding vb = getValueBinding("styleClass");
   		return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    
    public Boolean isAllowEditSource(){
   		if (_allowEditSource != null)
   		    return _allowEditSource;
   		ValueBinding vb = getValueBinding("allowEditSource");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.TRUE;
    }
    
    public Boolean isAddKupuLogo(){
   		if (_addKupuLogo != null)
   		    return _addKupuLogo;
   		ValueBinding vb = getValueBinding("addKupuLogo");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.TRUE;
    }
    
    public void setAddKupuLogo(boolean addKupuLogo){
   		this._addKupuLogo = Boolean.valueOf(addKupuLogo);
    }
    
    public Boolean isShowPropertiesToolBox(){
   		if (_showPropertiesToolBox != null)
   		    return _showPropertiesToolBox;
    	ValueBinding vb = getValueBinding("showPropertiesToolBox");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.FALSE;
    }
    
    public Boolean isShowLinksToolBox(){
   		if (_showLinksToolBox != null)
   		    return _showLinksToolBox;
    	ValueBinding vb = getValueBinding("showLinksToolBox");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.FALSE;
    }
    
    public Boolean isShowImagesToolBox(){
   		if (_showImagesToolBox != null)
   		    return _showImagesToolBox;
    	ValueBinding vb = getValueBinding("showImagesToolBox");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.FALSE;
    }
    
    public Boolean isShowTablesToolBox(){
   		if (_showTablesToolBox != null)
   		    return _showTablesToolBox;
    	ValueBinding vb = getValueBinding("showTablesToolBox");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.FALSE;
    }
    
    public Boolean isShowDebugToolBox(){
   		if (_showDebugToolBox != null)
   		    return _showDebugToolBox;
    	ValueBinding vb = getValueBinding("showDebugToolBox");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.FALSE;
    }
    
    public boolean isShowAnyToolBox(){
   		return isShowPropertiesToolBox().booleanValue()
   			|| isShowLinksToolBox().booleanValue()
   			|| isShowImagesToolBox().booleanValue()
   			|| isShowTablesToolBox().booleanValue()
   			|| isShowDebugToolBox().booleanValue();
    }
    
    public Boolean isEnableFlexiTools(){
   		if (_enableFlexiTools != null)
   		    return _enableFlexiTools;
    	ValueBinding vb = getValueBinding("formularMode");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.FALSE;
    }
}