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
        
        vb     = _application.createValueBinding(
            "#{testmap.list[4].list[testmap.list[4][1][0][testmap.list[4][0][1]]][0][0]}");
        vb.setValue(_facesContext, "zzz");
        assertSame("zzz", vb.getValue(_facesContext));
    }
    
    public void testSetRootValue()
    {
        ValueBinding vb;

        // set to a new vatiable
        vb = _application.createValueBinding("#{newVar}");
        vb.setValue(_facesContext, "test-value");
        assertSame("test-value", vb.getValue(_facesContext));

        // update existing variable
        vb.setValue(_facesContext, "another-value");
        assertSame("another-value", vb.getValue(_facesContext));
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
}
