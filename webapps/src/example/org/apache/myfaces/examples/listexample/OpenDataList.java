package org.apache.myfaces.examples.listexample;

import java.util.*;
import javax.faces.model.*;

public class OpenDataList extends SortableList
{
    private DataModel data;
    private DataModel columnHeaders;

    private static final int SORT_ASCENDING = 1;
    private static final int SORT_DESCENDING = -1;

    public OpenDataList()
    {
        super(null);

        // create header info
        List headerList = new ArrayList();
        headerList.add("Index");
        headerList.add("Type");
        headerList.add("Model");
        columnHeaders = new ListDataModel(headerList);

        // create list of lists (data)
        List rowList = new ArrayList();
        for (int i = 100; i <= 999; i++)
        {
            List colList = new ArrayList();
            colList.add(new Integer(i));
            colList.add("Car Type " + i);
            colList.add((i%2==0) ? "blue" : "green");
            rowList.add(colList);
        }
        data = new ListDataModel(rowList);
    }

    //==========================================================================
    // Getters
    //==========================================================================

    public DataModel getData()
    {
        sort(getSort(), isAscending());
        return data;
    }

    public DataModel getColumnHeaders()
    {
        return columnHeaders;
    }

    //==========================================================================
    // Public Methods
    //==========================================================================

    public Object getColumnValue()
    {
        Object columnValue = null;
        if (data.isRowAvailable() && columnHeaders.isRowAvailable())
        {
            columnValue = ((List)data.getRowData()).get(columnHeaders.getRowIndex());
        }
        return columnValue;
    }

    //==========================================================================
    // Protected Methods
    //==========================================================================

    protected boolean isDefaultAscending(String sortColumn)
    {
        return true;
    }

    protected void sort(final String column, final boolean ascending)
    {
        if (column != null)
        {
            int columnIndex = ( (List) columnHeaders.getWrappedData()).indexOf(column);
            int direction = (ascending) ? SORT_ASCENDING : SORT_DESCENDING;
            sort(columnIndex, direction);
        }
    }

    protected void sort(final int columnIndex, final int direction)
    {
        Comparator comparator = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                int result = 0;
                Object column1 = ((List)o1).get(columnIndex);
                Object column2 = ((List)o2).get(columnIndex);
                if (column1 == null && column2 != null)
                    result = -1;
                else if (column1 == null && column2 == null)
                    result = 0;
                else if (column1 != null && column2 == null)
                    result = 1;
                else
                    result = ((Comparable)column1).compareTo(column2) * direction;
                return result;
            }
        };
        Collections.sort((List)data.getWrappedData(), comparator);
    }

}
