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
package net.sourceforge.myfaces.examples.example1;

import java.math.BigDecimal;
import java.io.Serializable;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class CalcForm
    implements Serializable
{
    private BigDecimal number1 = new BigDecimal(0);
    private BigDecimal number2 = new BigDecimal(0);
    private BigDecimal result = new BigDecimal(0);

    public void add()
    {
        result = number1.add(number2);
    }

    public void subtract()
    {
        result = number1.subtract(number2);
    }

    public BigDecimal getNumber1()
    {
        return number1;
    }

    public void setNumber1(BigDecimal number1)
    {
        this.number1 = number1;
    }

    public BigDecimal getNumber2()
    {
        return number2;
    }

    public void setNumber2(BigDecimal number2)
    {
        this.number2 = number2;
    }

    public BigDecimal getResult()
    {
        return result;
    }

    public void setResult(BigDecimal result)
    {
        this.result = result;
    }

}
