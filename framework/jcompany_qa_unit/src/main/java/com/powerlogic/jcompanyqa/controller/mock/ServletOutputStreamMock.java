/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.controller.mock;

//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamMock extends ServletOutputStream
{
    private OutputStream outStream;
    String strStream = null;
    
    /**
     * Default constructor that sends all output to <code>System.out</code>.
     */
    public ServletOutputStreamMock()
    {
        this.outStream = System.out;
    }
    
    /**
     * Constructor that sends all output to given OutputStream.
     * @param out OutputStream to which all output will be sent.
     */
    public ServletOutputStreamMock( OutputStream out )
    {
        this.outStream = out;
    }
    
    public void write( int b )
    {
        try
	    {
		outStream.write( b );
		strStream += b;
	    }
        catch( IOException io )
	    {
		System.err.println("IOException: " + io.getMessage());
		io.printStackTrace();
	    }
    }
    
    @Override
    public void write(byte[] b) throws IOException {
    	outStream.write( b );
    	strStream += b.toString();
    }

	public String getStrStream() {
		return strStream;
	}
}
