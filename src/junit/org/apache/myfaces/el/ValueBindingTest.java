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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import net.sourceforge.myfaces.MyFacesTest;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ValueBindingTest
    extends MyFacesTest
{
    protected A _theA = new A();
    protected A _a = new A();
    protected Map _m = new HashMap();
    protected List _l = new ArrayList();
    protected Object[] _a0;
    protected Object[] _a1;

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

        // Setup for testNestedObjects()
        _a0 = new Object[] {new Integer(0), new Integer(1), new Integer(2)};
        _a1 = new Object[] {_a0, null};

        _l.add(0, _a0);
        _l.add(1, _a1);
        _l.add(2, "hello");
        _l.add(3, new D("testClass"));
        _l.add(4, _m);
        _l.add(5, new Boolean(true));

        _m.put("0", _a0);
        _m.put("1", _a1);
        _m.put("list", _l);
        _m.put("true", "TRUE");
        _m.put("false", "FALSE");
        _m.put("o", new D("OBJECT"));
        _m.put("o0", new D(_a0));
        _m.put("o1", new D(_a1));
        _m.put("map", _m);
        _m.put("my]bracket[", _m);
        _m.put("f", new Boolean(false));
        _m.put("t", new Boolean(true));

        vr.addVariable("testmap", _m);
        // END Setup for testNestedObjects()

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
    
    public void testNestedObjects() throws Exception
    {
        ValueBinding vb;
        Object r;
        
        vb = _application.getValueBinding("testmap");
        r = vb.getValue(_facesContext);
        assertEquals(r, _m);

        vb = _application.getValueBinding("testmap.f");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Boolean(false));

        vb = _application.getValueBinding("testmap.t");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Boolean(true));

        vb = _application.getValueBinding("testmap[\"o\"]['obj']");
        r = vb.getValue(_facesContext);
        assertEquals(r, "OBJECT");

        vb = _application.getValueBinding("testmap.true");
        r = vb.getValue(_facesContext);
        assertEquals(r, "TRUE");

        vb = _application.getValueBinding("testmap[testmap.t]");
        r = vb.getValue(_facesContext);
        assertEquals(r, "TRUE");

        vb = _application.getValueBinding("testmap.false");
        r = vb.getValue(_facesContext);
        assertEquals(r, "FALSE");

        vb = _application.getValueBinding("testmap[testmap.f]");
        r = vb.getValue(_facesContext);
        assertEquals(r, "FALSE");

        vb = _application.getValueBinding("testmap[0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.o0.obj[0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.o1.obj[0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.list[0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.list[1][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, _a0);

        vb = _application.getValueBinding("testmap.list[4][0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.list[4][1][0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.list[4].list[1][0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.map.map.list[4].list[4][1][0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.map.list[4].map.list[4][1][0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));


        vb = _application.getValueBinding("testmap.list[4][testmap.list[4][0][0]][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.list[4][1][0][testmap.list[4][0][0]]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.list[4].list[testmap.list[4][1][0][testmap.list[4][0][1]]][0][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap.map.map.list[4].list[4][1][testmap.list[4].list[testmap.list[4][1][0][testmap.list[4][0][testmap.t]]][0][0]][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));

        vb = _application.getValueBinding("testmap['my\\]bracket\\[']['\\m\\a\\p'].list[4]['map'][\"list\"][4][1][testmap.map.map.list[4].list[4][1][testmap.list[4].list[testmap.list[4][1][testmap.f][testmap['list'][4][0][1]]][0][0]][0]][0]");
        r = vb.getValue(_facesContext);
        assertEquals(r, new Integer(0));


        try {
            vb = _application.getValueBinding("testmap.map.list[4].map.list[][1][0][0]");
            assertTrue(false);
        } catch (ReferenceSyntaxException e) {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try {
            vb = _application.getValueBinding("testmap.map.list[4].map.list.[4][1][0][0]");
            assertTrue(false);
        } catch (ReferenceSyntaxException e) {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try {
            vb = _application.getValueBinding("testmap.map.list[4].map..list[4][1][0][0]");
            assertTrue(false);
        } catch (ReferenceSyntaxException e) {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try {
            vb = _application.getValueBinding(".testmap.map.list[4].map.list[4][1][0][0]");
            assertTrue(false);
        } catch (ReferenceSyntaxException e) {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try {
            vb = _application.getValueBinding("testmap.map.list[4].map.list[4[1][0]['0']");
            assertTrue(false);
        } catch (ReferenceSyntaxException e) {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try {
            vb = _application.getValueBinding("testmap.map.list[4].map.list[4][1][0].[0]");
            assertTrue(false);
        } catch (ReferenceSyntaxException e) {
            //System.out.println(e.getMessage());
            // we expect this
        }
    }
}
