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
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterFactoryImpl
        extends ConverterFactory
{
    private static final String CONVERTER_PROPS =
        "net.sourceforge.myfaces.convert.converter".replace('.', '/') + ".properties";
    private static final String CONV_MAP_PROPS =
        "net.sourceforge.myfaces.convert.converterMap".replace('.', '/') + ".properties";

    private Map _convertersById;
    private Map _convertersByClass;

    public ConverterFactoryImpl()
    {
        read();
    }

    protected void mapPrimitives()
    {
        _convertersByClass.put(Boolean.TYPE, _convertersById.get("BooleanConverter"));
        _convertersByClass.put(Long.TYPE, _convertersById.get("LongConverter"));
        _convertersByClass.put(Integer.TYPE, _convertersById.get("IntegerConverter"));
    }


    protected synchronized void read()
    {
        Properties converterProps = loadProperties(CONVERTER_PROPS);

        //Materialize and store converters
        _convertersById = new HashMap();
        for (Iterator it = converterProps.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            try
            {
                Class c = Class.forName((String)entry.getValue());
                _convertersById.put(entry.getKey(), c.newInstance());
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

        //Map classes to converters
        Properties converterMapProps = loadProperties(CONV_MAP_PROPS);
        _convertersByClass = new HashMap();
        for (Iterator it = converterMapProps.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            Converter conv = (Converter)_convertersById.get(entry.getValue());
            if (conv == null)
            {
                throw new RuntimeException("Error in converter map: converter " + (String)entry.getValue() + " not found!");
            }
            try
            {
                Class c = Class.forName((String)entry.getKey());
                _convertersByClass.put(c, conv);
            }
            catch (ClassNotFoundException e)
            {
                throw new FacesException(e);
            }
        }

        mapPrimitives();
    }

    protected Properties loadProperties(String fileName)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(fileName);
        if (stream == null)
        {
            throw new FacesException("File " + fileName + " not found.");
        }
        Properties props = new Properties();
        try
        {
            props.load(stream);
        }
        catch (IOException e)
        {
            throw new FacesException("Error reading file " + fileName + ".");
        }
        return props;
    }



    public void addConverter(String id, Converter converter)
    {
        synchronized (_convertersById)
        {
            if (_convertersById.put(id, converter) != null)
            {
                throw new IllegalArgumentException("Duplicate converter " + id);
            }
        }
    }

    public Converter getConverter(String id)
            throws IllegalArgumentException
    {
        Converter conv = (Converter)_convertersById.get(id);
        if (conv == null)
        {
            throw new IllegalArgumentException("No converter with id '" + id + "' found!");
        }
        return conv;
    }

    public Iterator getConverterIds()
    {
        return _convertersById.keySet().iterator();
    }



    public void addConverter(Class clazz, Converter converter)
    {
        synchronized (_convertersByClass)
        {
            if (_convertersByClass.put(clazz, converter) != null)
            {
                throw new IllegalArgumentException("Duplicate converter for class " + clazz.getName());
            }
        }
    }

    public Converter getConverter(Class clazz)
    {
        Converter conv = (Converter)_convertersByClass.get(clazz);
        if (conv == null)
        {
            throw new IllegalArgumentException("No converter for class " + clazz.getName() + "' found!");
        }
        return conv;
    }

    public Iterator getConverterClasses()
    {
        return _convertersByClass.keySet().iterator();
    }
}
