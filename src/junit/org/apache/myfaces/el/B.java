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
