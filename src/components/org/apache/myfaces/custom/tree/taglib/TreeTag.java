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
package org.apache.myfaces.custom.tree.taglib;

import org.apache.myfaces.custom.tree.UITreeData;
import org.apache.myfaces.custom.tree.HtmlTree;
import org.apache.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import org.apache.myfaces.taglib.UIComponentBodyTagBase;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller </a>
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class TreeTag extends UIComponentTagBase //UIComponentBodyTagBase
{
    private String _value;
    private String _var;
    private String _varNodeToggler;
    
//    private String styleClass;
//    private String rowClasses;
//    private String columnClasses;
//    private String headerClass;
//    private String footerClass;
//    private String nodeClass;
//    private String selectedNodeClass;
//    private String iconClass;
//    private boolean expandRoot;
//    private long expireListeners = HtmlTree.DEFAULT_EXPIRE_LISTENERS;

    public String getComponentType()
    {
        return "org.apache.myfaces.HtmlTree";
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.HtmlTree";
    }
 
    public void setValue(String value)
    {
        _value = value;
    }

    /**
     * @param var The var to set.
     */
    public void setVar(String var)
    {
        _var = var;
    }

    public void setVarNodeToggler(String varNodeToggler)
    {
        _varNodeToggler = varNodeToggler;
    }
        
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        
        FacesContext context = getFacesContext();
        if (_value != null)
        {
            //setValueBinding(component, "value", _value);
            ValueBinding vb = context.getApplication().createValueBinding(_value);
            component.setValueBinding("value", vb);
        }
        
        if (_var != null)
        {
            //setStringProperty(component, "nodeVar", _nodeVar);
            ((HtmlTree)component).setVar(_var);
        }
        
        if (_varNodeToggler != null)
        {
            ((HtmlTree)component).setVarNodeToggler(_varNodeToggler);
        }
    }

//    public String getIconLine()
//    {
//        return iconLine;
//    }
//
//    public void setIconLine(String iconLine)
//    {
//        this.iconLine = iconLine;
//    }
//
//    public String getIconNoline()
//    {
//        return iconNoline;
//    }
//
//    public void setIconNoline(String iconNoline)
//    {
//        this.iconNoline = iconNoline;
//    }
//
//    /**
//     * @return Returns the rowClasses.
//     */
//    public String getRowClasses()
//    {
//        return rowClasses;
//    }
//
//    /**
//     * @param rowClasses The rowClasses to set.
//     */
//    public void setRowClasses(String rowClasses)
//    {
//        this.rowClasses = rowClasses;
//    }
//
//    /**
//     * @return Returns the columnClasses.
//     */
//    public String getColumnClasses()
//    {
//        return columnClasses;
//    }
//
//    /**
//     * @param columnClasses The columnClasses to set.
//     */
//    public void setColumnClasses(String columnClasses)
//    {
//        this.columnClasses = columnClasses;
//    }
//
//    public String getIconChildFirst()
//    {
//        return iconChildFirst;
//    }
//
//    public void setIconChildFirst(String iconChildFirst)
//    {
//        this.iconChildFirst = iconChildFirst;
//    }
//
//    public String getIconChildMiddle()
//    {
//        return iconChildMiddle;
//    }
//
//    public void setIconChildMiddle(String iconChildMiddle)
//    {
//        this.iconChildMiddle = iconChildMiddle;
//    }
//
//    public String getIconChildLast()
//    {
//        return iconChildLast;
//    }
//
//    public void setIconChildLast(String iconChildLast)
//    {
//        this.iconChildLast = iconChildLast;
//    }
//
//    public String getIconNodeOpen()
//    {
//        return iconNodeOpen;
//    }
//
//    public void setIconNodeOpen(String iconNodeOpen)
//    {
//        this.iconNodeOpen = iconNodeOpen;
//    }
//
//    public String getIconNodeOpenFirst()
//    {
//        return iconNodeOpenFirst;
//    }
//
//    public void setIconNodeOpenFirst(String iconNodeOpenFirst)
//    {
//        this.iconNodeOpenFirst = iconNodeOpenFirst;
//    }
//
//    public String getIconNodeOpenMiddle()
//    {
//        return iconNodeOpenMiddle;
//    }
//
//    public void setIconNodeOpenMiddle(String iconNodeOpenMiddle)
//    {
//        this.iconNodeOpenMiddle = iconNodeOpenMiddle;
//    }
//
//    public String getIconNodeOpenLast()
//    {
//        return iconNodeOpenLast;
//    }
//
//    public void setIconNodeOpenLast(String iconNodeOpenLast)
//    {
//        this.iconNodeOpenLast = iconNodeOpenLast;
//    }
//
//    public String getIconNodeClose()
//    {
//        return iconNodeClose;
//    }
//
//    public void setIconNodeClose(String iconNodeClose)
//    {
//        this.iconNodeClose = iconNodeClose;
//    }
//
//    public String getIconNodeCloseFirst()
//    {
//        return iconNodeCloseFirst;
//    }
//
//    public void setIconNodeCloseFirst(String iconNodeCloseFirst)
//    {
//        this.iconNodeCloseFirst = iconNodeCloseFirst;
//    }
//
//    public String getIconNodeCloseMiddle()
//    {
//        return iconNodeCloseMiddle;
//    }
//
//    public void setIconNodeCloseMiddle(String iconNodeCloseMiddle)
//    {
//        this.iconNodeCloseMiddle = iconNodeCloseMiddle;
//    }
//
//    public String getIconNodeCloseLast()
//    {
//        return iconNodeCloseLast;
//    }
//
//    public void setIconNodeCloseLast(String iconNodeCloseLast)
//    {
//        this.iconNodeCloseLast = iconNodeCloseLast;
//    }
//
//    public String getStyleClass()
//    {
//        return styleClass;
//    }
//
//    public void setStyleClass(String styleClass)
//    {
//        this.styleClass = styleClass;
//    }
//
//    public String getNodeClass()
//    {
//        return nodeClass;
//    }
//
//    public void setNodeClass(String nodeClass)
//    {
//        this.nodeClass = nodeClass;
//    }
//
//    public String getSelectedNodeClass()
//    {
//        return selectedNodeClass;
//    }
//
//    public void setSelectedNodeClass(String selectedNodeClass)
//    {
//        this.selectedNodeClass = selectedNodeClass;
//    }
//
//    public String getIconClass()
//    {
//        return iconClass;
//    }
//
//    public void setIconClass(String iconClass)
//    {
//        this.iconClass = iconClass;
//    }
//
//    /**
//     * @return Returns the footerClass.
//     */
//    public String getFooterClass()
//    {
//        return footerClass;
//    }
//
//    /**
//     * @param footerClass The footerClass to set.
//     */
//    public void setFooterClass(String footerClass)
//    {
//        this.footerClass = footerClass;
//    }
//
//    /**
//     * @return Returns the headerClass.
//     */
//    public String getHeaderClass()
//    {
//        return headerClass;
//    }
//
//    /**
//     * @param headerClass The headerClass to set.
//     */
//    public void setHeaderClass(String headerClass)
//    {
//        this.headerClass = headerClass;
//    }
//
//    public boolean isExpandRoot()
//    {
//        return expandRoot;
//    }
//
//    public void setExpandRoot(boolean expandRoot)
//    {
//        this.expandRoot = expandRoot;
//    }
//
//    public long getExpireListeners()
//    {
//        return expireListeners;
//    }
//
//    public void setExpireListeners(long expireListeners)
//    {
//        this.expireListeners = expireListeners;
//    }
//
//    /**
//     * Obtain tree model or create a default model.
//     */
//    public int doStartTag() throws JspException
//    {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (value != null)
//        {
//            ValueBinding valueBinding = context.getApplication().createValueBinding(value);
////            TreeModel treeModel = (TreeModel) (valueBinding.getValue(context));
//
////            if (treeModel == null)
////            {
////                // create default model
////                treeModel = new DefaultTreeModel();
////                valueBinding.setValue(context, treeModel);
////            }
//        }
//        int answer = super.doStartTag();
//        HtmlTree tree = (HtmlTree) getComponentInstance();
//
//        if (getCreated() && expandRoot)
//        {
//            // component was created, so expand the root node
//            TreeModel model = tree.getModel(context);
//
//            if (model != null)
//            {
//                tree.expandPath(new TreePath(new Object[] { model.getRoot() }), context);
//            }
//        }
//
//        tree.addToModelListeners();
//        return answer;
//    }

    /**
      * Applies attributes to the tree component
      */
