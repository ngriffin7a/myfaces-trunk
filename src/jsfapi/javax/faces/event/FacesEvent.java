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

import javax.faces.component.UIComponent;
import java.util.EventObject;
import java.io.Serializable;

/**
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public abstract class FacesEvent extends EventObject
    implements Serializable
{
	// FIELDS
    private PhaseId _phaseId;

	// CONSTRUCTORS
	public FacesEvent(UIComponent uiComponent)
	{
        super(uiComponent);
        if (uiComponent == null) throw new IllegalArgumentException("uiComponent");
        _phaseId = PhaseId.ANY_PHASE;
	}

	// METHODS
    public abstract boolean isAppropriateListener(FacesListener faceslistener);

    public abstract void processListener(FacesListener faceslistener);

	public UIComponent getComponent()
	{
        return (UIComponent)getSource();
	}

	public void queue()
	{
        ((UIComponent)getSource()).queueEvent(this);
	}

    public PhaseId getPhaseId()
    {
        return _phaseId;
    }

    public void setPhaseId(PhaseId phaseId)
    {
        _phaseId = phaseId;
    }
}
