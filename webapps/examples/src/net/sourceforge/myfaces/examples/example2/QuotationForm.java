/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.examples.example2;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl
 * @version
 */
public class QuotationForm
{
    private String _text = "QuotationTest";
    private String _quoteChar;
    private String[] _selectManyValues;

    private SelectItem[] _selectItems = null;
    private List _selectManyItems = null;



    public QuotationForm()
    {
    }


    public String getText()
    {
        return _text;
    }

    public void setText(String text)
    {
        _text = text;
    }

    public String getQuoteChar()
    {
        return _quoteChar;
    }

    public void setQuoteChar(String value)
    {
        _quoteChar = value;
    }

    public SelectItem[] getSelectOneItems()
    {
        if (_selectItems == null)
        {
            _selectItems = new SelectItem[2];
            _selectItems[0] = new SelectItem("*", "Asterisk");
            _selectItems[1] = new SelectItem("+", "Plus");
        }
        return _selectItems;
    }




    public String[] getSelectManyValues()
    {
        return _selectManyValues;
    }

    public void setSelectManyValues(String[] value)
    {
        _selectManyValues = value;
    }

    public List getSelectManyItems()
    {
        if (_selectManyItems == null)
        {
            _selectManyItems = new ArrayList();
            _selectManyItems.add(new SelectItem("\"", "Double"));
            _selectManyItems.add(new SelectItem("'", "Single"));
            _selectManyItems.add(new SelectItem("*", "Asterisk"));
            _selectManyItems.add(new SelectItem("+", "Plus"));
            _selectManyItems.add(new SelectItem("-", "Hyphen"));
        }
        return _selectManyItems;
    }


    public void quote()
    {
        if (_quoteChar != null)
        {
            _text = _quoteChar + _text + _quoteChar;
        }
    }

    public void unquote()
    {
        if (_selectManyValues != null)
        {
            for (int i = 0; i < _selectManyValues.length; i++)
            {
                unquote(_selectManyValues[i]);
            }
        }
    }

    private void unquote(String quoteChar)
    {
        if (quoteChar != null && quoteChar.length() > 0)
        {
            if (_text.startsWith(quoteChar))
            {
                _text = _text.substring(1);
            }

            if (_text.endsWith(quoteChar))
            {
                _text = _text.substring(0, _text.length() - 1);
            }
        }
    }

}

