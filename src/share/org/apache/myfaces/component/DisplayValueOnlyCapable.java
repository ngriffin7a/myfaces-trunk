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
package org.apache.myfaces.component;

/**
 * Behavioral interface.
 * By default, displayValueOnly is false, and the components have the default behaviour.
 * When displayValueOnly is true, the renderer should not render any input widget.
 * Only the text corresponding to the component's value should be rendered instead.
 * 
 * @author Sylvain Vieujot & Martin Marinschek (latest modification by $Author: svieujot $)
 * @version $Revision: 169739 $ $Date: 2005-05-12 02:45:14 +0200 (Thu, 12 May 2005) $
 */
public interface DisplayValueOnlyCapable
{
    public boolean isSetDisplayValueOnly();
    public boolean isDisplayValueOnly();
    public void setDisplayValueOnly(boolean displayValueOnly);

    public String getDisplayValueOnlyStyle();
    public void setDisplayValueOnlyStyle(String style);
	
    public String getDisplayValueOnlyStyleClass();
    public void setDisplayValueOnlyStyleClass(String styleClass);
}
