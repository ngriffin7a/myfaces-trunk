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
package net.sourceforge.myfaces.util;

import java.util.Enumeration;

/**
 * Enumeration without elements
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/07/01 22:01:13  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/04/15 04:32:58  dave0000
 * make SessionMap request session every time
 *
 *
 */
public final class NullEnumeration implements Enumeration
{
    private static final NullEnumeration s_nullEnumeration = new NullEnumeration();

    public static final NullEnumeration instance()
    {
        return s_nullEnumeration;
    }

    public boolean hasMoreElements()
    {
        return false;
    }

    public Object nextElement()
    {
        throw new UnsupportedOperationException("NullEnumeration has no elements");
    }
}
