/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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
package net.sourceforge.myfaces.tree;

import net.sourceforge.myfaces.component.UIRoot;

import javax.faces.component.UIComponent;
import javax.faces.render.RenderKitFactory;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class TreeImpl
        extends Tree
{
    private ServletContext _servletContext;
    private String _treeId;
    private String _renderKitId;
    private UIComponent _root;

    public TreeImpl(ServletContext servletContext, String treeId)
    {
        _servletContext = servletContext;
        _treeId = treeId;
        setRenderKitId(RenderKitFactory.DEFAULT_RENDER_KIT);
        _root = new UIRoot();
    }

    public String getTreeId()
    {
        return _treeId;
    }

    public UIComponent getRoot()
    {
        return _root;
    }

    public void setRenderKitId(String s)
    {
        _renderKitId = s;
    }

    public String getRenderKitId()
    {
        return _renderKitId;
    }

}
