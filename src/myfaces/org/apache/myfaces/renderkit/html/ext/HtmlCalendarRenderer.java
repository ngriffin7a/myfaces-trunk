package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlLinkRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.component.ext.HtmlInputCalendar;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlCalendarRenderer
        extends HtmlRenderer
{

    //private static Log log = LogFactory.getLog(HtmlCalendarRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlInputCalendar.class);

        HtmlInputCalendar inputCalendar = (HtmlInputCalendar) component;

        Locale currentLocale = facesContext.getViewRoot().getLocale();

        Calendar timeKeeper = Calendar.getInstance(currentLocale);
        timeKeeper.setTime((Date) ((UIInput) component).getValue());

        DateFormatSymbols symbols = new DateFormatSymbols(currentLocale);

        String[] weekdays = mapWeekdays(symbols);
        String[] months = mapMonths(symbols);

        int lastDayInMonth = timeKeeper.getActualMaximum(Calendar.DAY_OF_MONTH);

        int currentDay = timeKeeper.get(Calendar.DAY_OF_MONTH);

        if (currentDay > lastDayInMonth)
            currentDay = lastDayInMonth;

        timeKeeper.set(Calendar.DAY_OF_MONTH, 1);

        int weekDayOfFirstDayOfMonth = mapCalendarDayToCommonDay(timeKeeper.get(Calendar.DAY_OF_WEEK));

        int weekStartsAtDayIndex = mapCalendarDayToCommonDay(timeKeeper.getFirstDayOfWeek());

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        writer.startElement(HTML.TABLE_ELEM, component);
        HTMLUtil.renderHTMLAttributes(writer, component, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, component, HTML.EVENT_HANDLER_ATTRIBUTES);
        writer.flush();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        writer.startElement(HTML.TR_ELEM, component);

        if(inputCalendar.getMonthYearRowClass() != null)
            writer.writeAttribute(HTML.CLASS_ATTR, inputCalendar.getMonthYearRowClass(), null);

        writeMonthYearHeader(facesContext, writer, inputCalendar, timeKeeper,
                currentDay, weekdays, months);

        writer.endElement(HTML.TR_ELEM);

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        writer.startElement(HTML.TR_ELEM, component);

        if(inputCalendar.getWeekRowClass() != null)
            writer.writeAttribute(HTML.CLASS_ATTR, inputCalendar.getWeekRowClass(), null);

        writeWeekDayNameHeader(weekStartsAtDayIndex, weekdays,
                facesContext, writer, inputCalendar);

        writer.endElement(HTML.TR_ELEM);

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        writeDays(facesContext, writer, inputCalendar, timeKeeper,
                currentDay, weekStartsAtDayIndex, weekDayOfFirstDayOfMonth,
                lastDayInMonth, weekdays);

        writer.endElement(HTML.TABLE_ELEM);
    }

    private void writeMonthYearHeader(FacesContext facesContext, ResponseWriter writer, UIInput inputComponent, Calendar timeKeeper,
                                      int currentDay, String[] weekdays,
                                      String[] months)
            throws IOException
    {
        Calendar cal = shiftMonth(facesContext, timeKeeper, currentDay, -1);

        writeCell(facesContext, writer, inputComponent, "<", cal.getTime(), null);

        writer.startElement(HTML.TD_ELEM, inputComponent);
        writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(weekdays.length - 2), null);
        writer.writeText(months[timeKeeper.get(Calendar.MONTH)] + " " + timeKeeper.get(Calendar.YEAR), null);
        writer.endElement(HTML.TD_ELEM);

        cal = shiftMonth(facesContext, timeKeeper, currentDay, 1);

        writeCell(facesContext, writer, inputComponent, ">", cal.getTime(), null);
    }

    private Calendar shiftMonth(FacesContext facesContext,
                                Calendar timeKeeper, int currentDay, int shift)
    {
        Calendar cal = copyCalendar(facesContext, timeKeeper);

        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + shift);

        if(currentDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            currentDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, currentDay);
        return cal;
    }

    private Calendar copyCalendar(FacesContext facesContext, Calendar timeKeeper)
    {
        Calendar cal = Calendar.getInstance(facesContext.getViewRoot().getLocale());
        cal.set(Calendar.YEAR, timeKeeper.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, timeKeeper.get(Calendar.MONTH));
        cal.set(Calendar.HOUR_OF_DAY, timeKeeper.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, timeKeeper.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, timeKeeper.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, timeKeeper.get(Calendar.MILLISECOND));
        return cal;
    }

    private void writeWeekDayNameHeader(int weekStartsAtDayIndex, String[] weekdays, FacesContext facesContext, ResponseWriter writer, UIInput inputComponent)
            throws IOException
    {
        for (int i = weekStartsAtDayIndex; i < weekdays.length; i++)
            writeCell(facesContext,
                    writer, inputComponent, weekdays[i], null, null);

        for (int i = 0; i < weekStartsAtDayIndex; i++)
            writeCell(facesContext, writer,
                    inputComponent, weekdays[i], null, null);
    }

    private void writeDays(FacesContext facesContext, ResponseWriter writer,
                           HtmlInputCalendar inputComponent, Calendar timeKeeper, int currentDay, int weekStartsAtDayIndex,
                           int weekDayOfFirstDayOfMonth, int lastDayInMonth, String[] weekdays)
            throws IOException
    {
        Calendar cal;

        int space = (weekStartsAtDayIndex < weekDayOfFirstDayOfMonth) ? (weekDayOfFirstDayOfMonth - weekStartsAtDayIndex)
                : (weekdays.length - weekStartsAtDayIndex + weekDayOfFirstDayOfMonth);

        if (space == weekdays.length)
            space = 0;

        int columnIndexCounter = 0;

        for (int i = 0; i < space; i++)
        {
            if (columnIndexCounter == 0)
            {
                writer.startElement(HTML.TR_ELEM, inputComponent);
            }

            writeCell(facesContext, writer, inputComponent, "",
                    null, inputComponent.getDayCellClass());
            columnIndexCounter++;
        }

        for (int i = 0; i < lastDayInMonth; i++)
        {
            if (columnIndexCounter == 0)
            {
                writer.startElement(HTML.TR_ELEM, inputComponent);
            }

            cal = copyCalendar(facesContext, timeKeeper);
            cal.set(Calendar.DAY_OF_MONTH, i + 1);

            String cellStyle = inputComponent.getDayCellClass();

            if((currentDay - 1) == i)
                cellStyle = inputComponent.getCurrentDayCellClass();

            writeCell(facesContext, writer,
                    inputComponent, String.valueOf(i + 1), cal.getTime(),
                    cellStyle);

            columnIndexCounter++;

            if (columnIndexCounter == weekdays.length)
            {
                writer.endElement(HTML.TR_ELEM);
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);
                columnIndexCounter = 0;
            }
        }

        if (columnIndexCounter != 0)
        {
            for (int i = columnIndexCounter; i < weekdays.length; i++)
            {
                writeCell(facesContext, writer,
                        inputComponent, "", null, inputComponent.getDayCellClass());
            }

            writer.endElement(HTML.TR_ELEM);
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        }
    }

    private void writeCell(FacesContext facesContext,
                           ResponseWriter writer, UIInput component, String content,
                           Date valueForLink, String styleClass)
            throws IOException
    {
        writer.startElement(HTML.TD_ELEM, component);

        if (styleClass != null)
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        if (valueForLink == null)
            writer.writeText(content, JSFAttr.VALUE_ATTR);
        else
        {
            writeLink(content, component, facesContext, valueForLink);

        }

        writer.endElement(HTML.TD_ELEM);
    }

    private void writeLink(String content, UIInput component, FacesContext facesContext, Date valueForLink)
            throws IOException
    {
        UICommand command = new UICommand();
        command.setValue(content);

        UIParameter param = new UIParameter();

        param.setName(component.getId());

        Converter converter = new CalendarDateTimeConverter();

        if (component.getConverter() != null)
            converter = component.getConverter();

        param.setValue(converter.getAsString(facesContext, component, valueForLink));

        command.getChildren().add(param);

        HtmlLinkRenderer renderer = new HtmlLinkRenderer();

        renderer.renderCommandLinkStart(facesContext, command);
        renderer.renderLinkEnd(facesContext, command);
    }

    private int mapCalendarDayToCommonDay(int day)
    {
        switch (day)
        {
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }

    private String[] mapWeekdays(DateFormatSymbols symbols)
    {
        String[] weekdays = new String[7];

        String[] localeWeekdays = symbols.getShortWeekdays();

        weekdays[0] = localeWeekdays[Calendar.MONDAY];
        weekdays[1] = localeWeekdays[Calendar.TUESDAY];
        weekdays[2] = localeWeekdays[Calendar.WEDNESDAY];
        weekdays[3] = localeWeekdays[Calendar.THURSDAY];
        weekdays[4] = localeWeekdays[Calendar.FRIDAY];
        weekdays[5] = localeWeekdays[Calendar.SATURDAY];
        weekdays[6] = localeWeekdays[Calendar.SUNDAY];

        return weekdays;
    }

    private String[] mapMonths(DateFormatSymbols symbols)
    {
        String[] months = new String[12];

        String[] localeMonths = symbols.getMonths();

        months[0] = localeMonths[Calendar.JANUARY];
        months[1] = localeMonths[Calendar.FEBRUARY];
        months[2] = localeMonths[Calendar.MARCH];
        months[3] = localeMonths[Calendar.APRIL];
        months[4] = localeMonths[Calendar.MAY];
        months[5] = localeMonths[Calendar.JUNE];
        months[6] = localeMonths[Calendar.JULY];
        months[7] = localeMonths[Calendar.AUGUST];
        months[8] = localeMonths[Calendar.SEPTEMBER];
        months[9] = localeMonths[Calendar.OCTOBER];
        months[10] = localeMonths[Calendar.NOVEMBER];
        months[11] = localeMonths[Calendar.DECEMBER];

        return months;
    }


    public void decode(FacesContext facesContext, UIComponent component)
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlInputCalendar.class);

        HtmlRendererUtils.decodeUIInput(facesContext, (UIInput) component);
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlInputCalendar.class);

        UIInput uiInput = (UIInput) uiComponent;

        Converter converter = uiInput.getConverter();

        if(converter==null)
            converter = new CalendarDateTimeConverter();

        if (!(submittedValue instanceof String))
        {
            throw new IllegalArgumentException("Submitted value of type String expected");
        }

        return converter.getAsObject(facesContext, uiComponent, (String) submittedValue);
    }

    public static class CalendarDateTimeConverter implements Converter
    {

        public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
        {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
                    facesContext.getViewRoot().getLocale());

            try
            {
                return dateFormat.parse(s);
            }
            catch (ParseException e)
            {
                ConverterException ex = new ConverterException(e);
                throw ex;
            }
        }

        public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
        {
            Date date = (Date) o;

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
                    facesContext.getViewRoot().getLocale());

            return dateFormat.format(date);
        }

    }
}
