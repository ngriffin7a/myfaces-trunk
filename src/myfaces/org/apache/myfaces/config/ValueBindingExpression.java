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
package net.sourceforge.myfaces.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Wrapper for value binding expression Strings.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValueBindingExpression
{
    private static final Log log = LogFactory.getLog(ValueBindingExpression.class);

    private String _valueBindingExpression;

    public ValueBindingExpression(String valueBindingExpression)
    {
        _valueBindingExpression = valueBindingExpression;
    }

    public ValueBinding getValueBinding(FacesContext facesContext)
    {
        return getValueBinding(facesContext, null);
    }

    /**
     * @param facesContext
     * @param desiredType   Class, this value binding is checked against, or null for no check
     * @return the ValueBinding
     */
    public ValueBinding getValueBinding(FacesContext facesContext, Class desiredType)
    {
        ValueBinding vb = facesContext.getApplication().createValueBinding(_valueBindingExpression);
        if (desiredType != null)
        {
            if (!desiredType.isAssignableFrom(vb.getType(facesContext)))
            {
                log.error("Type of value binding " + vb.getExpressionString() + " is not of desired type for map entry in faces-config.");
            }
        }
        return vb;
    }
}
