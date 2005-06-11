package org.apache.myfaces.custom.inputHtmlHelp;

import org.apache.myfaces.taglib.html.HtmlInputTextTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Obereder
 * @version Date: 09.06.2005, 22:16:41
 */
public class HtmlInputTextHelpTag extends HtmlInputTextTagBase
{
    private String _helpText = null;
    private String _selectText = null;
    private String _addResources = null;

    public String getComponentType()
    {
        return HtmlInputTextHelp.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.TextHelp";
    }

    public void release()
    {
        super.release();

        _helpText = null;
        _selectText = null;
        _addResources = null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        
        setStringProperty(component, "helpText", _helpText);
        setBooleanProperty(component, "selectText", _selectText);
        setBooleanProperty(component, "addResources", _addResources);
    }

    public void setHelpText(String helpText)
    {
        _helpText = helpText;
    }
    public void setSelectText(String selectText)
    {
        _selectText = selectText;
    }
    public void setAddResources(String addResources)
    {
        _addResources = addResources;
    }
}
