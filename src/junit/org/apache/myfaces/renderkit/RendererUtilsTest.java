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
 * Revision 1.2  2004/07/01 22:01:21  mwessendorf
 * ASF switch
 *
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
