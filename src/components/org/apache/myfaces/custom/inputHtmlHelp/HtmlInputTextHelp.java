package org.apache.myfaces.custom.inputHtmlHelp;

import org.apache.myfaces.component.html.util.HtmlComponentUtils;

import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/**
 * @author Thomas Obereder
 * @version $Date: 2005-07-02 15:32:34 +01:00 (Thu, 09 Jun 2005)
 */
public class HtmlInputTextHelp extends javax.faces.component.html.HtmlInputText
{
    public static final String JS_FUNCTION_SELECT_TEXT = "selectText";
    public static final String JS_FUNCTION_RESET_HELP = "resetHelpValue";
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlInputTextHelp";

    private String _helpText = null;
    private Boolean _selectText = null;
    private Boolean _addResources = null;

    public String getClientId(FacesContext context)
    {
        String clientId = HtmlComponentUtils.getClientId(this, getRenderer(context), context);
        if (clientId == null)
        {
            clientId = super.getClientId(context);
        }

        return clientId;
    }

    public HtmlInputTextHelp()
    {
    }

    public String getHelpText()
    {
        if(_helpText != null) return _helpText;
        ValueBinding vb = getValueBinding("helpText");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setHelpText(String helpText)
    {
        _helpText = helpText;
    }

    public boolean isSelectText()
    {
        if (_selectText != null) return _selectText.booleanValue();
        ValueBinding vb = getValueBinding("selectText");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setSelectText(boolean selectText)
    {
        _selectText = Boolean.valueOf(selectText);
    }

    public boolean isAddResources()
    {
        if (_addResources != null) return _addResources.booleanValue();
        ValueBinding vb = getValueBinding("addResources");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setAddResources(boolean addResources)
    {
        _addResources = Boolean.valueOf(addResources);
    }

    public Object saveState(FacesContext context)
    {
        Object[] state = new Object[4];
        state[0] = super.saveState(context);
        state[1] = _helpText;
        state[2] = _selectText;
        state[3] = _addResources;
        return state;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _helpText = (String)values[1];
        _selectText = (Boolean)values[2];
        _addResources = (Boolean)values[3];
    }
}
