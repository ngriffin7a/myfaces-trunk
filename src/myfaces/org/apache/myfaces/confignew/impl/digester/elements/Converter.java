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
package net.sourceforge.myfaces.confignew.impl.digester.elements;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class Converter
{

    private String converterId;
    private String forClass;
    private String converterClass;


    public String getConverterId()
    {
        return converterId;
    }


    public void setConverterId(String converterId)
    {
        this.converterId = converterId;
    }


    public String getForClass()
    {
        return forClass;
    }


    public void setForClass(String forClass)
    {
        this.forClass = forClass;
    }


    public String getConverterClass()
    {
        return converterClass;
    }


    public void setConverterClass(String converterClass)
    {
        this.converterClass = converterClass;
    }
}
