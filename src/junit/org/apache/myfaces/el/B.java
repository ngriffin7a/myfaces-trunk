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
package net.sourceforge.myfaces.el;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class B
{
    public static final String NAME = "Dummy object B";
    
    private String _name = NAME;
    private int _int = -1;
    private double _double = -1.1;
    private Integer _integer = new Integer(-2);
    private Double _double2 = new Double(-2.2);

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
        // Note: in certain cases, our tests reset the object on every invocation!
    }

    public C getC()
    {
        return new C();
    }

    public C getTheC()
    {
        return new C();
    }
    
    public void setInt(int i)
    {
        _int = i;
    }
    
    public int getInt()
    {
        return _int;
    }
    public double getDouble()
    {
        return _double;
    }
    public void setDouble(double double1)
    {
        _double = double1;
    }
    public Double getDouble2()
    {
        return _double2;
    }
    public void setDouble2(Double double2)
    {
        _double2 = double2;
    }
    public Integer getInteger()
    {
        return _integer;
    }
    public void setInteger(Integer integer)
    {
        _integer = integer;
    }
}
