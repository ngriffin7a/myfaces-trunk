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
package net.sourceforge.myfaces.el;

import javax.faces.el.MethodBinding;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class MethodBindingTest extends ELBaseTest
{
    //~ Constructors -------------------------------------------------------------------------------

    public MethodBindingTest(String name)
    {
        super(name);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void testGetType() throws Exception
    {
        MethodBinding mb;
        
        mb = _application.createMethodBinding("#{a.getName}", new Class[] {});
        assertSame(String.class, mb.getType(_facesContext));

        mb = _application.createMethodBinding("#{a.getInt}", new Class[] {});
        assertSame(Integer.class, mb.getType(_facesContext));

        mb = _application.createMethodBinding("#{theA.theB.getName}", new Class[] {});
        assertSame(String.class, mb.getType(_facesContext));

        mb = _application.createMethodBinding("#{testmap.toString}", new Class[] {});
        assertSame(String.class, mb.getType(_facesContext));

        mb = _application.createMethodBinding("#{ testmap  [  0  ]  [  1  ] . toString} ", new Class[] {});
        assertSame(String.class, mb.getType(_facesContext));

        mb = _application.createMethodBinding("#{true ? a.getName : a.getInt}", new Class[] {});
        assertSame(String.class, mb.getType(_facesContext));

        mb = _application.createMethodBinding("#{false ? a.getName : a.getInt}", new Class[] {});
        assertSame(Integer.class, mb.getType(_facesContext));

        try 
        {
            mb = _application.createMethodBinding("#{testmap}", new Class[] {});
            mb.getType(_facesContext);
            assertTrue(false);
        }
        catch (Exception e) {
            // ignore, error expected
        }
    }

    public void testInvoke() throws Exception
    {
        MethodBinding mb;
        
        mb = _application.createMethodBinding("#{a.getName}", new Class[] {});
        assertSame(A.NAME, mb.invoke(_facesContext, null));

        mb = _application.createMethodBinding("#{theA.theB.getName}", new Class[] {});
        assertSame(B.NAME, mb.invoke(_facesContext, null));

        mb = _application.createMethodBinding("#{true ? a.getName : a.getInt}", new Class[] {});
        assertSame(A.NAME, mb.invoke(_facesContext, null));

        mb = _application.createMethodBinding("#{false ? a.getName : a.getInt}", new Class[] {});
        assertEquals(new Integer(0), mb.invoke(_facesContext, null));

        mb = _application.createMethodBinding("#{false ? a.getName : true ? a.getInt : zzz}", new Class[] {});
        assertEquals(new Integer(0), mb.invoke(_facesContext, null));
    }
}
