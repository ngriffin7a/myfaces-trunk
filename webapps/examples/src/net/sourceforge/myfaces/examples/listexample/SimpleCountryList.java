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

import net.sourceforge.myfaces.examples.listexample.SimpleCountry;

import java.util.*;
import java.math.BigDecimal;

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
        _countries.add(new SimpleCountry("AUSTRIA", "AT", new BigDecimal(123)));
        _countries.add(new SimpleCountry("AZERBAIJAN", "AZ", new BigDecimal(535)));
        _countries.add(new SimpleCountry("BAHAMAS", "BS", new BigDecimal(1345623)));
        _countries.add(new SimpleCountry("BAHRAIN", "BH", new BigDecimal(346)));
        _countries.add(new SimpleCountry("BANGLADESH", "BD", new BigDecimal(456)));
        _countries.add(new SimpleCountry("BARBADOS", "BB", new BigDecimal(45645)));
    }

    public Iterator getCountries()
    {
        return _countries.iterator();
    }
}
