/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.examples.listexample;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SimpleCountryList
{
    private List _countries = new ArrayList();
    static
    {
    }

    SimpleCountry getSimpleCountry(long id)
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

    long getNewSimpleCountryId()
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

    void saveSimpleCountry(SimpleCountry simpleCountry)
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

    void deleteSimpleCountry(SimpleCountry simpleCountry)
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
        _countries.add(new SimpleCountry(1, "AUSTRIA", "AT", new BigDecimal(123)));
        _countries.add(new SimpleCountry(2, "AZERBAIJAN", "AZ", new BigDecimal(535)));
        _countries.add(new SimpleCountry(3, "BAHAMAS", "BS", new BigDecimal(1345623)));
        _countries.add(new SimpleCountry(4, "BAHRAIN", "BH", new BigDecimal(346)));
        _countries.add(new SimpleCountry(5, "BANGLADESH", "BD", new BigDecimal(456)));
        _countries.add(new SimpleCountry(6, "BARBADOS", "BB", new BigDecimal(45645)));
    }

    public List getCountries()
    {
        return _countries;
    }

    public void setCountries(List countries)
    {
        _countries = countries;
    }
}
