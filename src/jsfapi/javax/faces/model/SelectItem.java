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

package javax.faces.model;

/**
  * DOCUMENT ME!
  *
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public class SelectItem
{
	// FIELDS
    private Object _value;
    private String _label;
    private String _description;
    private boolean _disabled;

	// CONSTRUCTORS
    public SelectItem()
    {
    }

    public SelectItem(Object value)
    {
        _value = value;
    }

    public SelectItem(Object value, String label)
    {
        _value = value;
        _label = label;
    }

    public SelectItem(Object value, String label, String description)
    {
        _value = value;
        _label = label;
        _description = description;
    }

    public SelectItem(Object value, String label, String description, boolean disabled)
    {
        _value = value;
        _label = label;
        _description = description;
        _disabled = disabled;
    }

	// METHODS
    public String getDescription()
    {
        return _description;
    }

    public void setDescription(String description)
    {
        _description = description;
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public String getLabel()
    {
        return _label;
    }

    public void setLabel(String label)
    {
        _label = label;
    }

    public Object getValue()
    {
        return _value;
    }

    public void setValue(Object value)
    {
        _value = value;
    }
}
