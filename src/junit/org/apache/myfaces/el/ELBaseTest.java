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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ELBaseTest extends MyFacesTest
{
    //~ Instance fields ----------------------------------------------------------------------------

    protected A        _a    = new A();
    protected A        _theA = new A();
    protected List     _l    = new ArrayList();
    protected Map      _m    = new HashMap();
    protected Object[] _a0;
    protected Object[] _a1;

    //~ Constructors -------------------------------------------------------------------------------

    public ELBaseTest(String name)
    {
        super(name);
    }

    //~ Methods ------------------------------------------------------------------------------------

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
