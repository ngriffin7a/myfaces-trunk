/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.config;

import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FactoryConfig
    implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private ApplicationFactory  _applicationFactory;
    private FacesContextFactory _facesContextFactory;
    private LifecycleFactory    _lifecycleFactory;
    private RenderKitFactory    _renderKitFactory;

    //~ Methods ------------------------------------------------------------------------------------

    public void setApplicationFactory(ApplicationFactory applicationFactory)
    {
        _applicationFactory = applicationFactory;
    }

    public ApplicationFactory getApplicationFactory()
    {
        return _applicationFactory;
    }

    public void setFacesContextFactory(FacesContextFactory facesContextFactory)
    {
        _facesContextFactory = facesContextFactory;
    }

    public FacesContextFactory getFacesContextFactory()
    {
        return _facesContextFactory;
    }

    public void setLifecycleFactory(LifecycleFactory lifecycleFactory)
    {
        _lifecycleFactory = lifecycleFactory;
    }

    public LifecycleFactory getLifecycleFactory()
    {
        return _lifecycleFactory;
    }

    public void setRenderKitFactory(RenderKitFactory renderKitFactory)
    {
        _renderKitFactory = renderKitFactory;
    }

    public RenderKitFactory getRenderKitFactory()
    {
        return _renderKitFactory;
    }

    public void update(FactoryConfig factoryConfig)
    {
        if (factoryConfig._applicationFactory != null)
        {
            _applicationFactory = factoryConfig._applicationFactory;
        }

        if (factoryConfig._facesContextFactory != null)
        {
            _facesContextFactory = factoryConfig._facesContextFactory;
        }

        if (factoryConfig._lifecycleFactory != null)
        {
            _lifecycleFactory = factoryConfig._lifecycleFactory;
        }

        if (factoryConfig._renderKitFactory != null)
        {
            _renderKitFactory = factoryConfig._renderKitFactory;
        }
    }
}
