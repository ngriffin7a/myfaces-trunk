/*
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
package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * see Javadoc of JSF Specification
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISelectMany
        extends UIInput
{
    public static final String INVALID_MESSAGE_ID = "javax.faces.component.UISelectMany.INVALID";

    public Object[] getSelectedValues()
    {
        return (Object[])getValue();
    }

    public void setSelectedValues(Object[] selectedValues)
    {
        setValue(selectedValues);
    }

    public ValueBinding getValueBinding(String name)
    {
        if (name == null) throw new NullPointerException("name");
        if (name.equals("selectedValues"))
        {
            return super.getValueBinding("value");
        }
        else
        {
            return super.getValueBinding(name);
        }
    }

    public void setValueBinding(String name,
                                ValueBinding binding)
    {
        if (name == null) throw new NullPointerException("name");
        if (name.equals("selectedValues"))
        {
            super.setValueBinding("value", binding);
        }
        else
        {
            super.setValueBinding(name, binding);
        }
    }

    /**
     * TODO: JUnit testing!!!
     * @return true if Objects are different (!)
     */
    protected boolean compareValues(Object previous,
                                    Object value)
    {
        if (previous == null)
        {
            // one is null, the other not
            return value != null;
        }
        else if (value == null)
        {
            // one is null, the other not
            return previous != null;
        }
        else
        {
            if (previous instanceof Object[] &&
                value instanceof Object[])
            {
                return compareObjectArrays((Object[])previous, (Object[])value);
            }
            else if (previous instanceof List &&
                     value instanceof List)
            {
                return compareLists((List)previous, (List)value);
            }
            else if (previous.getClass().isArray() &&
                     value.getClass().isArray())
            {
                return comparePrimitiveArrays(previous, value);
            }
            else
            {
                //Objects have different classes
                return true;
            }
        }
    }

    /**
     * TODO: optimize
     */
    private boolean compareObjectArrays(Object[] previous,
                                        Object[] value)
    {
        int length = ((Object[])value).length;
        if (((Object[])previous).length != length)
        {
            //different length
            return true;
        }
        List previousList = new ArrayList(length);
        for (int i = 0; i < previous.length; i++)
        {
            previousList.add(previous[i]);
        }

        List valueList = new ArrayList(length);
        for (int i = 0; i < previous.length; i++)
        {
            valueList.add(previous[i]);
        }

        return compareLists(previousList, valueList);
    }

    private boolean compareLists(List previous, List value)
    {
        int length = value.size();
        if (previous.size() != length)
        {
            //different length
            return true;
        }

        List tempList = new ArrayList(length);
        Collections.copy(tempList, previous);

        for (Iterator it = value.iterator(); it.hasNext(); )
        {
            Object item = it.next();
            if (!tempList.remove(item))
            {
                //element exists in value list but not in previous list
                return true;
            }
        }

        return false;
    }

    private boolean comparePrimitiveArrays(Object previous, Object value)
    {
        int length = Array.getLength(value);
        if (Array.getLength(previous) != length)
        {
            //different length
            return true;
        }

        List previousList = new ArrayList(length);
        List valueList = new ArrayList(length);
        for (int i = 0; i < length; i++)
        {
            previousList.add(Array.get(previous, i));
            valueList.add(Array.get(value, i));
        }

        return compareLists(previousList, valueList);
    }



    public void validate(FacesContext context)
    {
        super.validate(context);
        //TODO: see javadoc: iterate through UISelectItem and UISelectItems and check
        //current values against these items
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.SelectMany";
    public static final String COMPONENT_FAMILY = "javax.faces.SelectMany";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Listbox";


    public UISelectMany()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }


    //------------------ GENERATED CODE END ---------------------------------------
}
