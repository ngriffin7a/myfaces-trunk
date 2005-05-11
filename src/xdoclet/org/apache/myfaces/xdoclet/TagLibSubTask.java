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
package org.apache.myfaces.xdoclet;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

/**
 * @ant.element
 *     name="taglib"
 *     parent="xdoclet.DocletTask"
 *
 * @author  <a href="mailto:Jiri.Zaloudek@ivancice.cz">Jiri Zaloudek</a> (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TagLibSubTask extends TemplateSubTask {

    /** Creates a new instance of TagSubTask */
    public TagLibSubTask() {
        setDestinationFile("wap_faces.tld");
        setTemplateURL(getClass().getResource("/org/apache/myfaces/xdoclet/resources/tld.xdt"));
    }

    public void validateOptions() throws XDocletException {
        super.validateOptions();
        if (getDestinationFile() == null) {
            throw new XDocletException("Ant Doclet Subtaksk TagSubTaks: You have to set attribute 'destinationFile'.");
        }
    }

}
