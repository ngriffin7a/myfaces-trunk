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
package org.apache.myfaces.el;

import org.apache.myfaces.MyFacesBaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ELBaseTest extends MyFacesBaseTest
{
    //~ Instance fields ----------------------------------------------------------------------------

    protected A        _a    = new A();
    protected A        _theA = new A();
    protected List     _l    = new ArrayList();
    protected Map      _m    = new HashMap();
    protected Object[] _a0;
    protected Object[] _a1;
    
    protected double[] _arrd = {0, 1, 2};
    protected int[] _arri = {0, 1, 2};
    protected Double[] _arrD = {new Double(0), new Double(1), new Double(2)};
    protected Integer[] _arrI = {new Integer(0), new Integer(1), new Integer(2)};

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
                    _implicitObjects.put("arrd", _arrd);
                    _implicitObjects.put("arri", _arri);
                    _implicitObjects.put("arrD", _arrD);
                    _implicitObjects.put("arrI", _arrI);
                }
            });

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
    }
}
