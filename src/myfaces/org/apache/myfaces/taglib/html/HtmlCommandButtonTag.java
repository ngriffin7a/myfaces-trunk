/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.taglib.html;

import net.sourceforge.myfaces.el.SimpleActionMethodBinding;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 */
public class HtmlCommandButtonTag
    extends HtmlComponentTag
{
    private static final Log log = LogFactory.getLog(HtmlCommandButtonTag.class);

    public String getComponentType()
    {
        return "javax.faces.CommandButton";
    }

    public String getDefaultRendererType()
    {
        return "javax.faces.Button";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // user role attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // HTML input attributes relevant for command-button
    private String _accesskey;
    private String _alt;
    private String _disabled;
    private String _onblur;
    private String _onchange;
    private String _onfocus;
    private String _onselect;
    private String _size;
    private String _tabindex;
    private String _type;

    // UICommand attributes
    private String _action;
    private String _immediate;
    private String _actionListener;

    // HtmlCommandButton attributes
    private String _image;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ACCESSKEY_ATTR, _accesskey);
        setStringProperty(component, HTML.ALT_ATTR, _alt);
        setBooleanProperty(component, HTML.DISABLED_ATTR, _disabled);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, HTML.ONCHANGE_ATTR, _onchange);
        setStringProperty(component, HTML.ONFOCUS_ATTR, _onfocus);
        setStringProperty(component, HTML.ONSELECT_ATTR, _onselect);
        setStringProperty(component, HTML.SIZE_ATTR, _size);
        setIntegerProperty(component, HTML.TABINDEX_ATTR, _tabindex);
        setStringProperty(component, HTML.TYPE_ATTR, _type);

        if (_action != null)
        {
            MethodBinding mb;
            if (isValueReference(_action))
            {
                mb = getFacesContext().getApplication().createMethodBinding(_action, null);
            }
            else
            {
                mb = new SimpleActionMethodBinding(_action);
            }
            ((UICommand)component).setAction(mb);
        }

        if (_actionListener != null)
        {
            if (isValueReference(_actionListener))
            {
                Class args[] = {javax.faces.event.ActionEvent.class};
                MethodBinding mb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(_actionListener, args);
                ((UICommand)component).setActionListener(mb);
            }
            else
            {
                log.error("Invalid expression " + _actionListener);
            }
        }

        setBooleanProperty(component, JSFAttr.IMMEDIATE_ATTR, _immediate);

        setStringProperty(component, JSFAttr.IMAGE_ATTR, _image);
   }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }

    public void setAlt(String alt)
    {
        _alt = alt;
    }

    public void setDisabled(String disabled)
    {
        _disabled = disabled;
    }

    public void setOnblur(String onblur)
    {
        _onblur = onblur;
    }

    public void setOnchange(String onchange)
    {
        _onchange = onchange;
    }

    public void setOnfocus(String onfocus)
    {
        _onfocus = onfocus;
    }

    public void setOnselect(String onselect)
    {
        _onselect = onselect;
    }

    public void setSize(String size)
    {
        _size = size;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }

    public void setType(String type)
    {
        _type = type;
    }

    public void setAction(String action)
    {
        _action = action;
    }

    public void setImmediate(String immediate)
    {
        _immediate = immediate;
    }

    public void setImage(String image)
    {
        _image = image;
    }

    public void setActionListener(String actionListener)
    {
        _actionListener = actionListener;
    }
}
