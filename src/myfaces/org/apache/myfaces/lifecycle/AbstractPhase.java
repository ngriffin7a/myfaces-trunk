/**
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
package net.sourceforge.myfaces.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Phase;
import java.util.Collections;
import java.util.Iterator;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class AbstractPhase
        extends Phase
{
    public abstract int execute(FacesContext facescontext) throws FacesException;

    protected int execute(FacesContext facescontext, PhaseAction action) throws FacesException
    {
        UIComponent root = facescontext.getRequestTree().getRoot();
        //Iterator children = root.getChildren();
        Iterator children = Collections.singleton(root).iterator();
        return traverse(facescontext, children, action);
    }

    private int traverse(FacesContext facescontext, Iterator components, PhaseAction action)
    {
        int ret;
        while(components.hasNext())
        {
            UIComponent next = (UIComponent)components.next();
            ret = action.doActionForComponent(facescontext, next);
            if (ret != GOTO_NEXT)
            {
                return ret;
            }

            Iterator children = next.getChildren();
            ret = traverse(facescontext, children, action);
            if (ret != GOTO_NEXT)
            {
                return ret;
            }
        }
        return GOTO_NEXT;
    }

}
