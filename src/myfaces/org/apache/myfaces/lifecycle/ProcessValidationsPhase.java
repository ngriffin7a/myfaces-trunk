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
import javax.faces.event.PhaseId;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ProcessValidationsPhase
        extends AbstractPhase
{
    private Lifecycle _lifecycle;
    public ProcessValidationsPhase(Lifecycle lifecycle)
    {
        super(PhaseId.PROCESS_VALIDATIONS);
        _lifecycle = lifecycle;
    }

    public int execute(FacesContext facescontext)
            throws FacesException
    {
        super.execute(facescontext, _action);
        Iterator it = facescontext.getMessages();
        if (it.hasNext())
        {
            return GOTO_RENDER;
        }
        else
        {
            return GOTO_NEXT;
        }
    }

    private PhaseAction _action = new PhaseAction()
    {
        public int doActionForComponent(FacesContext facescontext, UIComponent uicomponent)
                throws FacesException
        {
            uicomponent.processValidators(facescontext);
            return GOTO_NEXT;
        }
    };

}
