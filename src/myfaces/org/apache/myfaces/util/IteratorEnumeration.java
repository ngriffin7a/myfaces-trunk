package net.sourceforge.myfaces.util;

import java.util.Enumeration;
import java.util.Iterator;


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
