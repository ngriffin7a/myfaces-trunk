package org.apache.myfaces.renderkit.html.ext;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */

import org.apache.myfaces.renderkit.html.HtmlTableRendererBase;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIData;
import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.renderkit.html.HTML;

public class HtmlTableRenderer
        extends HtmlTableRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlTableRenderer.class);

    protected void renderRowStart(
            FacesContext facesContext,
            ResponseWriter writer,
            UIData uiData,
            String rowStyleClass) throws IOException
    {
        super.renderRowStart(facesContext, writer, uiData, rowStyleClass);

        // get event handlers from component
        HtmlDataTable table = (HtmlDataTable) uiData;
        String rowOnMouseOver = table.getRowOnMouseOver();
        String rowOnMouseOut = table.getRowOnMouseOut();

        // render onmouseover and onmouseout handlers if not null
        if (rowOnMouseOver != null)
        {
            writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, rowOnMouseOver, null);
        }
        if (rowOnMouseOut != null)
        {
            writer.writeAttribute(HTML.ONMOUSEOUT_ATTR, rowOnMouseOut, null);
        }
    }
}
