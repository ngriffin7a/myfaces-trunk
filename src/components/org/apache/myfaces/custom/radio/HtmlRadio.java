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
package net.sourceforge.myfaces.custom.radio;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/03/31 13:26:07  manolito
 * extended radio renderer
 *
 */
public class HtmlRadio
    extends UIComponentBase
{
    //private static final Log log = LogFactory.getLog(HtmlRadio.class);

    public static final String FOR_ATTR = "for".intern();
    public static final String INDEX_ATTR = "index".intern();


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlRadio";
    public static final String COMPONENT_FAMILY = "net.sourceforge.myfaces.Radio";
    private static final int DEFAULT_INDEX = -1;

    private String _for = null;
    private Integer _index = null;

    public HtmlRadio()
    {
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setFor(String forValue)
    {
        _for = forValue;
    }

    public String getFor()
    {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setIndex(int index)
    {
        _index = new Integer(index);
    }

    public int getIndex()
    {
        if (_index != null) return _index.intValue();
        ValueBinding vb = getValueBinding("index");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : DEFAULT_INDEX;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _index;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _for = (String)values[1];
        _index = (Integer)values[2];
    }
    //------------------ GENERATED CODE END ---------------------------------------

}
