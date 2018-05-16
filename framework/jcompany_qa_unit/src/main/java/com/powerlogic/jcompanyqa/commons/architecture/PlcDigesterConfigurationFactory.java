/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.commons.architecture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.seventytwomiles.springframework.io.FileUtils;

import com.seventytwomiles.architecturerules.configuration.xml.DigesterConfigurationFactory;

public class PlcDigesterConfigurationFactory extends
		DigesterConfigurationFactory {
	
	public PlcDigesterConfigurationFactory(final String fileName ) {

		super(fileName);
		
	}
	
	@Override
	protected String getConfigurationAsXml(String configurationFileName) {
		
        File file = new File(configurationFileName);

        if (!file.exists()) {

            /**
             * This code kinda sucks. First, an exception is thrown if the resource
             * does not exist, then an exception could be thrown if the resource
             * could not be read.
             */
            final ClassLoader classLoader = getClass().getClassLoader();

           // final ClassPathResource resource = new ClassPathResource(configurationFileName, classLoader);

            URL resource = classLoader.getResource(configurationFileName);

            if (resource==null)
                throw new IllegalArgumentException("could not load resource "
                        + configurationFileName
                        + " from classpath. File not found.");

            try {
            	
            	String xml = FileUtils.toString((InputStream)resource.getContent(), null);
            	
            	return xml;

            } catch (IOException e) {
            	 throw new IllegalArgumentException("could not locate resource "
                         + configurationFileName
                         + " from classpath. File not found.");
			}
        }


        final String xml;

        try {

            xml = FileUtils.readFileToString(file, null);

        } catch (final IOException e) {

            final String path = file.getAbsolutePath();

            throw new IllegalArgumentException(
                    "could not load configuration from " + path);
        }

        return xml;

	}

}
