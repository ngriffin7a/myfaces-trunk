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
package net.sourceforge.myfaces.custom.date;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

/**
 * $Log$
 * Revision 1.3  2004/07/18 03:08:23  svieujot
 * inputDate : add a type="date|time|both" similar as f:convertDateTime
 *
 * Revision 1.2  2004/07/17 21:03:05  svieujot
 * Clean code
 *
 * Revision 1.1  2004/07/17 20:52:53  svieujot
 * First version of an x:inputDate component
 *
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDateRenderer extends HtmlRenderer {
    private static final String ID_DAY_POSTFIX = ".day";
    private static final String ID_MONTH_POSTFIX = ".month";
    private static final String ID_YEAR_POSTFIX = ".year";
    private static final String ID_HOURS_POSTFIX = ".hours";
    private static final String ID_MINUTES_POSTFIX = ".minutes";
    private static final String ID_SECONDS_POSTFIX = ".seconds";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlInputDate.class);

        HtmlInputDate inputDate = (HtmlInputDate) uiComponent;
        Date date = inputDate.getDate();
        String type = inputDate.getType();
        String clientId = uiComponent.getClientId(facesContext);
        Locale currentLocale = facesContext.getViewRoot().getLocale();

        Calendar calendar = null;
        if (date != null) {
            calendar = Calendar.getInstance(currentLocale);
            calendar.setTime( date );
        }

        boolean disabled = false; // TODO : implement & use isDisabled(facesContext, uiComponent);

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        if( ! type.equals("time")){
	        encodeInputDay(uiComponent, writer, clientId, calendar, disabled);
	        encodeInputMonth(uiComponent, writer, clientId, calendar, currentLocale, disabled);
	        encodeInputYear(uiComponent, writer, clientId, calendar, disabled);
        }
        if( type.equals("both") ){
            writer.write(" ");
        }
        if( ! type.equals("date")){
	        encodeInputHours(uiComponent, writer, clientId, calendar, disabled);
	        writer.write(":");
	        encodeInputMinutes(uiComponent, writer, clientId, calendar, disabled);
	        writer.write(":");
	        encodeInputSeconds(uiComponent, writer, clientId, calendar, disabled);
        }
    }
    
    private static void encodeInputField(UIComponent uiComponent, ResponseWriter writer, String id, int value, int size, boolean disabled)  throws IOException {
        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES); // TODO Check that

		if (disabled) {
		    writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
		}

		writer.writeAttribute(HTML.ID_ATTR, id, null);
		writer.writeAttribute(HTML.NAME_ATTR, id, null);
		writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(size), null);
		writer.writeAttribute(HTML.MAXLENGTH_ATTR, Integer.toString(size), null);
		if (value != -1) {
		    writer.writeAttribute(HTML.VALUE_ATTR, Integer.toString(value), null);
		}
		writer.endElement(HTML.INPUT_ELEM);
    }
    
    private static void encodeInputDay(UIComponent uiComponent, ResponseWriter writer, String clientId, Calendar calendar, boolean disabled)
            throws IOException {
        encodeInputField(uiComponent, writer, clientId + ID_DAY_POSTFIX, calendar == null ? -1 : calendar.get(Calendar.DAY_OF_MONTH), 2, disabled);
    }

    private static void encodeInputMonth(UIComponent uiComponent, ResponseWriter writer, String clientId, Calendar calendar, Locale currentLocale,
            boolean disabled) throws IOException {
        writer.startElement(HTML.SELECT_ELEM, uiComponent);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + ID_MONTH_POSTFIX, null);
        writer.writeAttribute(HTML.SIZE_ATTR, "1", null);

        if (disabled) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        int selectedMonth = calendar == null ? -1 : calendar.get(Calendar.MONTH);

        String[] months = mapMonths(new DateFormatSymbols(currentLocale));
        for (int i = 0; i < months.length; i++) {
            String monthName = months[i];
            String monthNumber = Integer.toString(i);

            writer.write("\t\t");
            writer.startElement(HTML.OPTION_ELEM, null);
            writer.writeAttribute(HTML.VALUE_ATTR, monthNumber, null);

            if (i == selectedMonth)
                writer.writeAttribute(HTML.SELECTED_ATTR, HTML.SELECTED_ATTR, null);

            writer.writeText(monthName, null);

            writer.endElement(HTML.OPTION_ELEM);
        }

        // bug #970747: force separate end tag
        writer.writeText("", null);
        writer.endElement(HTML.SELECT_ELEM);
    }

    private static void encodeInputYear(UIComponent uiComponent, ResponseWriter writer, String clientId, Calendar calendar, boolean disabled) throws IOException {
        encodeInputField(uiComponent, writer, clientId + ID_YEAR_POSTFIX, calendar == null ? -1 : calendar.get(Calendar.YEAR), 4, disabled);
    }
    
    private static void encodeInputHours(UIComponent uiComponent, ResponseWriter writer, String clientId, Calendar calendar, boolean disabled) throws IOException {
        encodeInputField(uiComponent, writer, clientId + ID_HOURS_POSTFIX, calendar == null ? -1 : calendar.get(Calendar.HOUR_OF_DAY), 2, disabled);
    }
    
    private static void encodeInputMinutes(UIComponent uiComponent, ResponseWriter writer, String clientId, Calendar calendar, boolean disabled) throws IOException {
        encodeInputField(uiComponent, writer, clientId + ID_MINUTES_POSTFIX, calendar == null ? -1 : calendar.get(Calendar.MINUTE), 2, disabled);
    }
    
    private static void encodeInputSeconds(UIComponent uiComponent, ResponseWriter writer, String clientId, Calendar calendar, boolean disabled) throws IOException {
        encodeInputField(uiComponent, writer, clientId + ID_SECONDS_POSTFIX, calendar == null ? -1 : calendar.get(Calendar.SECOND), 2, disabled);
    }
    
    private static String[] mapMonths(DateFormatSymbols symbols) {
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

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlInputDate.class);

        HtmlInputDate inputDate = (HtmlInputDate) uiComponent;
        Date initialDate = inputDate.getDate();
        String clientId = inputDate.getClientId(facesContext);
        String type = inputDate.getType();
        Locale currentLocale = facesContext.getViewRoot().getLocale();
        Calendar newValue = Calendar.getInstance(currentLocale);
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();

        if( ! type.equals("both") ){
            Date date = inputDate.getDate();
            if( date != null ){  // try to change only the specified part (time or date), and keep the initial value for the not specified part
                newValue.setTime( date );
            }
        }

        if( ! type.equals( "time" ) ){
            String sDay = (String) requestMap.get(clientId + ID_DAY_POSTFIX);
            String sMonth = (String) requestMap.get(clientId + ID_MONTH_POSTFIX);
            String sYear = (String) requestMap.get(clientId + ID_YEAR_POSTFIX);
        
            newValue.set(Calendar.YEAR, Integer.parseInt(sYear));
            newValue.set(Calendar.DAY_OF_YEAR, 1);
            newValue.set(Calendar.MONTH, Integer.parseInt(sMonth));
            newValue.set(Calendar.DAY_OF_MONTH, Integer.parseInt(sDay));
        }
        
        if( ! type.equals( "date" ) ){
            String sHours = (String) requestMap.get(clientId + ID_HOURS_POSTFIX);
            String sMinutes = (String) requestMap.get(clientId + ID_MINUTES_POSTFIX);
            String sSeconds = (String) requestMap.get(clientId + ID_SECONDS_POSTFIX);
        
            newValue.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sHours));
            newValue.set(Calendar.MINUTE, Integer.parseInt(sMinutes));
            newValue.set(Calendar.SECOND, Integer.parseInt(sSeconds));            
        }

        
        inputDate.setSubmittedValue(newValue.getTime());
    }
}