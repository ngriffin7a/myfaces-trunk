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

import java.util.ArrayList;
import java.util.Collections;

/**
  * DOCUMENT ME!
  *
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public class PhaseId {

	// FIELDS
	public static final javax.faces.event.PhaseId ANY_PHASE;
	public static final javax.faces.event.PhaseId APPLY_REQUEST_VALUES;
	public static final javax.faces.event.PhaseId INVOKE_APPLICATION;
	public static final javax.faces.event.PhaseId PROCESS_VALIDATIONS;
	public static final javax.faces.event.PhaseId RENDER_RESPONSE;
	public static final javax.faces.event.PhaseId RESTORE_VIEW;
	public static final javax.faces.event.PhaseId UPDATE_MODEL_VALUES;
	public static final java.util.List VALUES;

    static
    {
        int i = 0;
        ArrayList list = new ArrayList(6);

        ANY_PHASE = new PhaseId("ANY_PHASE",i++);
        list.add(ANY_PHASE);
        RESTORE_VIEW = new PhaseId("RESTORE_VIEW",i++);
        list.add(RESTORE_VIEW);
        APPLY_REQUEST_VALUES = new PhaseId("APPLY_REQUEST_VALUES",i++);
        list.add(APPLY_REQUEST_VALUES);
        PROCESS_VALIDATIONS = new PhaseId("PROCESS_VALIDATIONS",i++);
        list.add(PROCESS_VALIDATIONS);
        UPDATE_MODEL_VALUES = new PhaseId("UPDATE_MODEL_VALUES",i++);
        list.add(UPDATE_MODEL_VALUES);
        INVOKE_APPLICATION = new PhaseId("INVOKE_APPLICATION",i++);
        list.add(INVOKE_APPLICATION);
        RENDER_RESPONSE = new PhaseId("RENDER_RESPONSE",i++);
        list.add(RENDER_RESPONSE);
        VALUES = Collections.unmodifiableList(list);
    }


    private final String _name;
    private final int _ordinal;

	// CONSTRUCTORS
    public PhaseId(String name, int ordinal)
    {
        this._name = name;
        this._ordinal = ordinal;
    }

	// METHODS
	public int compareTo(Object other)
	{
		return _ordinal - ((PhaseId)other)._ordinal;
	}

	public int getOrdinal()
	{
		return _ordinal;
	}

	public String toString()
	{
		return _name + _ordinal;
	}

}
