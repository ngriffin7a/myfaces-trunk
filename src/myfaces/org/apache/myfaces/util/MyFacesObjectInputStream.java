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
package net.sourceforge.myfaces.util;

import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;

/**
 * Tried to deploy v0.4.2 on JBoss 3.2.1 and had a classloading problem again.
 * The problem seemed to be with JspInfo, line 98. We are using an
 * ObjectInputStream Class, which then cannot find the classes to deserialize
 * the input stream.  The solution appears to be to subclass ObjectInputStream
 * (eg. CustomInputStream), and specify a different class-loading mechanism.
 *
 * @author Robert Gothan <robert@funkyjazz.net> (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesObjectInputStream
    extends ObjectInputStream
{
    public MyFacesObjectInputStream(InputStream in) throws IOException
    {
        super(in);
    }

    protected Class resolveClass(ObjectStreamClass desc)
        throws IOException, ClassNotFoundException
    {
        return Class.forName(desc.getName(),
                             true,
                             Thread.currentThread().getContextClassLoader());
    }
}
