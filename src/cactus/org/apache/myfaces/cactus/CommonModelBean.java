/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package net.sourceforge.myfaces.cactus;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/07/01 21:57:59  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/06/28 22:12:11  o_rossmueller
 * fix #978654: do not coerce null
 *
 * Revision 1.2  2004/05/26 17:19:56  o_rossmueller
 * test for bug 948626
 *
 * Revision 1.1  2004/04/23 15:12:14  manolito
 * inputHidden bug reported by Travis
 *
 */
public class CommonModelBean
{
    //private static final Log log = LogFactory.getLog(CommonModelBean.class);

    private long _primitiveLong = 0;
    private boolean primitiveLongSet = false;
    private Integer integer = null;
    private String string;


    public String getString()
    {
        return string;
    }


    public void setString(String string)
    {
        this.string = string;
    }


    public long getPrimitiveLong()
    {
        return _primitiveLong;
    }

    public void setPrimitiveLong(long primitiveLong)
    {
        _primitiveLong = primitiveLong;
        primitiveLongSet = true;
    }


    public boolean isPrimitiveLongSet()
    {
        return primitiveLongSet;
    }


    public Integer getInteger()
    {
        return integer;
    }


    public void setInteger(Integer integer)
    {
        this.integer = integer;
    }
}
