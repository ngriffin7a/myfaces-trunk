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
package javax.faces.component;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface EditableValueHolder
        extends ValueHolder
{
    public java.lang.Object getSubmittedValue();

    public void setSubmittedValue(java.lang.Object submittedValue);

    public boolean isLocalValueSet();

    public void setLocalValueSet(boolean localValueSet);

    public boolean isValid();

    public void setValid(boolean valid);

    public boolean isRequired();

    public void setRequired(boolean required);

    public boolean isImmediate();

    public void setImmediate(boolean immediate);

    public javax.faces.el.MethodBinding getValidator();

    public void setValidator(javax.faces.el.MethodBinding validatorBinding);

    public javax.faces.el.MethodBinding getValueChangeListener();

    public void setValueChangeListener(javax.faces.el.MethodBinding valueChangeMethod);

    public void addValidator(javax.faces.validator.Validator validator);

    public javax.faces.validator.Validator[] getValidators();

    public void removeValidator(javax.faces.validator.Validator validator);

    public void addValueChangeListener(javax.faces.event.ValueChangeListener listener);

    public javax.faces.event.ValueChangeListener[] getValueChangeListeners();

    public void removeValueChangeListener(javax.faces.event.ValueChangeListener listener);
}
