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
package net.sourceforge.myfaces.el;

import net.sourceforge.myfaces.MyFacesTest;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValueBindingTest
    extends MyFacesTest
{
    protected A _theA = new A();
    protected A _a = new A();

    public ValueBindingTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        /*
        _httpServletRequest.setAttribute("theA", _theA);
        _httpServletRequest.setAttribute("a", _a);
        */

        VariableResolverMockImpl vr = new VariableResolverMockImpl();
        vr.addVariable("theA", _theA);
        vr.addVariable("a", _a);

        _application.setVariableResolver(vr);
    }

    public void testGetValueWithLongName1() throws Exception
    {
        Object v;
        v = _application.getValueBinding("theA").getValue(_facesContext);
        assertTrue(v == _theA);
    }

    public void testGetValueWithLongName2() throws Exception
    {
        Object v;
        v = _application.getValueBinding("theA.name").getValue(_facesContext);
        assertEquals(A.NAME, v);
    }

    public void testGetValueWithLongName3() throws Exception
    {
        Object v;
        v = _application.getValueBinding("theA.theB.name").getValue(_facesContext);
        assertEquals(B.NAME, v);
    }

    public void testGetValueWithLongName4() throws Exception
    {
        Object v;
        v = _application.getValueBinding("theA.theB.theC.name").getValue(_facesContext);
        assertEquals(C.NAME, v);
    }



    public void testGetValueWithShortName1() throws Exception
    {
        Object v;
        v = _application.getValueBinding("a").getValue(_facesContext);
        assertTrue(v == _a);
    }

    public void testGetValueWithShortName2() throws Exception
    {
        Object v;
        v = _application.getValueBinding("a.name").getValue(_facesContext);
        assertEquals(A.NAME, v);
    }

    public void testGetValueWithShortName3() throws Exception
    {
        Object v;
        v = _application.getValueBinding("a.b.name").getValue(_facesContext);
        assertEquals(B.NAME, v);
    }

    public void testGetValueWithShortName4() throws Exception
    {
        Object v;
        v = _application.getValueBinding("a.b.c.name").getValue(_facesContext);
        assertEquals(C.NAME, v);
    }

}
