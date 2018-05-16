/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.velocity;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeInstance;


public class PlcParseVelocity {

	public static void parse(String templateFile, String outputFile, Map<String, Object> mapa) {
        try
        {

            VelocityContext context = new VelocityContext();

            for (Iterator iterator = mapa.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				context.put(key, mapa.get(key));
			}

            Template template =  null;

            try
            {
            	Properties p = new Properties();
            	p.put(RuntimeInstance.INPUT_ENCODING, "UTF-8");
            	p.put(RuntimeInstance.OUTPUT_ENCODING, "UTF-8");
            	p.put("file.resource.loader.path", "");
            	Velocity.init(p);
            	
                template = Velocity.getTemplate(templateFile);

                BufferedWriter writer = writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(templateFile+".merge")));

                if ( template != null) {
                    template.merge(context, writer);
                }

                /*
                 *  flush and cleanup
                 */

                writer.flush();
                writer.close();
                
                File merge = new File(templateFile+".merge");
                FileUtils.copyFile(merge, new File(outputFile));
                merge.delete();
                
            
            }
            catch( ResourceNotFoundException rnfe )
            {
                System.out.println("Error : cannot find template " + templateFile + ":" + rnfe );
            	rnfe.printStackTrace();
            }
            catch( ParseErrorException pee )
            {
                System.out.println("Error : Syntax error in template " + templateFile + ":" + pee );
            	pee.printStackTrace();
            }

        }
        catch( Exception e )
        {
            System.out.println(e);
        	e.printStackTrace();
        }

	}

}
