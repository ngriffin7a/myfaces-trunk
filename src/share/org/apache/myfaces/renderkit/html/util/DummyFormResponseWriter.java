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
package org.apache.myfaces.renderkit.html.util;

/**
 * Myfaces extension for ResponseWriters that support the automatical rendering
 * of a dummy form when endDocument is called.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface DummyFormResponseWriter
{
    /**
     * Switch on/off the writing of a dummy form in endDocument.
     * @param writeDummyForm
     */
    public void setWriteDummyForm(boolean writeDummyForm);

    /**
     * @return name of the dummy form, that will be rendered
     */
    public String getDummyFormName();

    /**
     * Adds a parameter that will be rendered as a hidden input within the dummy form.
     */
    public void addDummyFormParameter(String paramName);
}
