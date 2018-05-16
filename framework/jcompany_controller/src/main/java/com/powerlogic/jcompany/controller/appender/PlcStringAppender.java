package com.powerlogic.jcompany.controller.appender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.WriterAppender;


public class PlcStringAppender extends WriterAppender {

	private ByteArrayOutputStream out;
	
	
	public ByteArrayOutputStream getOut() {
		return out;
	}

	
	
	/**
	 * Constructs an unconfigured appender.
	 */
	public PlcStringAppender() {
		this.name = "JCOMPANY_QA";
		activateOptions();
	}


	/**
	 *   Prepares the appender for use.
	 */
	public void activateOptions() {
		out = new ByteArrayOutputStream();

		setWriter(createWriter(out));

		super.activateOptions();
	}


	protected final void closeWriter() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.closeWriter();
	}


}
