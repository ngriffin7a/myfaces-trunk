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

import java.util.StringTokenizer;
import javax.faces.component.NamingContainer;

/**
 * Model class for the tree component.  It provides random access to nodes in a tree
 * made up of instances of the {@link TreeNode} class.
 * 
 * @author Sean Schofield
 * @author Hans Bergsten (Some code taken from an example in his O'Reilly JavaServer Faces book. Copied with permission)
 * @version $Revision$ $Date$
 */
public class TreeModel 
{
    private final static String SEPARATOR = String.valueOf(NamingContainer.SEPARATOR_CHAR);
    
    private TreeNode root;
    private TreeNode currentNode;
    
    /**
     * Constructor 
     * @param root The root TreeNode
     */
    public TreeModel(TreeNode root) 
    {
        this.root = root;
    }
    
    /**
     * Gets the current {@link TreeNode} or <code>null</code> if no node ID is selected.
     * @return The current node
     */
    public TreeNode getNode() 
    {
        return currentNode;    
    }
    
    /**
     * Sets the current {@link TreeNode} to the specified node ID, which is a colon-separated list 
     * of node indexes.  For instance, "0:0:1" means "the second child node of the first child node 
     * under the root node."
     * 
     * @param nodeId The id of the node to set
     */
    public void setNodeId(String nodeId) 
    {
        if (nodeId == null)
        {
            currentNode = null;
            return;
        }
        
        TreeNode node = root;

        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(nodeId, SEPARATOR);
        sb.append(st.nextToken()).append(SEPARATOR);

        while (st.hasMoreTokens())
        {
            int nodeIndex = Integer.parseInt(st.nextToken());
            sb.append(nodeIndex);
            
            try 
            {
                node = (TreeNode)node.getChildren().get(nodeIndex);
            }
            catch (IndexOutOfBoundsException e) 
            {
                String msg = "Node with id " + sb.toString() + ". Failed to parse " + nodeId;
                throw new IllegalArgumentException(msg);
            }
            sb.append(SEPARATOR);
        }
        
        currentNode = node;
    }
}
