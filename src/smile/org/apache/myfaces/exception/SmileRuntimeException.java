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
package net.sourceforge.myfaces.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * @author Dimitry D'hondt
 *
 * Base class for all runtime exceptions in smile.
 * SmileExceptions also support constructors with the causing exception.
 */
public class SmileRuntimeException extends RuntimeException {
	private static Log log = LogFactory.getLog(SmileException.class);
	private String msg = null;
	private Throwable cause = null;

	public SmileRuntimeException() {
		super();
		log.error("Anonymous exception of type <" + this.getClass().getModifiers() + "> was thrown..");
	}

	public SmileRuntimeException(String msg) {
		super();
		this.msg = msg;
		log.error("<" + this.getClass().getModifiers() + "> thrown with message : " + msg);		
	}
	
	public SmileRuntimeException(Throwable cause) {
		super();
		this.cause = cause;
		log.info("Anonymous exception of type <" + this.getClass().getModifiers() + "> was thrown..",cause);
	}
	
	public SmileRuntimeException(String msg, Throwable cause) {
		super();
		this.msg = msg;
		this.cause = cause;
		log.error("<" + this.getClass().getModifiers() + "> thrown with message : " + msg,cause);		
	}
	
	/**
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace() {
		super.printStackTrace();
		if(cause != null) {
			System.out.println("\n\nCaused by:\n");
			cause.printStackTrace();
		}
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		if(cause != null) {
			s.println("\n\nCaused by:\n");
			cause.printStackTrace(s);
		}
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		if(cause != null) {
			s.println("\n\nCaused by:\n");
			cause.printStackTrace(s);
		}
	}
}