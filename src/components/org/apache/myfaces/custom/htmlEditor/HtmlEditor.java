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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.renderkit.RendererUtils;

/**
 * HTML Editor using the kupu library.
 * http://kupu.oscom.org/
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.16  2005/02/05 18:51:21  svieujot
 * x:htmlEditor : Upgrade to Kupu 1.2rc1, remove formularMode (too experimental), bugfixes.
 *
 * Revision 1.15  2005/01/03 03:49:31  svieujot
 * trim returned text
 *
 * Revision 1.14  2005/01/02 20:39:16  svieujot
 * HtmlEditor can now process HTML documents and HTML fragments.
 *
 * Revision 1.13  2004/12/24 13:21:50  matzew
 * organized imports
 *
 * Revision 1.12  2004/12/09 05:16:44  svieujot
 * Simplify by extending html.ext.HtmlInputText instead of extending UIInput and implementing UserRolesAware.
 *
 * Revision 1.11  2004/12/08 04:36:27  svieujot
 * Cancel last *source attributes, and make style and styleClass more modular.
 *
 * Revision 1.10  2004/12/08 04:13:56  svieujot
 * Add styleSource and styleClassSource for the htmlEditor source window.
 *
 * Revision 1.9  2004/12/06 04:26:07  svieujot
 * Make HtmlEditor UserRoleAware.
 *
 * Revision 1.8  2004/12/06 03:34:57  svieujot
 * Typo
 *
 * Revision 1.7  2004/12/06 01:03:42  svieujot
 * Bugfix : getter now use boolean instead of Boolean, and setters created.
 *
 * Revision 1.6  2004/12/04 22:25:36  svieujot
 * *** empty log message ***
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
public class HtmlEditor extends HtmlInputText {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlEditor";

    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.HtmlEditor";
    
    private static final Log log = LogFactory.getLog(HtmlInputText.class);
    
    private String _style;
    private String _styleClass;
    
    private String _type;
    
    private Boolean _allowEditSource;
    private Boolean _addKupuLogo;
    
    private Boolean _showPropertiesToolBox;
    private Boolean _showLinksToolBox;
    private Boolean _showImagesToolBox;
    private Boolean _showTablesToolBox;
    private Boolean _showDebugToolBox;

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
        
        values[2] = _type;
        
        Boolean toolBarButtons[] = new Boolean[2];
        toolBarButtons[0] = _allowEditSource;
        toolBarButtons[1] = _addKupuLogo;
        
        values[3] = toolBarButtons;
        
        Boolean toolBoxes[] = new Boolean[5];
        toolBoxes[0] = _showPropertiesToolBox;
        toolBoxes[1] = _showLinksToolBox;
        toolBoxes[2] = _showImagesToolBox;
        toolBoxes[3] = _showTablesToolBox;
        toolBoxes[4] = _showDebugToolBox;
        
        values[4] = toolBoxes;
        
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        
        String[] display = (String[]) values[1];
        _style = display[0];
        _styleClass = display[1];
        
        _type = (String) values[2];
        
        Boolean[] toolBarButtons = (Boolean[]) values[3];
        _allowEditSource = toolBarButtons[0];
        _addKupuLogo = toolBarButtons[1];
        
        Boolean[] toolBoxes = (Boolean[]) values[4];
        _showPropertiesToolBox = toolBoxes[0];
        _showLinksToolBox = toolBoxes[1];
        _showImagesToolBox = toolBoxes[2];
        _showTablesToolBox = toolBoxes[3];
        _showDebugToolBox = toolBoxes[4];
    }
    
    public String getStyle(){
   		if (_style != null)
   		    return _style;
    	ValueBinding vb = getValueBinding("style");
   		return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    public void setStyle(String style){
   		this._style = style;
    }
    
    public String getStyleClass(){
   		if (_styleClass != null)
   		    return _styleClass;
    	ValueBinding vb = getValueBinding("styleClass");
   		return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    public void setStyleClass(String styleClass){
   		this._styleClass = styleClass;
    }
    
    public String getType(){
        if (_type != null)
            return _type;
        ValueBinding vb = getValueBinding("type");
        return vb != null ? (String)vb.getValue(getFacesContext()) : "fragment";
    }
    public void setType(String _type){
        this._type = _type;
    }
    public boolean isTypeDocument(){
        return getType().equals("document");
    }
    
    public Boolean isAllowEditSource(){
   		if (_allowEditSource != null)
   		    return _allowEditSource;
   		ValueBinding vb = getValueBinding("allowEditSource");
   		return vb != null ? (Boolean)vb.getValue(getFacesContext()) : Boolean.TRUE;
    }
    
    public boolean isAddKupuLogo(){
   		if (_addKupuLogo != null)
   		    return _addKupuLogo.booleanValue();
   		ValueBinding vb = getValueBinding("addKupuLogo");
   		return vb != null ? ((Boolean)vb.getValue(getFacesContext())).booleanValue() : true;
    }
    public void setAddKupuLogo(boolean addKupuLogo){
        this._addKupuLogo = Boolean.valueOf(addKupuLogo);
    }
    
    public boolean isShowPropertiesToolBox(){
   		if (_showPropertiesToolBox != null)
   		    return _showPropertiesToolBox.booleanValue();
    	ValueBinding vb = getValueBinding("showPropertiesToolBox");
    	return vb != null ? ((Boolean)vb.getValue(getFacesContext())).booleanValue() : false;
    }
    public void setShowPropertiesToolBox(boolean showPropertiesToolBox){
        this._showPropertiesToolBox = Boolean.valueOf(showPropertiesToolBox);
    }
    
    public boolean isShowLinksToolBox(){
   		if (_showLinksToolBox != null)
   		    return _showLinksToolBox.booleanValue();
    	ValueBinding vb = getValueBinding("showLinksToolBox");
    	return vb != null ? ((Boolean)vb.getValue(getFacesContext())).booleanValue() : false;
    }
    public void setShowLinksToolBox(boolean showLinksToolBox){
        this._showLinksToolBox = Boolean.valueOf(showLinksToolBox);
    }
    
    public boolean isShowImagesToolBox(){
   		if (_showImagesToolBox != null)
   		    return _showImagesToolBox.booleanValue();
    	ValueBinding vb = getValueBinding("showImagesToolBox");
    	return vb != null ? ((Boolean)vb.getValue(getFacesContext())).booleanValue() : false;
    }
    public void setShowImagesToolBox(boolean showImagesToolBox){
        this._showImagesToolBox = Boolean.valueOf(showImagesToolBox);
    }
    
    public boolean isShowTablesToolBox(){
   		if (_showTablesToolBox != null)
   		    return _showTablesToolBox.booleanValue();
    	ValueBinding vb = getValueBinding("showTablesToolBox");
    	return vb != null ? ((Boolean)vb.getValue(getFacesContext())).booleanValue() : false;
    }
    public void setShowTablesToolBox(boolean showTablesToolBox){
        this._showTablesToolBox = Boolean.valueOf(showTablesToolBox);
    }
    
    public boolean isShowDebugToolBox(){
   		if (_showDebugToolBox != null)
   		    return _showDebugToolBox.booleanValue();
    	ValueBinding vb = getValueBinding("showDebugToolBox");
    	return vb != null ? ((Boolean)vb.getValue(getFacesContext())).booleanValue() : false;
    }
    public void setShowDebugToolBox(boolean showTablesToolBox){
        this._showDebugToolBox = Boolean.valueOf(showTablesToolBox);
    }
    
    public boolean isShowAnyToolBox(){
   		return isShowPropertiesToolBox()
   			|| isShowLinksToolBox()
   			|| isShowImagesToolBox()
   			|| isShowTablesToolBox()
   			|| isShowDebugToolBox();
    }

    public String getValueAsHtmlDocument(FacesContext context){
        String val = RendererUtils.getStringValue(context, this);
        if( isHtmlDocument( val ) )
            return val;
        
        return "<html><body>"+(val==null ? "" : val)+"</body></html>";
    }
    
    private static boolean isHtmlDocument(String text){
        if( text == null )
            return false;
        
        if( text.indexOf("<body>")!=-1 || text.indexOf("<body ")!=-1
            || text.indexOf("<BODY>")!=-1 || text.indexOf("<BODY ")!=-1 )
            return true;
        
        return false;
    }
    
    public String getValueFromDocument(String text){
        if( text == null )
            return "";
        
        if( isTypeDocument() )
            return text.trim();
        
        if( !isHtmlDocument(text) )
            return text.trim();
        
        // Extract the fragment from the document.
        String lcText = text.toLowerCase();
        int textLength = lcText.length();
        int bodyStartIndex = 0;
        while(bodyStartIndex < textLength){
            bodyStartIndex = lcText.indexOf("<body");
            if( bodyStartIndex == -1 )
                break; // not found.
            
            bodyStartIndex += 5;
            char c = lcText.charAt(bodyStartIndex);
            if( c=='>' ){
                break;
            }
            
            if( c!=' ' && c!='\t' )
                continue;
            
            bodyStartIndex = lcText.indexOf('>', bodyStartIndex);
        }
        bodyStartIndex++;
        
        int bodyEndIndex = lcText.lastIndexOf("</body>")-1;
        
        if( bodyStartIndex<0 || bodyEndIndex<0
           || bodyStartIndex > bodyEndIndex
           || bodyStartIndex>=textLength || bodyEndIndex>=textLength ){
            log.warn("Couldn't extract HTML body from :\n"+text);
            return text.trim();
        }
        
        return text.substring(bodyStartIndex, bodyEndIndex+1).trim();
    }
}