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
package net.sourceforge.myfaces.application;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class MyfacesStateManager
        extends StateManager
{
    /**
     * Writes hidden form inputs with the state info to be saved.
     * For every form state marker this method is called once from
     * {@link net.sourceforge.myfaces.taglib.core.ViewTag#doAfterBody()}.
     */
    public abstract void writeState(FacesContext facesContext,
                                    SerializedView serializedView) throws IOException;

    /**
     * Writes url parameters with the state info to be saved.
     * For every url state marker this method is called once from
     * {@link net.sourceforge.myfaces.taglib.core.ViewTag#doAfterBody()}.
     */
    public abstract void writeStateAsUrlParams(FacesContext facesContext,
                                               SerializedView serializedView) throws IOException;
}
