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
package net.sourceforge.myfaces.confignew.element;

import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/07/01 22:05:04  mwessendorf
 * ASF switch
 *
 * Revision 1.2  2004/06/16 23:02:22  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.1.2.1  2004/06/16 02:07:22  o_rossmueller
 * get navigation rules from RuntimeConfig
 * refactored all remaining usages of MyFacesFactoryFinder to use RuntimeConfig
 *
 * Revision 1.1  2004/05/17 14:28:26  manolito
 * new configuration concept
 *
 */
public interface ManagedBean
{
    // <!ELEMENT managed-bean (description*, display-name*, icon*, managed-bean-name, managed-bean-class, managed-bean-scope, (managed-property* | map-entries | list-entries))>

    public static final int INIT_MODE_NO_INIT = 0;
    public static final int INIT_MODE_PROPERTIES = 1;
    public static final int INIT_MODE_MAP = 2;
    public static final int INIT_MODE_LIST = 3;

    public String getManagedBeanName();
    public String getManagedBeanClassName();
    public Class getManagedBeanClass();
    public String getManagedBeanScope();

    public int getInitMode();

    /**
     * @return Iterator over {@link ManagedProperty} entries
     */
    public Iterator getManagedProperties();

    public MapEntries getMapEntries();

    public ListEntries getListEntries();
}
