package org.apache.myfaces.custom.tree2;

import java.util.List;
import java.util.ArrayList;


public class TreeNodeBase implements TreeNode
{

    private ArrayList children = new ArrayList();
    private String type;
    private String description;
    private boolean leaf;
    private String identifier;
    private boolean expanded;


    public TreeNodeBase()
    {
    }


    public TreeNodeBase(String type, String description, boolean leaf)
    {
        this(type, description, null, leaf);
    }


    public TreeNodeBase(String type, String description, String identifier, boolean leaf)
    {
        this.type = type;
        this.description = description;
        this.identifier = identifier;
        this.leaf = leaf;
    }


    public boolean isLeaf()
    {
        return leaf;
    }


    public void setLeaf(boolean leaf)
    {
        this.leaf = leaf;
    }


    public List getChildren()
    {
        return children;
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return description;
    }


    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    public String getIdentifier()
    {
        return identifier;
    }


    public int getChildCount()
    {
        return getChildren().size();
    }
}
