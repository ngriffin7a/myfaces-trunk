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

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SimpleCountryList
{
    private static List _countries = new ArrayList();
    static
    {
        _countries.add(new SimpleCountry(1, "AUSTRIA", "AT", new BigDecimal(123)));
        _countries.add(new SimpleCountry(2, "AZERBAIJAN", "AZ", new BigDecimal(535)));
        _countries.add(new SimpleCountry(3, "BAHAMAS", "BS", new BigDecimal(1345623)));
        _countries.add(new SimpleCountry(4, "BAHRAIN", "BH", new BigDecimal(346)));
        _countries.add(new SimpleCountry(5, "BANGLADESH", "BD", new BigDecimal(456)));
        _countries.add(new SimpleCountry(6, "BARBADOS", "BB", new BigDecimal(45645)));
    }

    static synchronized SimpleCountry getSimpleCountry(long id)
    {
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() == id)
            {
                return country;
            }
        }
        return null;
    }

    private static synchronized long getNewSimpleCountryId()
    {
        long maxId = 0;
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() > maxId)
            {
                maxId = country.getId();
            }
        }
        return maxId + 1;
    }

    static synchronized void saveSimpleCountry(SimpleCountry simpleCountry)
    {
        if (simpleCountry.getId() == 0)
        {
            simpleCountry.setId(getNewSimpleCountryId());
        }
        boolean found = false;
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() == simpleCountry.getId())
            {
                _countries.set(i, simpleCountry);
                found = true;
            }
        }
        if (!found)
        {
            _countries.add(simpleCountry);
        }
    }

    static synchronized void deleteSimpleCountry(SimpleCountry simpleCountry)
    {
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() == simpleCountry.getId())
            {
                _countries.remove(i);
            }
        }
    }

    public SimpleCountryList()
    {
    }

    public List getCountries()
    {
        return _countries;
    }
}
