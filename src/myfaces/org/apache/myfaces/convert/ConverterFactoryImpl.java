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
package net.sourceforge.myfaces.convert;

import javax.faces.FacesException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterFactoryImpl
        implements ConverterFactory
{
    private static final String CONVERTER_PROPS =
        "net.sourceforge.myfaces.convert.converter".replace('.', '/') + ".properties";
    private HashMap _converters = new HashMap();

    public ConverterFactoryImpl()
    {
        read();
    }

    protected void read()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(CONVERTER_PROPS);
        if (stream == null)
        {
            throw new FacesException("File " + CONVERTER_PROPS + " not found.");
        }
        Properties converterProps = new Properties();
        try
        {
            converterProps.load(stream);
        }
        catch (IOException e)
        {
            throw new FacesException("Error reading file " + CONVERTER_PROPS + ".");
        }

        //Materialize
        for (Iterator it = converterProps.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            try
            {
                Class c = Class.forName((String)entry.getValue());
                _converters.put(entry.getKey(), c.newInstance());
            }
            catch (ClassNotFoundException e)
            {
                throw new FacesException(e);
            }
            catch (InstantiationException e)
            {
                throw new FacesException(e.getCause());
            }
            catch (IllegalAccessException e)
            {
                throw new FacesException(e);
            }
        }
    }

    public void addConverter(String id, Converter converter)
    {
        synchronized (_converters)
        {
            if (_converters.put(id, converter) != null)
            {
                throw new RuntimeException("Duplicate converter " + id);
            }
        }
    }

    public Converter getConverter(String id)
            throws IllegalArgumentException
    {
        Converter conv = (Converter)_converters.get(id);
        if (conv == null)
        {
            throw new IllegalArgumentException("No converter with id '" + id + "' found!");
        }
        return conv;
    }

    public Iterator getConverterIds()
    {
        return _converters.keySet().iterator();
    }

}
