/*
 * $Id$
 *
 * Copyright 2004 Oliver Rossmueller
 *
 * This file is part of tuxerra.
 *
 * tuxerra is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 * tuxerra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with welofunc; if not, mailto:oliver@tuxerra.com or have a look at
 * http://www.gnu.org/licenses/licenses.html#GPL
 */
package net.sourceforge.myfaces.renderkit.html;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class Bug972165Bean
{
    private boolean checkDisabled;
    private boolean checkCommand;


    public boolean isCheckDisabled()
    {
        return checkDisabled;
    }


    public void setCheckDisabled(boolean checkDisabled)
    {
        this.checkDisabled = checkDisabled;
    }


    public boolean isCheckCommand()
    {
        return checkCommand;
    }


    public void setCheckCommand(boolean checkCommand)
    {
        this.checkCommand = checkCommand;
    }
}
