/**
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

package javax.faces.event;

import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import java.util.EventObject;

/**
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public class PhaseEvent extends EventObject
{

	// FIELDS
    private FacesContext _facesContext;
    private PhaseId _phaseId;

	// CONSTRUCTORS
	public PhaseEvent(FacesContext facesContext, PhaseId phaseId, Lifecycle lifecycle)
	{
		super(lifecycle);
        if (facesContext == null) throw new NullPointerException("facesContext");
        if (phaseId == null) throw new NullPointerException("phaseId");
        if (lifecycle == null) throw new NullPointerException("lifecycle");

        _facesContext = facesContext;
        _phaseId = phaseId;
	}

	// METHODS
	public FacesContext getFacesContext()
	{
		return _facesContext;
	}

	public PhaseId getPhaseId()
	{
		return _phaseId;
	}

}
