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
package net.sourceforge.myfaces.taglib.core;

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValidateStringRangeTag
    extends ValidatorTag
{
    private boolean _isMinSet = false;
    private boolean _isMaxSet = false;
    private String _minimum;
    private String _maximum;

    private static final String ID = "StringRange";

    public ValidateStringRangeTag()
    {
        _minimum = null;
        _maximum = null;
    }

    public void release()
    {
        _isMaxSet = false;
        _isMinSet = false;
        _minimum = null;
        _maximum = null;
    }

    public String getMinimum()
    {
        return _minimum;
    }

    public void setMinimum(String minimum)
    {
        _minimum = minimum;
        _isMinSet = true;
    }

    public String getMaximum()
    {
        return _maximum;
    }

    public void setMaximum(String maximum)
    {
        _maximum = maximum;
        _isMaxSet = true;
    }

    protected Validator createValidator()
        throws JspException
    {
        setId(ID);
        //FIXME
        /*
        StringRangeValidator validator = (StringRangeValidator)super.createValidator();
        if(_isMinSet)
        {
            validator.setMinimum(getMinimum());
        }
        if(_isMaxSet)
        {
            validator.setMaximum(getMaximum());
        }
        return validator;
        */
        return null;
    }
}
