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

package net.sourceforge.myfaces.cactus;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class TestBean
{
    private long numberPrimitiveLong;
    private Long numberLong;
    private BigDecimal numberBigDecimal;
    private List list;
    private Map map;
    private Date date;
    private String valueWithDefault = "default";


    public String getValueWithDefault()
    {
        return valueWithDefault;
    }


    public void setValueWithDefault(String valueWithDefault)
    {
        this.valueWithDefault = valueWithDefault;
    }


    public long getNumberPrimitiveLong()
    {
        return numberPrimitiveLong;
    }


    public void setNumberPrimitiveLong(long numberPrimitiveLong)
    {
        this.numberPrimitiveLong = numberPrimitiveLong;
    }


    public Long getNumberLong()
    {
        return numberLong;
    }


    public void setNumberLong(Long numberLong)
    {
        this.numberLong = numberLong;
    }


    public BigDecimal getNumberBigDecimal()
    {
        return numberBigDecimal;
    }


    public void setNumberBigDecimal(BigDecimal numberBigDecimal)
    {
        this.numberBigDecimal = numberBigDecimal;
    }


    public List getList()
    {
        return list;
    }


    public void setList(List list)
    {
        this.list = list;
    }


    public Map getMap()
    {
        return map;
    }


    public void setMap(Map map)
    {
        this.map = map;
    }


    public Date getDate()
    {
        return date;
    }


    public void setDate(Date date)
    {
        this.date = date;
    }
}
