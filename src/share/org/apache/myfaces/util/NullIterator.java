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

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Iterator without elements
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/07/01 22:01:13  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/03/30 15:37:17  manolito
 * moved to share
 *
 */
public final class NullIterator implements Iterator
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final NullIterator INSTANCE = new NullIterator();

    //~ Methods ------------------------------------------------------------------------------------

    public static final Iterator instance()
    {
        return INSTANCE;
    }

    public boolean hasNext()
    {
        return false;
    }

    public Object next()
    {
        throw new NoSuchElementException();
    }

    public void remove()
    {
        throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
    }
}
