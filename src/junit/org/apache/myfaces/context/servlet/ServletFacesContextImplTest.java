package net.sourceforge.myfaces.context.servlet;

import net.sourceforge.myfaces.MyFacesBaseTest;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
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
