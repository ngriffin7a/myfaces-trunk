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
package org.apache.myfaces.examples.listexample;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

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
    private boolean _remove = false;
    private List _cities;

    public SimpleCountry(long id, String name, String isoCode, BigDecimal size, String[] cities)
    {
        _id = id;
        _name = name;
        _isoCode = isoCode;
        _size = size;

        if(cities!=null)
            _cities = Arrays.asList(cities);
        else
            _cities = Collections.EMPTY_LIST;
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

    public List getCities()
    {
        return _cities;
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

    public boolean isRemove()
    {
        return _remove;
    }

    public void setRemove(boolean remove)
    {
        _remove = remove;
    }
}
