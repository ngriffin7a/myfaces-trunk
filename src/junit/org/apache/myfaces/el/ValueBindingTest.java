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
package net.sourceforge.myfaces.el;

import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * 
 * $Log$
 * Revision 1.16  2004/03/30 07:38:11  dave0000
 * implement mixed string-reference expressions
 *
 */
public class ValueBindingTest extends ELBaseTest
{
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
    
    public void testCompositeExpressions()
    {
        ValueBinding vb;
        Object       r;

        vb     = _application.createValueBinding("This is one #{testmap[\"o\"]['obj']}");
        r      = vb.getValue(_facesContext);
        assertEquals("This is one OBJECT", r);

        vb     = _application.createValueBinding("#{ testmap [ \"o\" ] [ 'obj' ] } is the One.");
        r      = vb.getValue(_facesContext);
        assertEquals("OBJECT is the One.", r);

        vb     = _application.createValueBinding("Is it really #{testmap.true_}?");
        r      = vb.getValue(_facesContext);
        assertEquals("Is it really TRUE_?", r);

        vb     = _application.createValueBinding("#{testmap . true_} or #{testmap.false_}, that's the question!");
        r      = vb.getValue(_facesContext);
        assertEquals("TRUE_ or FALSE_, that's the question!", r);

        vb     = _application.createValueBinding("What? #{testmap . true_} or #{testmap.false_}, that's the question!");
        r      = vb.getValue(_facesContext);
        assertEquals("What? TRUE_ or FALSE_, that's the question!", r);

        vb     = _application.createValueBinding("What? ${ #{testmap . true_} or #{testmap.false_}, that's the question! }");
        r      = vb.getValue(_facesContext);
        assertEquals("What? ${ TRUE_ or FALSE_, that's the question! }", r);

        vb     = _application.createValueBinding("#{ '#{' } What? ${ #{true ? '${' : \"#{\\\\\"} #{testmap . true_} or #{testmap.false_}, that's the question! }");
        r      = vb.getValue(_facesContext);
        assertEquals("#{ What? ${ ${ TRUE_ or FALSE_, that's the question! }", r);
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
}