//     protected void setProperties(UIComponent component)
//     {
//         super.setProperties(component);
//         FacesContext context = FacesContext.getCurrentInstance();
// 
//         if (value != null)
//         {
//             if (isValueReference(value))
//             {
//                 ValueBinding binding = context.getApplication().createValueBinding(value);
//                 component.setValueBinding("model", binding);
//             }
//         }
//         else
//         {
//             ValueBinding binding = component.getValueBinding("model");
//             if (binding == null)
//             {
//                 binding = context.getApplication().createValueBinding("#{sessionScope.tree}");
//             }
//             component.setValueBinding("model", binding);
//         }
 
//         setStringProperty(component, "var", var);
//         setStringProperty(component, "iconLine", iconLine);
//         setStringProperty(component, "iconNoline", iconNoline);
//         setStringProperty(component, "iconChildFirst", iconChildFirst);
//         setStringProperty(component, "iconChildMiddle", iconChildMiddle);
//         setStringProperty(component, "iconChildLast", iconChildLast);
//         setStringProperty(component, "iconNodeOpen", iconNodeOpen);
//         setStringProperty(component, "iconNodeOpenFirst", iconNodeOpenFirst);
//         setStringProperty(component, "iconNodeOpenMiddle", iconNodeOpenMiddle);
//         setStringProperty(component, "iconNodeOpenLast", iconNodeOpenLast);
//         setStringProperty(component, "iconNodeClose", iconNodeClose);
//         setStringProperty(component, "iconNodeCloseFirst", iconNodeCloseFirst);
//         setStringProperty(component, "iconNodeCloseMiddle", iconNodeCloseMiddle);
//         setStringProperty(component, "iconNodeCloseLast", iconNodeCloseLast);
//         setStringProperty(component, "styleClass", styleClass);
//         setStringProperty(component, "rowClasses", rowClasses);
//         setStringProperty(component, "columnClasses", columnClasses);
//         setStringProperty(component, "headerClass", headerClass);
//         setStringProperty(component, "footerClass", footerClass);
//         setStringProperty(component, "nodeClass", nodeClass);
//         setStringProperty(component, "selectedNodeClass", selectedNodeClass);
//         setStringProperty(component, "iconClass", iconClass);
//         ((HtmlTree) component).setExpireListeners(expireListeners);
//    }    
}
