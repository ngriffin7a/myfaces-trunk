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
package net.sourceforge.myfaces.bug925693;
import java.util.ArrayList;
import java.util.List;

public class Invoice
{
	private String invoiceNumber;
	private String purchaser;
	private List lineItems = new ArrayList();
	
	public Invoice()
	{
		setInvoiceNumber("34324234");
		setPurchaser("Arthur");
		
		LineItem firstItem = new LineItem();
		firstItem.setProduct("Product 1");
		firstItem.setQuantity("10");
		
		lineItems.add(firstItem);
	}
	
	/**
	 * @return
	 */
	public String getInvoiceNumber()
	{
		return invoiceNumber;
	}

	/**
	 * @return
	 */
	public String getPurchaser()
	{
		return purchaser;
	}

	/**
	 * @param string
	 */
	public void setInvoiceNumber(String string)
	{
		invoiceNumber = string;
	}

	/**
	 * @param string
	 */
	public void setPurchaser(String string)
	{
		purchaser = string;
	}



	public void addLineItem()
	{
		LineItem newItem = new LineItem();
		newItem.setProduct("New!");
		
		lineItems.add(newItem);
	}
	public void removeLineItem()
	{
	    // remove the last item
		lineItems.remove(lineItems.size() - 1);
	}
	/**
	 * @return
	 */
	public List getLineItems()
	{
		return lineItems;
	}

	/**
	 * @param list
	 */
	public void setLineItems(List list)
	{
		lineItems = list;
	}

}
