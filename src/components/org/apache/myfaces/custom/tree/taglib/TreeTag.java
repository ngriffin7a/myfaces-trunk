/*
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


package net.sourceforge.myfaces.custom.tree.taglib;

import net.sourceforge.myfaces.custom.tree.model.DefaultTreeModel;
import net.sourceforge.myfaces.custom.tree.model.TreeModel;
import net.sourceforge.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;


/**
 * HtmlTree tag.
 * 
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:25  manolito
 *          tree component
 *
 */
public class TreeTag
        extends UIComponentTagBase
{

    private String value;
    private String iconLine;
    private String iconNoline;
    private String iconChild;
    private String iconChildFirst;
    private String iconChildMiddle;
    private String iconChildLast;
    private String iconNodeOpen;
    private String iconNodeOpenFirst;
    private String iconNodeOpenMiddle;
    private String iconNodeOpenLast;
    private String iconNodeClose;
    private String iconNodeCloseFirst;
    private String iconNodeCloseMiddle;
    private String iconNodeCloseLast;
    private String styleClass;
    private String nodeClass;
    private String selectedNodeClass;


    public String getComponentType()
    {
        return "net.sourceforge.myfaces.HtmlTree";
    }


    public String getRendererType()
    {
        return "net.sourceforge.myfaces.HtmlTree";
    }


    public String getValue()
    {
        return value;
    }


    public void setValue(String newValue)
    {
        value = newValue;
    }


    public String getIconLine()
    {
        return iconLine;
    }


    public void setIconLine(String iconLine)
    {
        this.iconLine = iconLine;
    }


    public String getIconNoline()
    {
        return iconNoline;
    }


    public void setIconNoline(String iconNoline)
    {
        this.iconNoline = iconNoline;
    }


    public String getIconChild()
    {
        return iconChild;
    }


    public void setIconChild(String iconChild)
    {
        this.iconChild = iconChild;
    }


    public String getIconChildFirst()
    {
        return iconChildFirst;
    }


    public void setIconChildFirst(String iconChildFirst)
    {
        this.iconChildFirst = iconChildFirst;
    }


    public String getIconChildMiddle()
    {
        return iconChildMiddle;
    }


    public void setIconChildMiddle(String iconChildMiddle)
    {
        this.iconChildMiddle = iconChildMiddle;
    }


    public String getIconChildLast()
    {
        return iconChildLast;
    }


    public void setIconChildLast(String iconChildLast)
    {
        this.iconChildLast = iconChildLast;
    }


    public String getIconNodeOpen()
    {
        return iconNodeOpen;
    }


    public void setIconNodeOpen(String iconNodeOpen)
    {
        this.iconNodeOpen = iconNodeOpen;
    }


    public String getIconNodeOpenFirst()
    {
        return iconNodeOpenFirst;
    }


    public void setIconNodeOpenFirst(String iconNodeOpenFirst)
    {
        this.iconNodeOpenFirst = iconNodeOpenFirst;
    }


    public String getIconNodeOpenMiddle()
    {
        return iconNodeOpenMiddle;
    }


    public void setIconNodeOpenMiddle(String iconNodeOpenMiddle)
    {
        this.iconNodeOpenMiddle = iconNodeOpenMiddle;
    }


    public String getIconNodeOpenLast()
    {
        return iconNodeOpenLast;
    }


    public void setIconNodeOpenLast(String iconNodeOpenLast)
    {
        this.iconNodeOpenLast = iconNodeOpenLast;
    }


    public String getIconNodeClose()
    {
        return iconNodeClose;
    }


    public void setIconNodeClose(String iconNodeClose)
    {
        this.iconNodeClose = iconNodeClose;
    }


    public String getIconNodeCloseFirst()
    {
        return iconNodeCloseFirst;
    }


    public void setIconNodeCloseFirst(String iconNodeCloseFirst)
    {
        this.iconNodeCloseFirst = iconNodeCloseFirst;
    }


    public String getIconNodeCloseMiddle()
    {
        return iconNodeCloseMiddle;
    }


    public void setIconNodeCloseMiddle(String iconNodeCloseMiddle)
    {
        this.iconNodeCloseMiddle = iconNodeCloseMiddle;
    }


    public String getIconNodeCloseLast()
    {
        return iconNodeCloseLast;
    }


    public void setIconNodeCloseLast(String iconNodeCloseLast)
    {
        this.iconNodeCloseLast = iconNodeCloseLast;
    }


    public String getStyleClass()
    {
        return styleClass;
    }


    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }


    public String getNodeClass()
    {
        return nodeClass;
    }


    public void setNodeClass(String nodeClass)
    {
        this.nodeClass = nodeClass;
    }


    public String getSelectedNodeClass()
    {
        return selectedNodeClass;
    }


    public void setSelectedNodeClass(String selectedNodeClass)
    {
        this.selectedNodeClass = selectedNodeClass;
    }


    /**
     * Obtain tree model or create a default model.
     */
    public int doStartTag() throws JspException
    {
        if (value != null)
        {
            FacesContext context = FacesContext.getCurrentInstance();
            ValueBinding valueBinding = context.getApplication().createValueBinding(value);
            TreeModel treeModel = (TreeModel)(valueBinding.getValue(context));

            if (treeModel == null)
            {
                // create default model
                treeModel = new DefaultTreeModel();
                valueBinding.setValue(context, treeModel);
            }
        }
        return super.doStartTag();
    }


    /**
     * Applies attributes to the tree component
     */
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        FacesContext context = FacesContext.getCurrentInstance();

        if (value != null)
        {
            if (isValueReference(value))
            {
                ValueBinding binding = context.getApplication().createValueBinding(value);
                component.setValueBinding("model", binding);
            }
        }
        else
        {
            ValueBinding binding = component.getValueBinding("model");
            if (binding == null)
            {
                binding = context.getApplication().createValueBinding("#{sessionScope.tree}");
            }
            component.setValueBinding("model", binding);
        }

        setStringProperty(component, "iconLine", iconLine);
        setStringProperty(component, "iconNoline", iconNoline);
        setStringProperty(component, "iconChild", iconChild);
        setStringProperty(component, "iconChildFirst", iconChildFirst);
        setStringProperty(component, "iconChildMiddle", iconChildMiddle);
        setStringProperty(component, "iconChildLast", iconChildLast);
        setStringProperty(component, "iconNodeOpen", iconNodeOpen);
        setStringProperty(component, "iconNodeOpenFirst", iconNodeOpenFirst);
        setStringProperty(component, "iconNodeOpenMiddle", iconNodeOpenMiddle);
        setStringProperty(component, "iconNodeOpenLast", iconNodeOpenLast);
        setStringProperty(component, "iconNodeClose", iconNodeClose);
        setStringProperty(component, "iconNodeCloseFirst", iconNodeCloseFirst);
        setStringProperty(component, "iconNodeCloseMiddle", iconNodeCloseMiddle);
        setStringProperty(component, "iconNodeCloseLast", iconNodeCloseLast);
        setStringProperty(component, "styleClass", styleClass);
        setStringProperty(component, "nodeClass", nodeClass);
        setStringProperty(component, "selectedNodeClass", selectedNodeClass);
    }
}
