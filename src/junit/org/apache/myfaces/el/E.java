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
package org.apache.myfaces.el;

import java.util.HashMap;

/**
 * @author <a href="mailto:matzew@apache.org">Matthias Weﬂendorf</a> 
 */
public class E {
    
    private String a = "foo";
    private String b = "bar";
    private HashMap c = null;
    private Integer d = null;
    
    /**
     * @return Returns the d.
     */
    public Integer getD() {
        return d;
    }
    /**
     * @param d The d to set.
     */
    public void setD(Integer d) {
        this.d = d;
    }
    public E(){
        c = new HashMap();
        c.put("foo","bar");
        
    }
    
    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
    public HashMap getC() {
        return c;
    }
   public void setC(HashMap c) {
        this.c = c;
    }
}
