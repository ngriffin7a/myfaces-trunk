package net.sourceforge.myfaces.bug925693;
import java.util.ArrayList;
import java.util.List;

/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author arthur.fitt
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
