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

import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class SimpleCountryForm
{
    private long _id;
    private String _name;
    private String _isoCode;

    public long getId()
    {
        return _id;
    }

    public void setId(long id)
    {
        _id = id;
        if (_id > 0)
        {
            SimpleCountry simpleCountry = getList().getSimpleCountry(_id);
            if (simpleCountry == null)
            {
                return;
            }
            _name = simpleCountry.getName();
            _isoCode = simpleCountry.getIsoCode();
        }
    }

    public void setIsoCode(String isoCode)
    {
        _isoCode = isoCode;
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

    private SimpleCountry getSimpleCountry()
    {
        return new SimpleCountry(_id, _name, _isoCode, null);
    }

    public String save()
    {
        getList().saveSimpleCountry(getSimpleCountry());
        return "ok_next";
    }

    public String delete()
    {
        getList().deleteSimpleCountry(getSimpleCountry());
        return "ok_next";
    }

    public String apply()
    {
        getList().saveSimpleCountry(getSimpleCountry());
        return "ok";
    }

    private SimpleCountryList getList()
    {
        Object obj = FacesContext.getCurrentInstance().getApplication().getVariableResolver()
            .resolveVariable(FacesContext.getCurrentInstance(), "countryList");
        return (SimpleCountryList) obj;

    }
}
