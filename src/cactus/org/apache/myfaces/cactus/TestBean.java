/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
