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
import java.util.Iterator;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * 
 * $Log$
 * Revision 1.4  2004/07/01 22:05:15  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/03/30 07:40:24  dave0000
 * *** empty log message ***
 *
 * Revision 1.2  2004/03/29 09:00:56  manolito
 * copyright header
 */
public class IteratorEnumeration implements Enumeration
{
    Iterator _it;

    public IteratorEnumeration(Iterator it)
    {
        _it = it;
    }

    public boolean hasMoreElements()
    {
        return _it.hasNext();
    }

    public Object nextElement()
    {
        return _it.next();
    }
}
