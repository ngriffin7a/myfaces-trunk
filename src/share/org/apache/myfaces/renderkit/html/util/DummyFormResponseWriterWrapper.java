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
