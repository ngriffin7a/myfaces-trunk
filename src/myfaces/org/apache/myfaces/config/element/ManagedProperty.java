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
package net.sourceforge.myfaces.config.element;

import javax.faces.el.ValueBinding;

import net.sourceforge.myfaces.config.element.ListEntries;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/07/07 00:25:04  o_rossmueller
 * tidy up config/confignew package (moved confignew classes to package config)
 *
 * Revision 1.3  2004/07/01 22:05:04  mwessendorf
 * ASF switch
 *
 * Revision 1.2  2004/06/16 23:02:22  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.1.2.1  2004/06/13 15:59:06  o_rossmueller
 * started integration of new config mechanism:
 * - factories
 * - components
 * - render kits
 * - managed beans + managed properties (no list/map initialization)
 *
 * Revision 1.1  2004/05/17 14:28:26  manolito
 * new configuration concept
 *
 */
public interface ManagedProperty
{
    // <!ELEMENT managed-property (description*, display-name*, icon*, property-name, property-class?, (map-entries|null-value|value|list-entries))>

    public static final int TYPE_MAP = 1;
    public static final int TYPE_NULL = 2;
    public static final int TYPE_VALUE = 3;
    public static final int TYPE_LIST = 4;

    public String getPropertyName();
    public String getPropertyClass();

    public int getType();
    public MapEntries getMapEntries();
    public String getValue();
    public ListEntries getListEntries();

    // used to cache ValueBinding instances
    public ValueBinding getValueBinding();
    public void setValueBinding(ValueBinding valueBinding);
}
