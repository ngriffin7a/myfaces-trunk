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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;
import java.lang.reflect.Array;
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

    private boolean compareObjectArrays(Object[] previous,
                                        Object[] value)
    {
        int length = ((Object[])value).length;
        if (((Object[])previous).length != length)
        {
            //different length
            return true;
        }

        boolean[] scoreBoard = new boolean[length];
        for (int i = 0; i < length; i++)
        {
            Object p = previous[i];
            boolean found = false;
            for (int j = 0; j < length; j++)
            {
                if (scoreBoard[j] == false)
                {
                    Object v = value[j];
                    if ((p == null && v == null) ||
                        (p != null && v != null && p.equals(v)))
                    {
                        scoreBoard[j] = true;
                        found = true;
                        break;
                    }
                }
            }
            if (!found)
            {
                return true;    //current element of previous array not found in new array
            }
        }

        return false;   // arrays are identical
    }

    private boolean compareLists(List previous, List value)
    {
        int length = value.size();
        if (previous.size() != length)
        {
            //different length
            return true;
        }

        boolean[] scoreBoard = new boolean[length];
        for (int i = 0; i < length; i++)
        {
            Object p = previous.get(i);
            boolean found = false;
            for (int j = 0; j < length; j++)
            {
                if (scoreBoard[j] == false)
                {
                    Object v = value.get(j);
                    if ((p == null && v == null) ||
                        (p != null && v != null && p.equals(v)))
                    {
                        scoreBoard[j] = true;
                        found = true;
                        break;
                    }
                }
            }
            if (!found)
            {
                return true;    //current element of previous List not found in new List
            }
        }

        return false;   // Lists are identical
    }

    private boolean comparePrimitiveArrays(Object previous, Object value)
    {
        int length = Array.getLength(value);
        if (Array.getLength(previous) != length)
        {
            //different length
            return true;
        }

        boolean[] scoreBoard = new boolean[length];
        for (int i = 0; i < length; i++)
        {
            Object p = Array.get(previous, i);
            boolean found = false;
            for (int j = 0; j < length; j++)
            {
                if (scoreBoard[j] == false)
                {
                    Object v = Array.get(value, j);
                    if ((p == null && v == null) ||
                        (p != null && v != null && p.equals(v)))
                    {
                        scoreBoard[j] = true;
                        found = true;
                        break;
                    }
                }
            }
            if (!found)
            {
                return true;    //current element of previous array not found in new array
            }
        }

        return false;   // arrays are identical
    }


    /**
     * First part is identical to super.validate except the empty condition.
     * Second part: iterate through UISelectItem and UISelectItems and check
     *              current values against these items
     */
    public void validate(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        Object submittedValue = getSubmittedValue();
        if (submittedValue == null) return;

        Object convertedValue = getConvertedValue(context, submittedValue);
        if (!isValid()) return;

        boolean empty = convertedValue == null ||
                        (convertedValue instanceof Object[] &&
                         ((Object[])convertedValue).length == 0);
        if (isRequired() && empty)
        {
            _MessageUtils.addErrorMessage(context, this, REQUIRED_MESSAGE_ID,new Object[]{getId()});
            setValid(false);
            return;
        }

        if (!empty)
        {
            _ComponentUtils.callValidators(context, this, convertedValue);
        }
        if (!isValid()) return;

        //TODO: see javadoc: iterate through UISelectItem and UISelectItems and check
        //current values against these items

        Object previousValue = getValue();
        setValue(convertedValue);
        setSubmittedValue(null);
        if (compareValues(previousValue, convertedValue))
        {
            queueEvent(new ValueChangeEvent(this, previousValue, convertedValue));
        }
    }


    private Object getConvertedValue(FacesContext context, Object submittedValue)
    {
        try
        {
            Renderer renderer = getRenderer(context);
            if (renderer != null)
            {
                return renderer.getConvertedValue(context, this, submittedValue);
            }
            else if (submittedValue instanceof String[])
            {
                return _SharedRendererUtils.getConvertedUISelectManyValue(context, this,
                                                                          (String[])submittedValue);
            }
        }
        catch (ConverterException e)
        {
            FacesMessage facesMessage = e.getFacesMessage();
            if (facesMessage != null)
            {
                context.addMessage(getClientId(context), facesMessage);
            }
            else
            {
                _MessageUtils.addErrorMessage(context, this, CONVERSION_MESSAGE_ID,new Object[]{getId()});
            }
            setValid(false);
        }
        return submittedValue;
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
