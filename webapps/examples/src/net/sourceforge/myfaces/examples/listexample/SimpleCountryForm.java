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

import java.math.BigDecimal;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SimpleCountryForm
{
    private String _isoCode = null;
    private String _name = null;
    private BigDecimal _size = null;

    public void setIsoCode(String isoCode)
    {
        _isoCode = isoCode;
        /*
        if (_name == null)
        {
            Locale currLocale = FacesContext.getCurrentInstance().getLocale();
            _name = new Locale("", isoCode).getDisplayCountry(currLocale);
        }
        */
    }

    public String getIsoCode()
    {
        return _isoCode;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public BigDecimal getSize()
    {
        return _size;
    }

    public void setSize(BigDecimal size)
    {
        _size = size;
    }
}
