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
package org.apache.myfaces.custom.tree;

import javax.faces.context.FacesContext;

import java.util.HashSet;
import java.util.Map;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller </a>
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class HtmlTree extends UITreeData
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.Tree";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Tree";

    private String _varNodeToggler;
    private HashSet _expandedNodes = new HashSet();

    /**
     * Constructor
     */
    public HtmlTree()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }
    
    // see superclass for documentation
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _expandedNodes;
        values[2] = _varNodeToggler;
        return ((Object) (values));
    }

    // see superclass for documentation
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _expandedNodes = (HashSet)values[1];
        _varNodeToggler = (String)values[2];
    }        
    
//    public TreeNode getNode()
//    {
//        // make determination about whether or not the node is expanded
//        TreeNode node = super.getNode();
//        String nodeId = getNodeId();
//        
//        node.setExpanded(_expandedNodes.contains(nodeId));
//        
//        return node;
//    }
    
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
    
    public void setVarNodeToggler(String varNodeToggler)
    {
        _varNodeToggler = varNodeToggler;
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
        return (_expandedNodes.contains(getNodeId()));
    }
    
    protected void processChildNodes(FacesContext context, TreeNode parentNode, int processAction)
    {
        String parentNodeId = getNodeId();
        
        if (_expandedNodes.contains(parentNodeId))
        {
            super.processChildNodes(context, parentNode, processAction);
        }
//        if (parentNode.isExpanded())
//        {
//            super.processChildNodes(context, parentNode, processAction);
//        }
    }
}
