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

import net.sourceforge.myfaces.MyFacesTest;

import javax.faces.el.MethodBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ValueBindingTest
    extends MyFacesTest
{
    //~ Instance fields ----------------------------------------------------------------------------

    protected A        _a    = new A();
    protected A        _theA = new A();
    protected List     _l    = new ArrayList();
    protected Map      _m    = new HashMap();
    protected Object[] _a0;
    protected Object[] _a1;

    //~ Constructors -------------------------------------------------------------------------------

    public ValueBindingTest(String name)
    {
        super(name);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void testGetValueWithLongName1()
    throws Exception
    {
        Object       v;
        ValueBinding vb = _application.createValueBinding("#{theA}");
        v = vb.getValue(_facesContext);
        assertTrue(v == _theA);
    }

    public void testGetValueWithLongName2()
    throws Exception
    {
        Object v;
        v = _application.createValueBinding("#{theA.name}").getValue(_facesContext);
        assertEquals(A.NAME, v);
    }

    public void testGetValueWithLongName3()
    throws Exception
    {
        Object v;
        v = _application.createValueBinding("#{theA.theB.name}").getValue(_facesContext);
        assertEquals(B.NAME, v);
    }

    public void testGetValueWithLongName4()
    throws Exception
    {
        Object v;
        v = _application.createValueBinding("#{theA.theB.theC.name}").getValue(_facesContext);
        assertEquals(C.DEFAULT_NAME, v);
    }

    public void testGetValueWithShortName1()
    throws Exception
    {
        Object v;
        v = _application.createValueBinding("#{a}").getValue(_facesContext);
        assertTrue(v == _a);
    }

    public void testGetValueWithShortName2()
    throws Exception
    {
        Object v;
        v = _application.createValueBinding("#{a.name}").getValue(_facesContext);
        assertEquals(A.NAME, v);
    }

    public void testGetValueWithShortName3()
    throws Exception
    {
        Object v;
        v = _application.createValueBinding("#{a.b.name}").getValue(_facesContext);
        assertEquals(B.NAME, v);
    }

    public void testGetValueWithShortName4()
    throws Exception
    {
        Object v;
        v = _application.createValueBinding("#{a.b.c.name}").getValue(_facesContext);
        assertEquals(C.DEFAULT_NAME, v);
    }

    public void testGetValue()
    throws Exception
    {
        ValueBinding vb;
        Object       r;

        vb     = _application.createValueBinding("#{testmap}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{true ? testmap : testmap.f}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{testmap.f}");
        r      = vb.getValue(_facesContext);
        assertFalse(((Boolean) r).booleanValue());

        vb     = _application.createValueBinding("#{false ? testmap : testmap.f}");
        r      = vb.getValue(_facesContext);
        assertFalse(((Boolean) r).booleanValue());

        vb     = _application.createValueBinding("#{testmap.t}");
        r      = vb.getValue(_facesContext);
        assertTrue(((Boolean) r).booleanValue());

        vb     = _application.createValueBinding("#{testmap[\"o\"]['obj']}");
        r      = vb.getValue(_facesContext);
        assertEquals("OBJECT", r);

        vb     = _application.createValueBinding("#{ testmap [ \"o\" ] [ 'obj' ] }");
        r      = vb.getValue(_facesContext);
        assertEquals("OBJECT", r);

        vb     = _application.createValueBinding("#{testmap.true_}");
        r      = vb.getValue(_facesContext);
        assertEquals("TRUE_", r);

        vb     = _application.createValueBinding("#{ testmap . true_ }");
        r      = vb.getValue(_facesContext);
        assertEquals("TRUE_", r);

        vb     = _application.createValueBinding("#{testmap[\"true_\"]}");
        r      = vb.getValue(_facesContext);
        assertEquals("TRUE_", r);

        vb     = _application.createValueBinding("#{testmap['true_']}");
        r      = vb.getValue(_facesContext);
        assertEquals("TRUE_", r);

        vb     = _application.createValueBinding("#{testmap[testmap.t]}");
        r      = vb.getValue(_facesContext);
        assertEquals("TRUE", r);

        vb     = _application.createValueBinding("#{ testmap   [ testmap   . t ] }");
        r      = vb.getValue(_facesContext);
        assertEquals("TRUE", r);

        vb     = _application.createValueBinding("#{testmap.false_}");
        r      = vb.getValue(_facesContext);
        assertEquals("FALSE_", r);

        vb     = _application.createValueBinding("#{testmap[testmap.f]}");
        r      = vb.getValue(_facesContext);
        assertEquals("FALSE", r);

        vb     = _application.createValueBinding("#{testmap['map']}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{testmap[\"David's\"]}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{testmap['David\\'s']}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{testmap['my]bracket[']}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{testmap[\"my]bracket[\"]}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{testmap[\"my\\\\]bracket[\"]}");
        r      = vb.getValue(_facesContext);
        assertEquals(_m, r);

        vb     = _application.createValueBinding("#{testmap[0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{ testmap  [  0  ]  [  0  ]} ");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.o0.obj[0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.o1.obj[0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.list[0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.list[1][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(_a0, r);

        vb     = _application.createValueBinding("#{testmap.list[4][0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.list[4][1][0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.list[4].list[1][0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.map.map.list[4].list[4][1][0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.map.list[4].map.list[4][1][0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.list[4][testmap.list[4][0][0]][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap.list[4][1][0][testmap.list[4][0][0]]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding(
                "#{testmap.list[4].list[testmap.list[4][1][0][testmap.list[4][0][1]]][0][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding(
                "#{testmap.map.map.list[4].list[4][1][testmap.list[4].list[testmap.list[4][1][0][testmap.list[4][0][testmap.t]]][0][0]][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding(
                "#{testmap['my]bracket[']['map'].list[4]['map'][\"list\"][4][1][testmap.map.map.list[4].list[4][1][testmap.list[4].list[testmap.list[4][1][testmap.f][testmap['list'][4][0][1]]][0][0]][0]][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding(
                "#{testmap['my]bracket[']['map'].list[4]['map'][\"list\"][4][1][testmap.map.map.list[4].list[4][1][testmap.list[4].list[testmap.list[4][1][testmap.f][testmap['list'][4][0][1]]][0][0]][0]][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding(
                "#{testmap['my\\\\]bracket[']['map'].list[4]['map'][\"list\"][4][1][testmap.map.map.list[4].list[4][1][testmap.list[4].list[testmap.list[4][1][testmap.f][testmap['list'][4][0][1]]][0][0]][0]][0]}");
        r      = vb.getValue(_facesContext);
        assertEquals(new Integer(0), r);

        vb     = _application.createValueBinding("#{testmap[\"\\\\]true[\"]}");
        r      = vb.getValue(_facesContext);
        assertEquals("_TRUE_", r);

        vb     = _application.createValueBinding("#{testmap['\\\\]false[']}");
        r      = vb.getValue(_facesContext);
        assertEquals("_FALSE_", r);

        // Now check for error conditions
        try
        {
            vb = _application.createValueBinding(
                    "#{testmap['my\\\\]bracket[']['map'].list[4]['map'][\"list\"][4][1][testmap.map.map.list[4].list[4][1][testmap.list[4].list[testmap.list[4][1][testmap.f][testmap['list'][4][0][1]]][0][0]][0][0]}");
            assertTrue(false);
        }
        catch (ReferenceSyntaxException e)
        {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try
        {
            vb = _application.createValueBinding("#{testmap.map.list[4].map.list[][1][0][0]}");
            assertTrue(false);
        }
        catch (ReferenceSyntaxException e)
        {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try
        {
            vb = _application.createValueBinding("#{testmap.map.list[4].map.list.[4][1][0][0]}");
            assertTrue(false);
        }
        catch (ReferenceSyntaxException e)
        {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try
        {
            vb = _application.createValueBinding("#{testmap.map.list[4].map..list[4][1][0][0]}");
            assertTrue(false);
        }
        catch (ReferenceSyntaxException e)
        {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try
        {
            vb = _application.createValueBinding("#{.testmap.map.list[4].map.list[4][1][0][0]}");
            assertTrue(false);
        }
        catch (ReferenceSyntaxException e)
        {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try
        {
            vb = _application.createValueBinding("#{testmap.map.list[4].map.list[4[1][0]['0']}");
            assertTrue(false);
        }
        catch (ReferenceSyntaxException e)
        {
            //System.out.println(e.getMessage());
            // we expect this
        }

        try
        {
            vb = _application.createValueBinding("#{testmap.map.list[4].map.list[4][1][0].[0]}");
            assertTrue(false);
        }
        catch (ReferenceSyntaxException e)
        {
            //System.out.println(e.getMessage());
            // we expect this
        }
    }

    /**
     * Since test functions are called by name in ascending order, 
     * this will always be called after testGetValues(). Otherwise testGetValues()
     * would fail as we modify the values (mind this if you change the function names!)
     * 
     * @throws Exception
     */
    public void testSetValue() throws Exception
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{theA.theB.name}");
        vb.setValue(_facesContext, "test");

        vb = _application.createValueBinding("#{testmap.newValue}");
        vb.setValue(_facesContext, "newValue");
        assertSame("newValue", vb.getValue(_facesContext));

        vb     = _application.createValueBinding("#{ testmap [ \"o\" ] [ 'obj' ] }");
        vb.setValue(_facesContext, "NEW_OBJECT");
        assertSame("NEW_OBJECT", vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{ testmap  [  0  ]  [  0  ]} ");
        vb.setValue(_facesContext, _theA);
        assertSame(_theA, vb.getValue(_facesContext));
        
        vb     = _application.createValueBinding(
            "#{testmap.list[4].list[testmap.list[4][1][0][testmap.list[4][0][1]]][0][0]}");
        vb.setValue(_facesContext, "zzz");
        assertSame("zzz", vb.getValue(_facesContext));
    }
    
    public void testIsReadOnly() throws Exception
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{'constant literal'}");
        assertTrue(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{cookie}");
        assertTrue(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{requestScope}");
        assertTrue(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{a.name}");
        assertTrue(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{theA.theB.name}");
        assertFalse(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{testmap}");
        assertFalse(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{testmap.f}");
        assertFalse(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{ testmap  [  0  ]  [  0  ]} ");
        assertFalse(vb.isReadOnly(_facesContext));
        
        vb = _application.createValueBinding("#{true ? cookie : max}");
        assertTrue(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{false ? cookie : max}");
        assertFalse(vb.isReadOnly(_facesContext));
    }

    public void testGetValueType() throws Exception
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{a.name}");
        assertSame(A.NAME.getClass(), vb.getType(_facesContext));

        vb = _application.createValueBinding("#{theA.theB.name}");
        assertSame(B.NAME.getClass(), vb.getType(_facesContext));

        vb = _application.createValueBinding("#{testmap}");
        assertSame(_m.getClass(), vb.getType(_facesContext));

        vb = _application.createValueBinding("#{testmap.f}");
        assertSame(Boolean.class, vb.getType(_facesContext));

        vb = _application.createValueBinding("#{ testmap  [  0  ]  [  1  ]} ");
        // Object.class is the _a0 Array Component Type!
        assertSame(Object.class, vb.getType(_facesContext));

// Not supported by mock objects yet        
//        vb = _application.createValueBinding("#{notExistingValueBlahBlahBlah}");
//        vb.setValue(_facesContext, new Object());
//        assertSame(Object.class, vb.getType(_facesContext));
        
        try 
        {
            _application.createValueBinding("#{cookie}").setValue(_facesContext, null);
            assertTrue(false);
        }
        catch (Exception e) {
            // ignore, error expected
        }
    }
    
    /**
     * Should move to its own class, but running out of time--so using the setUp() code here
     */
    public void testMethodBindingGetType() throws Exception
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

    /**
     * Should move to its own class, but running out of time--so using the setUp() code here
     */
    public void testMethodBindingInvoke() throws Exception
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

    protected void setUp()
    throws Exception
    {
        super.setUp();

        /*
        _httpServletRequest.setAttribute("theA", _theA);
        _httpServletRequest.setAttribute("a", _a);
        */
        _application.setVariableResolver(
            new VariableResolverImpl()
            {
                {
                    _implicitObjects.put("theA", _theA);
                    _implicitObjects.put("a", _a);
                    _implicitObjects.put("testmap", _m);
                }
            });

        // Setup for testNestedObjects()
        _a0     = new Object[] {new Integer(0), new Integer(1), new Integer(2)};
        _a1     = new Object[] {_a0, null};

        _l.add(0, _a0);
        _l.add(1, _a1);
        _l.add(2, "hello");
        _l.add(3, new D("testClass"));
        _l.add(4, _m);
        _l.add(5, new Boolean(true));

        _m.put(new Integer(0), _a0);
        _m.put(new Long(0), _a0);
        _m.put(new Long(1), _a1);
        _m.put("list", _l);
        _m.put(new Boolean(true), "TRUE");
        _m.put(new Boolean(false), "FALSE");
        _m.put("true_", "TRUE_");
        _m.put("false_", "FALSE_");
        _m.put("o", new D("OBJECT"));
        _m.put("o0", new D(_a0));
        _m.put("o1", new D(_a1));
        _m.put("map", _m);
        _m.put("David's", _m);
        _m.put("my]bracket[", _m);
        _m.put("my\\]bracket[", _m);
        _m.put("my\\\\]bracket[", _m);
        _m.put("\\]true[", "_TRUE_");
        _m.put("\\]false[", "_FALSE_");
        _m.put("f", new Boolean(false));
        _m.put("t", new Boolean(true));

        // END Setup for testNestedObjects()
    }
}
