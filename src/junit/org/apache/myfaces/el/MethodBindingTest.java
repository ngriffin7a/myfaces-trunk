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

        mb = _application.createMethodBinding("#{ testmap  [  0  ]  [  1  ] . toString}", new Class[] {});
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
