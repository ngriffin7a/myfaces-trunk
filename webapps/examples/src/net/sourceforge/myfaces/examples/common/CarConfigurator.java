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
package net.sourceforge.myfaces.examples.common;

import net.sourceforge.myfaces.examples.util.LocalizedSelectItem;

import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CarConfigurator
{
    private List _cars;
    private List _colors;
    private List _extrasList;
    private BigDecimal _price = new BigDecimal(0);
    private String[] _extras;
    private String _discount;
    private String _discount2;
    private String _car;
    private String _color;
    private HashMap _priceList = new HashMap();
    private HashMap _priceFactorColors = new HashMap();
    private HashMap _priceListExtras = new HashMap();

    public CarConfigurator()
    {
    }

    private void init()
    {
        if (_cars == null)
        {
            _cars = new ArrayList();
            _colors = new ArrayList();;
            _extrasList = new ArrayList();;

            _cars.add(new SelectItem("c1", "Audee X6", null));
            _cars.add(new SelectItem("c2", "PMW 321u", null));
            _cars.add(new SelectItem("c3", "Masta ZX7", null));
            _cars.add(new SelectItem("c4", "Renolt ESP", null));
            _cars.add(new SelectItem("c5", "WV Lumpo", null));

            _colors.add(new LocalizedSelectItem("color_black"));
            _colors.add(new LocalizedSelectItem("color_blue"));
            _colors.add(new LocalizedSelectItem("color_marine"));
            _colors.add(new LocalizedSelectItem("color_red"));

            _extrasList.add(new LocalizedSelectItem("extra_aircond"));
            _extrasList.add(new LocalizedSelectItem("extra_sideab"));
            _extrasList.add(new LocalizedSelectItem("extra_mirrowheat"));
            _extrasList.add(new LocalizedSelectItem("extra_leaderseat"));

            _priceList.put("c1", new BigDecimal(30000));
            _priceList.put("c2", new BigDecimal(32000));
            _priceList.put("c3", new BigDecimal(20000));
            _priceList.put("c4", new BigDecimal(25000));
            _priceList.put("c5", new BigDecimal(10000));

            _priceFactorColors.put("color_black", new BigDecimal(1.15));
            _priceFactorColors.put("color_blue", new BigDecimal(1.10));
            _priceFactorColors.put("color_marine", new BigDecimal(1.05));
            _priceFactorColors.put("color_red", new BigDecimal(1.0));

            _priceListExtras.put("extra_aircond", new BigDecimal(510));
            _priceListExtras.put("extra_sideab", new BigDecimal(1220));
            _priceListExtras.put("extra_mirrowheat", new BigDecimal(1230));
            _priceListExtras.put("extra_leaderseat", new BigDecimal(840));
        }
    }

    public Iterator getCars()
    {
        init();
        return _cars.iterator();
    }

    public Iterator getColors()
    {
        init();
        return _colors.iterator();
    }

    public Iterator getExtrasList()
    {
        init();
        return _extrasList.iterator();
    }

    public String getCar()
    {
        return _car;
    }

    public void setCar(String car)
    {
        _car = car;
    }

    public String getColor()
    {
        return _color;
    }

    public void setColor(String color)
    {
        _color = color;
    }

    public BigDecimal getPrice()
    {
        return _price;
    }

    public void setPrice(BigDecimal price)
    {
        _price = price;
    }

    public String[] getExtras()
    {
        return _extras;
    }

    public void setExtras(String[] extras)
    {
        _extras = extras;
    }

    public String getDiscount()
    {
        return _discount;
    }

    public void setDiscount(String discount)
    {
        _discount = discount;
    }

    public String getDiscount2()
    {
        return _discount2;
    }

    public void setDiscount2(String discount2)
    {
        _discount2 = discount2;
    }

    public void calcPrice()
    {
        init();
        String car = getCar();
        String color = getColor();
        if (car == null ||
            color == null)
        {
            _price = new BigDecimal(0);
            return;
        }

        BigDecimal carprice = (BigDecimal)_priceList.get(car);
        BigDecimal colorfactor = (BigDecimal)_priceFactorColors.get(color);
        if (carprice == null ||
            colorfactor == null)
        {
            _price = new BigDecimal(0);
            return;
        }
        _price = carprice.multiply(colorfactor);

        String[] extras = getExtras();
        if (extras != null)
        {
            for (int i = 0; i < extras.length; i++)
            {
                String extra = extras[i];
                _price = _price.add((BigDecimal)_priceListExtras.get(extra));
            }
        }

        if (_discount != null)
        {
            try
            {
                int i = Integer.parseInt(_discount);
                switch (i)
                {
                    case 0: break;
                    case 1: _price = _price.multiply(new BigDecimal(0.95)); break;
                    case 2: _price = _price.multiply(new BigDecimal(0.91)); break;
                }
            }
            catch (NumberFormatException e)
            {
            }
        }

        if (_discount2 != null)
        {
            try
            {
                int i = Integer.parseInt(_discount2);
                switch (i)
                {
                    case 0: _price = _price.multiply(new BigDecimal(2)); break;
                    case 1: _price = _price.multiply(new BigDecimal(0.80)); break;
                }
            }
            catch (NumberFormatException e)
            {
            }
        }
    }


    /*
    FIXME
    public Action getCalcPriceAction()
    {
        return new Action() {
            public String invoke()
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                ValueBinding vb = af.getApplication().getValueBinding("carconf");
                CarConfigurator carconf = (CarConfigurator)vb.getValue(facesContext);
                carconf.calcPrice();
                return "ok";
            }
        };
    }
    */

}
