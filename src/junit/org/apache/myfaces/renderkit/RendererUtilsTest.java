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

package net.sourceforge.myfaces.renderkit;

import net.sourceforge.myfaces.MyFacesBaseTest;
import net.sourceforge.myfaces.el.ValueBindingImpl;

import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/03 08:05:19  manolito
 * new test
 *
 */
public class RendererUtilsTest
        extends MyFacesBaseTest
{
    //private static final Log log = LogFactory.getLog(RendererUtilsTest.class);

    public RendererUtilsTest(String name)
    {
        super(name);
    }


    protected void setUp() throws Exception
    {
        super.setUp();

        Map reqMap = _facesContext.getExternalContext().getRequestMap();
        reqMap.put("bean", new Bean());
    }


    public static class Bean
    {
        private String _countryCode = Locale.US.getCountry();
        public String getCountryCode()
        {
            return _countryCode;
        }

        public SelectItem[] getCountrySelectItems()
        {
            String[] codes = Locale.getISOCountries();
            SelectItem[] items = new SelectItem[codes.length];
            for (int i = 0, len = codes.length; i < len; i++)
            {
                String iso = codes[i];
                String name = new Locale("", codes[i]).getDisplayCountry();
                items[i] = new SelectItem(iso, name);
            }
            return items;
        }
    }


    public void testGetSelectItemList()
    {
        UISelectOne uiSelectOne = new UISelectOne();
        uiSelectOne.setId("id1");
        uiSelectOne.setValueBinding("value", new ValueBindingImpl(_application,
                                                                  "#{bean.countryCode}"));

        UISelectItems uiSelectItems = new UISelectItems();
        uiSelectItems.setId("id2");
        uiSelectItems.setValueBinding("value", new ValueBindingImpl(_application,
                                                                  "#{bean.countrySelectItems}"));

        uiSelectOne.getChildren().add(uiSelectItems);

        List lst = RendererUtils.getSelectItemList(uiSelectOne);
        assertEquals(Locale.getISOCountries().length, lst.size());
    }

}
