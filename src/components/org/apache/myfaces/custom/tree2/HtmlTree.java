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
package org.apache.myfaces.custom.tree2;

import javax.faces.context.FacesContext;
import javax.faces.component.UICommand;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
//import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.renderkit.JSFAttr;

import java.util.HashSet;
import java.util.Map;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller </a>
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class HtmlTree extends UITreeData
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.Tree2";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Tree2";
//    private static final String NAV_COMMAND_ID = "NAV_COMMAND_ID";
    private UICommand expandControl;
    private String _varNodeToggler;
    private HashSet _expandedNodes = new HashSet();

    /**
     * Constructor
     */
    public HtmlTree()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);

        expandControl = new HtmlCommandLink();
        //getChildren().add(expandControl);
//        expandControl.addActionListener(new NodeTogglerListener(this));
//        expandControl.setImmediate(true);
//        expandControl.setId(NAV_COMMAND_ID);
//        Map expandAttr = expandControl.getAttributes();
//        expandAttr.put(JSFAttr.FORCE_ID_ATTR, "true");
    }

//    private static class NodeTogglerListener implements ActionListener
//    {
//        private HtmlTree tree;
//
//        public NodeTogglerListener(HtmlTree tree)
//        {
//            this.tree = tree;
//        }
//        public void processAction(ActionEvent e)
//        {
//            tree.toggleExpanded();
//        }
//    }

//    public void decode(FacesContext context)
//    {
//        System.out.println("decoding");
//    }

//    protected void processNodes(FacesContext context, int processAction, String parentId, int childLevel)
//    {
//        super.processNodes(context, processAction, parentId, childLevel);
//
//    }

    // see superclass for documentation
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _expandedNodes;
        values[2] = _varNodeToggler;
        //values[3] = expandControl.saveState(context);

        return ((Object) (values));
    }

    // see superclass for documentation
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _expandedNodes = (HashSet)values[1];
        setVarNodeToggler((String)values[2]);
        //expandControl = (UICommand)values[3];
        //_varNodeToggler = (String)values[2];
    }

    // see superclass for documentation
    public void setNodeId(String nodeId)
    {
        super.setNodeId(nodeId);

        if (_varNodeToggler != null)
        {
            Map requestMap = getFacesContext().getExternalContext().getRequestMap();
            requestMap.put(_varNodeToggler, this);
        }
    }

    public UICommand getExpandControl()
    {
        return expandControl;
    }

    public void setVarNodeToggler(String varNodeToggler)
    {
        _varNodeToggler = varNodeToggler;

        // create a method binding for the expand control
        String bindingString = "#{" + varNodeToggler + ".toggleExpanded}";
        MethodBinding actionBinding = FacesContext.getCurrentInstance().getApplication().createMethodBinding(bindingString, null);
        expandControl.setAction(actionBinding);
    }

    public String toggleExpanded()
    {
        String nodeId = getNodeId();

        if (_expandedNodes.contains(nodeId))
        {
            _expandedNodes.remove(nodeId);
        }
        else
        {
            _expandedNodes.add(nodeId);
        }

        return null;
    }

    /**
     * Indicates whether or not the current {@link TreeNode} is expanded.
     * @return boolean
     */
    public boolean isNodeExpanded()
    {
        return (_expandedNodes.contains(getNodeId()) && getNode().getChildCount() > 0);
    }

    protected void processChildNodes(FacesContext context, TreeNode parentNode, int processAction)
    {
        String parentNodeId = getNodeId();

        if (_expandedNodes.contains(parentNodeId))
        {
            super.processChildNodes(context, parentNode, processAction);
        }
    }
}
