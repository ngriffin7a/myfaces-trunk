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

package net.sourceforge.myfaces.confignew.impl.digester.elements;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class RenderKit
{

    private String id;
    private String renderKitClass;
    private List renderer = new ArrayList();


    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getRenderKitClass()
    {
        return renderKitClass;
    }


    public void setRenderKitClass(String renderKitClass)
    {
        this.renderKitClass = renderKitClass;
    }


    public List getRenderer()
    {
        return renderer;
    }


    public void addRenderer(Renderer value)
    {
        renderer.add(value);
    }


    public void merge(RenderKit renderKit)
    {
        renderer.addAll(renderKit.getRenderer());
    }

}
