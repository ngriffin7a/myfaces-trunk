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
package net.sourceforge.myfaces.el;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SimpleActionMethodBinding
        extends MethodBinding
        implements StateHolder
{
    //private static final Log log = LogFactory.getLog(SimpleActionMethodBinding.class);

    private String _outcome;

    public SimpleActionMethodBinding(String outcome)
    {
        _outcome = outcome;
    }

    public Object invoke(FacesContext facescontext, Object aobj[]) throws EvaluationException, MethodNotFoundException
    {
        return _outcome;
    }

    public Class getType(FacesContext facescontext) throws MethodNotFoundException
    {
        return String.class;
    }


    //~ StateHolder support ----------------------------------------------------------------------------

    private boolean _transient = false;

    /**
     * Empty constructor, so that new instances can be created when restoring state.
     */
    public SimpleActionMethodBinding()
    {
        _outcome = null;
    }

    public Object saveState(FacesContext facescontext)
    {
        return _outcome;
    }

    public void restoreState(FacesContext facescontext, Object obj)
    {
        _outcome = (String)obj;
    }

    public boolean isTransient()
    {
        return _transient;
    }

    public void setTransient(boolean flag)
    {
        _transient = flag;
    }
}
