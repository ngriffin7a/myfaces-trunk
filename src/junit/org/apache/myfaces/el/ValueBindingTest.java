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
package net.sourceforge.myfaces.el;

import java.util.Map;

import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * 
 * $Log$
 * Revision 1.23  2004/09/01 18:25:19  mwessendorf
 * added TestCase
 *
 * Revision 1.22  2004/07/27 06:46:59  dave0000
 * cleanup/arange testcases, remove duplicates
 *
 * Revision 1.21  2004/07/27 06:28:34  dave0000
 * fix issue with getType of literal expressions (and other improvements)
 *
 * Revision 1.20  2004/07/01 22:00:55  mwessendorf
 * ASF switch
 *
 * Revision 1.19  2004/05/11 04:24:12  dave0000
 * Bug 943166: add value coercion to ManagedBeanConfigurator
 *
 * Revision 1.18  2004/05/10 05:30:15  dave0000
 * Fix issue with setting Managed Bean to a wrong scope
 *
 * Revision 1.17  2004/04/07 03:54:07  dave0000
 * fix testcases to match removed trim() on expression string
 *
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

        vb     = _application.createValueBinding("#{ testmap  [  0  ]  [  0  ]}");
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
    
    public void testMixedExpressions()
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
        
//        // Test '\' as escape for #{
//        vb     = _application.createValueBinding("\\#{ \\\\\\#{ What? ${ \\\\#{false ? '${' : \"#{\\\\\"} #{testmap . true_} or #{testmap.false_}, that's the question! }");
//        r      = vb.getValue(_facesContext);
//        assertEquals("#{ \\#{ What? ${ \\\\#{\\ TRUE_ or FALSE_, that's the question! }", r);
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

        vb = _application.createValueBinding("#{ testmap  [  0  ]  [  0  ]}");
        assertFalse(vb.isReadOnly(_facesContext));
        
        vb = _application.createValueBinding("#{true ? cookie : max}");
        assertTrue(vb.isReadOnly(_facesContext));

        vb = _application.createValueBinding("#{false ? cookie : max}");
        assertFalse(vb.isReadOnly(_facesContext));
    }

    public void testGetType() throws Exception
    {
        ValueBinding vb;

        //-----------------------------------------------------
        // Literal expression
        
        vb = _application.createValueBinding("#{'constant literal'}");
        assertSame(String.class, vb.getType(_facesContext));

        vb = _application.createValueBinding("#{false && true}");
        assertSame(Boolean.class, vb.getType(_facesContext));

        vb = _application.createValueBinding("#{applicationScope.bean == null}");
        assertSame(Boolean.class, vb.getType(_facesContext));
        
        // REVISIT: Should getType of implicit object throw an error, 
        //          return null, or Object.class, or the appropriate Map class?
        vb = _application.createValueBinding("#{cookie}");
        assertTrue(Map.class.isAssignableFrom(vb.getType(_facesContext)));

        vb = _application.createValueBinding("#{requestScope}");
        assertTrue(Map.class.isAssignableFrom(vb.getType(_facesContext)));

        vb = _application.createValueBinding("#{true ? cookie : max}");
        assertTrue(Map.class.isAssignableFrom(vb.getType(_facesContext)));

        // REVISIT: should unknown bean name return type null or Object.class?
        vb = _application.createValueBinding("#{false ? cookie : max}");
        assertSame(Object.class, vb.getType(_facesContext));

        //-----------------------------------------------------
        // Specific types
        
        vb = _application.createValueBinding("#{a.name}");
        assertSame(A.NAME.getClass(), vb.getType(_facesContext));

        vb = _application.createValueBinding("#{theA.theB.name}");
        assertSame(B.NAME.getClass(), vb.getType(_facesContext));

        vb = _application.createValueBinding("#{testmap}");
        assertSame(_m.getClass(), vb.getType(_facesContext));

        vb = _application.createValueBinding("#{testmap.f}");
        assertSame(Boolean.class, vb.getType(_facesContext));

        vb = _application.createValueBinding("#{ testmap  [  0  ]  [  1  ]}");
        // Object.class is the _a0 Array Component Type!
        assertSame(Object.class, vb.getType(_facesContext));

        vb = _application.createValueBinding("#{nonExistingValueBlahBlahBlah}");
        assertSame(Object.class, vb.getType(_facesContext));
    }
    
    public void testManagedBean() throws Exception
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{testBean_B.name}");
        assertEquals("testName", vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B.int}");
        assertEquals(new Integer(1), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B.double}");
        assertEquals(new Double(1.1), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B.integer}");
        assertEquals(new Integer(2), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B.double2}");
        assertEquals(new Double(2.2), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B1.double2}");
        assertEquals(new Double(2.2), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B2.name}");
        assertEquals(B.NAME, vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B2.int}");
        assertEquals(new Integer(-1), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B2.double}");
        assertEquals(new Double(-1.1), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B2.integer}");
        assertEquals(new Integer(-2), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_B2.double2}");
        assertEquals(new Double(-2.2), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_E.a}");
        assertEquals("foo", vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_E.b}");
        assertEquals("bar", vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{testBean_E.a}#{testBean_E.b}");
        assertEquals("foobar", vb.getValue(_facesContext));

    }
}
