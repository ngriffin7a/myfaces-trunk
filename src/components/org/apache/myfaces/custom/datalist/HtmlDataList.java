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
package net.sourceforge.myfaces.custom.datalist;

import java.util.Iterator;
import javax.faces.component.UIData;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataList
        extends UIData
{

    public void processDecodes(FacesContext context)
    {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0)
        {
            last = getRowCount();
        }
        else
        {
            last = first + rows;
        }
        for (int rowIndex = first; rowIndex < last; rowIndex++)
        {
            setRowIndex(rowIndex);
            if (isRowAvailable())
            {
                for (Iterator it = getChildren().iterator(); it.hasNext();)
                {
                    UIComponent child = (UIComponent)it.next();
                    if (!child.isRendered())
                    {
                        continue;
                    }
                    child.processDecodes(context);
                }
            }
        }
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlDataList";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.List";

    private String _layout = null;
    private String _rowIndexVar = null;
    private String _rowCountVar = null;

    public HtmlDataList()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public String getLayout()
    {
        if (_layout != null) return _layout;
        ValueBinding vb = getValueBinding("layout");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRowIndexVar(String rowIndexVar)
    {
        _rowIndexVar = rowIndexVar;
    }

    public String getRowIndexVar()
    {
        if (_rowIndexVar != null) return _rowIndexVar;
        ValueBinding vb = getValueBinding("rowIndexVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRowCountVar(String rowCountVar)
    {
        _rowCountVar = rowCountVar;
    }

    public String getRowCountVar()
    {
        if (_rowCountVar != null) return _rowCountVar;
        ValueBinding vb = getValueBinding("rowCountVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _layout;
        values[2] = _rowIndexVar;
        values[3] = _rowCountVar;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _layout = (String)values[1];
        _rowIndexVar = (String)values[2];
        _rowCountVar = (String)values[3];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
