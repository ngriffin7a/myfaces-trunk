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

import javax.faces.el.ValueBinding;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class SetValueBindingTest extends ELBaseTest
{
    //~ Constructors -------------------------------------------------------------------------------

    public SetValueBindingTest(String name)
    {
        super(name);
    }

    //~ Methods ------------------------------------------------------------------------------------

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

        vb = _application.createValueBinding("#{ testmap  [  0  ]  [  0  ]}");
        vb.setValue(_facesContext, _theA);
        assertSame(_theA, vb.getValue(_facesContext));
        
        vb = _application.createValueBinding(
            "#{testmap.list[4].list[testmap.list[4][1][0][testmap.list[4][0][1]]][0][0]}");
        vb.setValue(_facesContext, "zzz");
        assertSame("zzz", vb.getValue(_facesContext));
        
        vb = _application.createValueBinding("#{nonExistingValueBlahBlahBlah}");
        vb.setValue(_facesContext, new Double(5.5));
        assertEquals(new Double(5.5), vb.getValue(_facesContext));
    }
    
    public void testSetRootValueDefaultScope()
    {
        ValueBinding vb;

        // --------------------------------------------------
        // Test setting/updating value in the default request scope
        
        // set to a new variable
        vb = _application.createValueBinding("#{newVar}");
        vb.setValue(_facesContext, "test-value");
        assertSame("test-value", vb.getValue(_facesContext));

        // update existing variable
        vb.setValue(_facesContext, "another-value");
        assertSame("another-value", vb.getValue(_facesContext));

        // make sure it was created in requestScope
        vb = _application.createValueBinding("#{requestScope.newVar}");
        assertSame("another-value", vb.getValue(_facesContext));
    }
    
    public void testSetRootValueNonDefaultScope()
    {
        ValueBinding vb;
        
        // set to a new variable in application scope
        vb = _application.createValueBinding("#{applicationScope.newVar1}");
        vb.setValue(_facesContext, "test-value");
        assertSame("test-value", vb.getValue(_facesContext));

        // update existing variable without providing scope
        vb = _application.createValueBinding("#{newVar1}");
        vb.setValue(_facesContext, "another-value");
        assertSame("another-value", vb.getValue(_facesContext));

        // make sure it was updated in application scope
        vb = _application.createValueBinding("#{applicationScope.newVar1}");
        assertSame("another-value", vb.getValue(_facesContext));
    }
    
    public void testSetWithCoercion()
    {
        ValueBinding vb;
        
        // set to a new variable in session scope
        vb = _application.createValueBinding("#{sessionScope.newVar2}");
        vb.setValue(_facesContext, new Integer(123));
        assertEquals(new Integer(123), vb.getValue(_facesContext));

        // update existing variable without providing scope
        vb = _application.createValueBinding("#{newVar2}");
        vb.setValue(_facesContext, new Double(321.123));
        assertEquals(new Integer(321), vb.getValue(_facesContext));

        // make sure it was updated in session scope
        vb = _application.createValueBinding("#{sessionScope.newVar2}");
        assertEquals(new Integer(321), vb.getValue(_facesContext));
        
        try
        {
            vb.setValue(_facesContext, new B());
            assertTrue(false);
        }
        catch (Exception e)
        {
            // expected: error because B cannot be coerced Integer
        }
    }
    
    public void testCoercion()
    {
        ValueBinding vb;

        // test with no coercion needed
        vb = _application.createValueBinding("#{arrd[0]}");
        vb.setValue(_facesContext, new Double(666.666));
        assertEquals(new Double(666.666), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{arri[0]}");
        vb.setValue(_facesContext, new Integer(667));
        assertEquals(new Integer(667), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{arrD[0]}");
        vb.setValue(_facesContext, new Double(668.666));
        assertEquals(new Double(668.666), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{arrI[0]}");
        vb.setValue(_facesContext, new Integer(669));
        assertEquals(new Integer(669), vb.getValue(_facesContext));

        // test with coercion
        vb = _application.createValueBinding("#{arrd[0]}");
        vb.setValue(_facesContext, new Integer(666));
        assertEquals(new Double(666), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{arri[0]}");
        vb.setValue(_facesContext, new Double(667.666));
        assertEquals(new Integer(667), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{arrD[0]}");
        vb.setValue(_facesContext, new Integer(668));
        assertEquals(new Double(668), vb.getValue(_facesContext));

        vb = _application.createValueBinding("#{arrI[0]}");
        vb.setValue(_facesContext, new Double(669.666));
        assertEquals(new Integer(669), vb.getValue(_facesContext));
    }
    
    public void testSetImplicitObject()
    {
        try 
        {
            _application.createValueBinding("#{cookie}").setValue(_facesContext, null);
            assertTrue(false);
        }
        catch (Exception e) {
            // ignore, error expected
        }
    }
    
    public void testSetManagedBean()
    {
        ValueBinding vb;
        
        vb = _application.createValueBinding("#{testBean_B}");
        try 
        {
            vb.setValue(_facesContext, new Double(5.5));
            assertTrue(false);
        }
        catch (Exception e)
        {
            // expected: error because Double cannot be coerced to Managed Bean B's class
        }

        // The above setValue must not have created the bean
        vb = _application.createValueBinding("#{sessionScope.testBean_B}");
        assertNull(vb.getValue(_facesContext));

        B b = new B();
        b.setName("differentName");
        vb = _application.createValueBinding("#{testBean_B}");
        vb.setValue(_facesContext, b);
        
        // check that the managed bean was created in the correct scope
        vb = _application.createValueBinding("#{sessionScope.testBean_B.name}");
        assertEquals("differentName", vb.getValue(_facesContext));
        
        vb = _application.createValueBinding("#{testBean_B.name}");
        assertEquals("differentName", vb.getValue(_facesContext));
    }
    
    public void testSetNullValue() 
    {
        ValueBinding vb;

        vb = _application.createValueBinding("#{testmap.o.obj}");
        assertNotNull(vb.getValue(_facesContext));
        vb.setValue(_facesContext, null);
        assertNull(vb.getValue(_facesContext));
    }
}
