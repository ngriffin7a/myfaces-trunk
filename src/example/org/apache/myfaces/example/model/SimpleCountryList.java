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
package net.sourceforge.myfaces.example.model;

import java.util.*;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SimpleCountryList
{
    private List _countries = new ArrayList();

    public SimpleCountryList()
    {
        _countries.add(new SimpleCountry("AUSTRIA", "AT"));
        _countries.add(new SimpleCountry("AZERBAIJAN", "AZ"));
        _countries.add(new SimpleCountry("BAHAMAS", "BS"));
        _countries.add(new SimpleCountry("BAHRAIN", "BH"));
        _countries.add(new SimpleCountry("BANGLADESH", "BD"));
        _countries.add(new SimpleCountry("BARBADOS", "BB"));
    }

    public Iterator getCountries()
    {
        return _countries.iterator();
    }
}
