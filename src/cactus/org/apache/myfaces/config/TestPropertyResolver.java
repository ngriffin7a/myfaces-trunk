/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package net.sourceforge.myfaces.config;

import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class TestPropertyResolver extends PropertyResolver
{

    private PropertyResolver delegate;


    public TestPropertyResolver()
    {
    }


    public TestPropertyResolver(PropertyResolver delegate)
    {
        this.delegate = delegate;
    }


    // METHODS
    public Class getType(Object base, int index) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getType(base, index);
    }


    public Class getType(Object base, Object property) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getType(base, property);
    }


    public Object getValue(Object base, int index) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getValue(base, index);
    }


    public Object getValue(Object base, Object property) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.getValue(base, property);
    }


    public boolean isReadOnly(Object base, int index) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.isReadOnly(base, index);
    }


    public boolean isReadOnly(Object base, Object property) throws EvaluationException, PropertyNotFoundException
    {
        return delegate.isReadOnly(base, property);
    }


    public void setValue(Object base, int index, Object value) throws EvaluationException, PropertyNotFoundException
    {
        delegate.setValue(base, index, value);
    }


    public void setValue(Object base, Object property, Object value) throws EvaluationException, PropertyNotFoundException
    {
        delegate.setValue(base, property, value);
    }


    public PropertyResolver getDelegate()
    {
        return delegate;
    }
}
