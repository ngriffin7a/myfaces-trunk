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
package net.sourceforge.myfaces.config;



/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationCaseConfig
    implements Config
{
    private String _fromActionRef = null;
    private String _fromOutcome = null;
    private String _toTreeId = null;

    public String getFromActionRef()
    {
        return _fromActionRef;
    }

    public void setFromActionRef(String fromActionRef)
    {
        _fromActionRef = fromActionRef;
    }

    public String getFromOutcome()
    {
        return _fromOutcome;
    }

    public void setFromOutcome(String fromOutcome)
    {
        _fromOutcome = fromOutcome;
    }

    public String getToTreeId()
    {
        return _toTreeId;
    }

    public void setToTreeId(String toTreeId)
    {
        _toTreeId = toTreeId;
    }
}
