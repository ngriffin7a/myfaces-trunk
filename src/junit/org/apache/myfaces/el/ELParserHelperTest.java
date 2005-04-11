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

import junit.framework.TestCase;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ELParserHelperTest extends TestCase
{
    //~ Constructors -------------------------------------------------------------------------------

    public ELParserHelperTest(String name)
    {
        super(name);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void testToJspElExpression() throws Exception
    {
        assertEquals("${}", ELParserHelper.toJspElExpression("#{}"));
		assertEquals("{}", ELParserHelper.toJspElExpression("{}"));
		assertEquals("{aa} {bb}", ELParserHelper.toJspElExpression("{aa} {bb}"));
		assertEquals("{aa}{bb}", ELParserHelper.toJspElExpression("{aa}{bb}"));
		assertEquals("1${'<'}{0,number,integer}", ELParserHelper.toJspElExpression("1#{'<'}{0,number,integer}"));
		
		assertEquals(
												 "{0,choice,0#${scrollerNoItem_}|1#${scrollerOneItem_}|1${'<'}{0,number,integer} ${scrollerItems_}} ${dataScrollerMsg.found_displaying} {1} to {2}. ${dataScrollerMsg.Page} {3} / {4}",
				ELParserHelper.toJspElExpression("{0,choice,0##{scrollerNoItem_}|1##{scrollerOneItem_}|1#{'<'}{0,number,integer} #{scrollerItems_}} #{dataScrollerMsg.found_displaying} {1} to {2}. #{dataScrollerMsg.Page} {3} / {4}"));
    }
    
}