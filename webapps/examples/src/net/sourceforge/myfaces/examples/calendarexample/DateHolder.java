package net.sourceforge.myfaces.examples.calendarexample;

import java.util.Date;

/**
 * DOCUMENT ME!
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DateHolder
{
    private Date _date = new Date();

    public Date getDate()
    {
        return _date;
    }

    public void setDate(Date date)
    {
        _date = date;
    }
}
