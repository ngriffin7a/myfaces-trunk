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
package net.sourceforge.myfaces.examples.listexample;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SimpleCountry
        implements Serializable
{
    private long _id;
    private String _name;
    private String _isoCode;
    private BigDecimal _size;

    public SimpleCountry(long id, String name, String isoCode, BigDecimal size)
    {
        _id = id;
        _name = name;
        _isoCode = isoCode;
        _size = size;
    }

    public long getId()
    {
        return _id;
    }

    public String getName()
    {
        return _name;
    }

    public String getIsoCode()
    {
        return _isoCode;
    }

    public BigDecimal getSize()
    {
        return _size;
    }

    public void setId(long id)
    {
        _id = id;
    }

    public void setIsoCode(String isoCode)
    {
        _isoCode = isoCode;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setSize(BigDecimal size)
    {
        _size = size;
    }
}
