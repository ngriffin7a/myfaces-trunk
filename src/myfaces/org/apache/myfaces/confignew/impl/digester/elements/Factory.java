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

import java.util.List;
import java.util.ArrayList;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class Factory
{

    private List applicationFactory = new ArrayList();
    private List facesContextFactory = new ArrayList();
    private List lifecycleFactory = new ArrayList();
    private List renderkitFactory = new ArrayList();

    public void addApplicationFactory(String factory)
    {
        applicationFactory.add(factory);
    }


    public void addFacesContextFactory(String factory)
    {
        facesContextFactory.add(factory);
    }


    public void addLifecycleFactory(String factory)
    {
        lifecycleFactory.add(factory);
    }


    public void addRenderkitFactory(String factory)
    {
        renderkitFactory.add(factory);
    }


    public List getApplicationFactory()
    {
        return applicationFactory;
    }


    public List getFacesContextFactory()
    {
        return facesContextFactory;
    }


    public List getLifecycleFactory()
    {
        return lifecycleFactory;
    }


    public List getRenderkitFactory()
    {
        return renderkitFactory;
    }
}
