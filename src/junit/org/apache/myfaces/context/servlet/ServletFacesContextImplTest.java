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
package org.apache.myfaces.context.servlet;

import org.apache.myfaces.MyFacesBaseTest;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.2  2004/07/01 22:01:09  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/04/16 13:57:00  manolito
 * Bug #922317 - ClassCastException in action handler after adding message
 *
 */
public class ServletFacesContextImplTest
        extends MyFacesBaseTest
{
    //private static final Log log = LogFactory.getLog(ServletFacesContextImplTest.class);

    public ServletFacesContextImplTest(String name)
    {
        super(name);
    }

    public void testGetMessages()
    {
        int cnt;
        FacesContext context = new ServletFacesContextImpl(_servletContext,
                                                           _httpServletRequest,
                                                           _httpServletResponse);

        context.addMessage("clientId1", new FacesMessage("summary1", "detail1"));
        context.addMessage("clientId2", new FacesMessage("summary2", "detail2"));
        context.addMessage(null, new FacesMessage("summary3", "detail3"));
        context.addMessage("clientId2", new FacesMessage("summary4", "detail4"));
        context.addMessage(null, new FacesMessage("summary5", "detail5"));
        context.addMessage(null, new FacesMessage("summary6", "detail6"));

        System.out.println("\nAll messages");
        cnt = 0;
        for (Iterator it = context.getMessages(); it.hasNext(); cnt++)
        {
            FacesMessage m = (FacesMessage)it.next();
            System.out.println(m.getSummary() + "/" + m.getDetail());
        }
        assertEquals(cnt, 6);

        System.out.println("\nclientId2 messages");
        cnt = 0;
        for (Iterator it = context.getMessages("clientId2"); it.hasNext(); cnt++)
        {
            FacesMessage m = (FacesMessage)it.next();
            System.out.println(m.getSummary() + "/" + m.getDetail());
        }
        assertEquals(cnt, 2);

        System.out.println("\nnull messages");
        cnt = 0;
        for (Iterator it = context.getMessages(null); it.hasNext(); cnt++)
        {
            FacesMessage m = (FacesMessage)it.next();
            System.out.println(m.getSummary() + "/" + m.getDetail());
        }
        assertEquals(cnt, 3);
    }
}
