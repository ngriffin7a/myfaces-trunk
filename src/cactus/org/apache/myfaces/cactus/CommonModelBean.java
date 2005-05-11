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
package org.apache.myfaces.cactus;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
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
