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
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.context.ResponseWriterWrapper;

import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DummyFormResponseWriterWrapper
        extends ResponseWriterWrapper
        implements DummyFormResponseWriter
{
    //private static final Log log = LogFactory.getLog(DummyFormResponseWriterWrapper.class);

    private Set _dummyFormParams = new HashSet();

    public DummyFormResponseWriterWrapper(ResponseWriter responseWriter)
    {
        super(responseWriter);
    }

    private DummyFormResponseWriterWrapper(ResponseWriter responseWriter,
                                           Set dummyFormParams)
    {
        super(responseWriter);
        _dummyFormParams = dummyFormParams;
    }

    public ResponseWriter cloneWithWriter(Writer writer)
    {
        return new DummyFormResponseWriterWrapper(_responseWriter.cloneWithWriter(writer),
                                                  _dummyFormParams);
    }

    public void endDocument() throws IOException
    {
        super.flush();
        DummyFormUtils.writeDummyForm(_responseWriter, _dummyFormParams);
        super.endDocument();
    }

    public void setWriteDummyForm(boolean writeDummyForm)
    {
        if (writeDummyForm == false)
        {
            throw new IllegalArgumentException();
        }
    }

    public String getDummyFormName()
    {
        return DummyFormUtils.DUMMY_FORM_NAME;
    }

    public void addDummyFormParameter(String paramName)
    {
        _dummyFormParams.add(paramName);
    }

}
