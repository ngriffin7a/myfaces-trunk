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
package org.apache.myfaces.taglib;

import org.apache.myfaces.renderkit.JSFAttr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.14  2005/01/30 15:24:10  matzew
 * thanks to sean schofield for removing *legacy* attributes of MyFaces
 *
 * Revision 1.13  2005/01/28 17:19:09  matzew
 * Patch for MYFACES-91 form Sean Schofield
 *
 * Revision 1.12  2005/01/25 22:15:53  matzew
 * JavaDoc patch form Sean Schofield
 *
 * Revision 1.11  2005/01/10 08:08:12  matzew
 * added patch form sean schofield. forceId for reuse of "legacy JavaScript" (MyFaces-70)
 *
 * Revision 1.10  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.9  2004/07/01 22:01:21  mwessendorf
 * ASF switch
 *
 * Revision 1.8  2004/04/16 15:13:33  manolito
 * validator attribute support and MethodBinding invoke exception handling fixed
 *
 * Revision 1.7  2004/04/05 11:04:56  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.6  2004/04/01 13:18:39  manolito
 * warning message
 *
 * Revision 1.5  2004/04/01 12:57:44  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.4  2004/04/01 09:33:43  manolito
 * user role support removed
 *
 * Revision 1.3  2004/03/31 11:14:28  royalts
 * no message
 *
 * Revision 1.2  2004/03/30 12:16:08  manolito
 * header comments
 *
 */
public abstract class UIComponentBodyTagBase
        extends UIComponentBodyTag
{
    private static final Log log = LogFactory.getLog(UIComponentBodyTagBase.class);

    public int doEndTag() throws JspException
    {
        if (log.isWarnEnabled())
        {
            UIComponent component = getComponentInstance();
            if (component != null &&
                component.getRendersChildren() &&
                !isBodyContentEmpty())
            {
                log.warn("Component with id '" + component.getClientId(getFacesContext()) +
                         "' (" + getClass().getName() +
                         " tag) renders it's children, but has embedded JSP or HTML code. Use the <f:verbatim> tag for nested HTML. For comments use <%/* */%> style JSP comments instead of <!-- --> style HTML comments." +
                         "\n BodyContent:\n" + getBodyContent().getString().trim());
            }
        }
        return super.doEndTag();
    }

    /**
     * TODO: Ignore <!-- --> comments
     */ 
    private boolean isBodyContentEmpty()
    {
        BodyContent bodyContent = getBodyContent();
        if (bodyContent == null)
        {
            return true;
        }
        try
        {
            Reader reader = bodyContent.getReader();
            int c;
            while ((c = reader.read()) != -1)
            {
                if (!Character.isWhitespace((char)c))
                {
                    return false;
                }
            }
            return true;
        }
        catch (IOException e)
        {
            log.error("Error inspecting BodyContent", e);
            return false;
        }
    }

    //-------- rest is identical to UIComponentTagBase ------------------

    //UIComponent attributes
    private String _forceId;
    private String _forceIdIndex = "true";

    //Special UIComponent attributes (ValueHolder, ConvertibleValueHolder)
    private String _value;
    private String _converter;
    //attributes id, rendered and binding are handled by UIComponentTag

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, JSFAttr.FORCE_ID_ATTR, _forceId);
        setBooleanProperty(component, JSFAttr.FORCE_ID_INDEX_ATTR, _forceIdIndex);

        //rendererType already handled by UIComponentTag

        setValueProperty(component, _value);
        setConverterProperty(component, _converter);
    }

    /**
     * Sets the forceId attribute of the tag.  NOTE: Not every tag that extends this class will 
     * actually make use of this attribute.  Check the TLD to see which components actually 
     * implement it.
     * 
     * @param aForceId The value of the forceId attribute.
     */
    public void setForceId(String aForceId)
    {
        _forceId = aForceId;
    }

    /**
     * Sets the forceIdIndex attribute of the tag.  NOTE: Not every tag that extends this class will 
     * actually make use of this attribute.  Check the TLD to see which components actually implement it.
     * 
     * @param aForceIdIndex The value of the forceIdIndex attribute.
     */
    public void setForceIdIndex(String aForceIdIndex)
    {
        _forceIdIndex = aForceIdIndex;
    }

    public void setValue(String value)
    {
        _value = value;
    }

    public void setConverter(String converter)
    {
        _converter = converter;
    }



    // sub class helpers

    protected void setIntegerProperty(UIComponent component, String propName, String value)
    {
        UIComponentTagUtils.setIntegerProperty(getFacesContext(), component, propName, value);
    }

    protected void setStringProperty(UIComponent component, String propName, String value)
    {
        UIComponentTagUtils.setStringProperty(getFacesContext(), component, propName, value);
    }

    protected void setBooleanProperty(UIComponent component, String propName, String value)
    {
        UIComponentTagUtils.setBooleanProperty(getFacesContext(), component, propName, value);
    }

    protected void setValueProperty(UIComponent component, String value)
    {
        UIComponentTagUtils.setValueProperty(getFacesContext(), component, value);
    }

    private void setConverterProperty(UIComponent component, String value)
    {
        UIComponentTagUtils.setConverterProperty(getFacesContext(), component, value);
    }

    protected void setValidatorProperty(UIComponent component, String value)
    {
        UIComponentTagUtils.setValidatorProperty(getFacesContext(), component, value);
    }
    
    protected void setActionProperty(UIComponent component, String action)
    {
        UIComponentTagUtils.setActionProperty(getFacesContext(), component, action);
    }

    protected void setActionListenerProperty(UIComponent component, String actionListener)
    {
        UIComponentTagUtils.setActionListenerProperty(getFacesContext(), component, actionListener);
    }

    protected void setValueChangedListenerProperty(UIComponent component, String valueChangedListener)
    {
        UIComponentTagUtils.setValueChangedListenerProperty(getFacesContext(), component, valueChangedListener);
    }

    protected void setValueBinding(UIComponent component,
                                   String propName,
                                   String value)
    {
        UIComponentTagUtils.setValueBinding(getFacesContext(), component, propName, value);
    }

}
