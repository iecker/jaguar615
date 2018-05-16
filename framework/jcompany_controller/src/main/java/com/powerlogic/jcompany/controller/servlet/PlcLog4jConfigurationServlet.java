/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
/*
 * Copyright 1999,2004 The Apache Software Foundation.
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
package com.powerlogic.jcompany.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * A servlet used to dynamically adjust package logging levels while an application is running. NOTE: This servlet is
 * only aware of pre-configured packages and packages that contain objects that have logged at least one message since
 * application startup.
 * <p>
 * web.xml configuration:
 * </p>
 *
 * <pre>
 * &amp;lt;servlet>
 *   &amp;lt;servlet-name>log4j&amp;lt;/servlet-name>
 *   &amp;lt;display-name>Log4j configuration Servlet&amp;lt;/display-name>
 *   &amp;lt;servlet-class>org.apache.log4j.servlet.ConfigurationServlet&amp;lt;/servlet-class>
 * &amp;lt;/servlet>
 * </pre>
 *
 * <p>
 * The <code>fragment</code> parameter can be added if you don't want a full xhtml page in output, but only the
 * content of the body tag, so that it can be used in portlets or struts tiles.
 * </p>
 *
 * <pre>
 * &amp;lt;servlet>
 *   &amp;lt;servlet-name>log4j&amp;lt;/servlet-name>
 *   &amp;lt;display-name>Log4j configuration Servlet&amp;lt;/display-name>
 *   &amp;lt;servlet-class>org.apache.log4j.servlet.ConfigurationServlet&amp;lt;/servlet-class>
 *   &amp;lt;init-param>
 *     &amp;lt;param-name>fragment&amp;lt;/param-name>
 *     &amp;lt;param-value>true&amp;lt;/param-value>
 *   &amp;lt;/init-param>
 * &amp;lt;/servlet>
 * </pre>
 *
 * @author Luther E. Birdzell lebirdzell@yahoo.com
 * @author Yoav Shapira yoavs@apache.org
 * @author Fabrizio Giustina
 * @since jCompany 1.5
 */
public class PlcLog4jConfigurationServlet extends HttpServlet implements SingleThreadModel
{
	private static final String LOG_SPY_PLC = "logSpyPlc";

	private static final String SHOW_PROFILING_TIME_ZERO_PLC = "showZeroTimePlc";

    /**
     * The response content type: text/html
     */
    private static final String CONTENT_TYPE = "text/html";

    /**
     * Should not print html head and body?
     */
    private static final String CONFIG_FRAGMENT = "fragment";

    /**
     * The root appender.
     */
    private static final String ROOT = "Root";

    /**
     * The name of the class / package.
     */
    private static final String PARAM_CLASS = "class";

    /**
     * The logging level.
     */
    private static final String PARAM_LEVEL = "level";

    /**
     * Sort by level?
     */
    private static final String PARAM_SORTBYLEVEL = "sortbylevel";

    /**
     * All the log levels.
     */
    private static final String[] LEVELS = new String[]{
        Level.OFF.toString(),
        Level.FATAL.toString(),
        Level.ERROR.toString(),
        Level.WARN.toString(),
        Level.INFO.toString(),
        Level.DEBUG.toString(),
        Level.ALL.toString()};

    /**
     * Don't include html head.
     */
    private boolean isFragment;

    /**
     * Print the status of all current <code>Logger</code> s and an option to change their respective logging levels.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String sortByLevelParam = request.getParameter(PARAM_SORTBYLEVEL);
        boolean sortByLevel = ("true".equalsIgnoreCase(sortByLevelParam) || "yes".equalsIgnoreCase(sortByLevelParam));

        List loggers = getSortedLoggers(sortByLevel);
        int LoggerNum = 0;

        List<Logger> loggersFiltrados = filtraLogs(loggers,request);
        
        PrintWriter out = response.getWriter();
        if (!isFragment)
        {
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding("UTF-8");

            // print title and header
            printHeader(out, request);
        } 
        StringBuffer b = new StringBuffer();
        // print scripts
        b.append("<a href=\"" + request.getRequestURI() + "\">Atualiza</a>");

        b.append("<table class=\"log4jtable\">");
        b.append("<thead><tr>");

        b.append("<th title=\"Nome da classe de Logger\">");
        b.append("<a href=\"?" + PARAM_SORTBYLEVEL + "=false\">Classe</a>");
        b.append("</th>");

        b.append("<th title=\"O nível do Logger é herdado?\" style=\"text-align:right\" >*</th>");
        b.append("<th title=\"Nível do Logger\">");
        b.append("<a href=\"?" + PARAM_SORTBYLEVEL + "=true\">Nível</a>");
        b.append("</th>");

        b.append("</tr></thead>");
        b.append("<tbody>");

        out.println( b.toString());
        
        // print the root Logger
        displayLogger(out, Logger.getLogger(""), LoggerNum++, request);

        // print the rest of the Loggers
        Iterator iterator = loggersFiltrados.iterator();

        while (iterator.hasNext())
        {
            displayLogger(out, (Logger) iterator.next(), LoggerNum++, request);
        }

        out.println("</tbody>");
        out.println("</table>");
        out.println("Atenção: Esta facilidade não é destinada ao uso em produção.<br>Pode-se variar o número de classes de logging exibidas, " +
        		"modificando-se o final da URL com o parâmetro .../res-plc/log4j?"+LOG_SPY_PLC+"=1 (podendo ainda utilizar-se os níveis 2 e 3)<br>");
        out.println("Existe a opção para não mostrar o profiling de métodos com tempo 0ms. Para apresentar os métodos com tempo 0ms, clique <a href=\"log4j?"+SHOW_PROFILING_TIME_ZERO_PLC+"=S\">aqui</a>, caso contrário <a href=\"log4j?"+SHOW_PROFILING_TIME_ZERO_PLC+"=N\">aqui</a> ");

        if (!isFragment)
        {
            out.println("</body></html>");
            out.flush();
            out.close();
        }
        
    }        	


    /**
     * Filtra os logs por nível
     * @param loggers Lista de todas as classes de log, ordenadas
     * @param request 
     */
    private List<Logger> filtraLogs(List loggers, HttpServletRequest request) {
		
    	// Níveis do espião de logs, de 0 (somente essencial) até 3.
    	int nivelLog=0;
    	if (request.getParameter(LOG_SPY_PLC)!=null) {
    		String nivelLogS = request.getParameter(LOG_SPY_PLC);
    		if (NumberUtils.isNumber(nivelLogS)) {
    			nivelLog = NumberUtils.toInt(nivelLogS);
    			request.getSession().setAttribute(LOG_SPY_PLC,nivelLog);
    		}
    	} else {
    		Object nivelLogO = request.getSession().getAttribute(LOG_SPY_PLC);
    		if (nivelLogO != null) {
    			if (NumberUtils.isNumber(nivelLogO+""))
        			nivelLog = NumberUtils.toInt(nivelLogO+"");
    		}
    	}
    	if (nivelLog==3)
    		return (List<Logger>) loggers;
    	else {
	    	List<Logger> listaFiltrados = new ArrayList<Logger>();
	    	for (Iterator i = loggers.iterator(); i.hasNext();) {
				Logger logger = (Logger) i.next();
				if (nivelLog==0) {
					if (filtroMaximo(logger))
						listaFiltrados.add(logger);
				} else if (nivelLog==1) {
					if (filtroMedio(logger))
						listaFiltrados.add(logger);
				} else if (nivelLog==2) {
					if (filtroMinimo(logger))
						listaFiltrados.add(logger);
	    		} 
			}
	    	return listaFiltrados;
    	}
		
	}

    /**
     * Deixa somente classes básicas:<br>
     *  - iniciadas com "com.powerlogic.jcompany.log"<br>
     *  - classe "log4j.logger.org.hibernate.type"
     */
	private boolean filtroMaximo(Logger logger) {
		return logger.getName().startsWith("com.powerlogic.jcompany.log") || logger.getName().startsWith("com.powerlogic.jcompany_qa.profiling") ||
			logger.getName().equals("org.hibernate.type") ||
			logger.getName().equals("org.hibernate");
	}
	
    /**
     * Deixa adicionalmente às do maximo<br>
     *  - iniciadas com "com.powerlogic.jcompany"<br>
     *  - classe ".controle.", ".facade.", ".modelo.",".persistencia.",".comuns."
     */
	private boolean filtroMedio(Logger logger) {
		return logger.getName().startsWith("com.powerlogic.jcompany.log") || logger.getName().startsWith("com.powerlogic.jcompany_qa.profiling") ||
			logger.getName().equals("org.hibernate.type") ||
			logger.getName().equals("org.hibernate") ||
			logger.getName().indexOf(".controller.")>1 ||
			logger.getName().indexOf(".model.")>1 ||
			logger.getName().indexOf(".persistence.")>1 ||
			logger.getName().indexOf(".facade.")>1 ||
			logger.getName().indexOf(".commons.")>1;
	}

    /**
     * Somente tira eventuais JSPs pre-compiladas pelo Tomcat.
     */
	private boolean filtroMinimo(Logger logger) {
		return !logger.getName().startsWith("org.apache.jsp");
	}
	/**
     * Change a <code>Logger</code>'s level, then call <code>doGet</code> to refresh the page.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String className = request.getParameter(PARAM_CLASS);
        String level = request.getParameter(PARAM_LEVEL);
        
        if (className != null)
        {
            setClass(className, level);
        }

        doGet(request, response);
    }

    /**
     * 
     * Print a Logger and its current level.
     * @param out the output writer.
     * @param logger the Logger to output.
     * @param row the row number in the table this Logger will appear in.
     * @param request the servlet request.
     */
    private void displayLogger(PrintWriter out, Logger logger, int row, HttpServletRequest request)
    {
        String color = null;
        String loggerName = (logger.getName().equals("") ? ROOT : logger.getName());

        color = ((row % 2) == 1) ? "even" : "odd";

        out.println("<tr class=\"" + color + "\">");

        // Logger
        out.println("<td>");
        out.println(loggerName);
        out.println("</td>");

        // level inherited?
        out.println("<td style=\"text-align:right\">");
        if ((logger.getLevel() == null))
        {
            out.println("*");
        }
        out.println("</td>");

        // level and selection
        out.println("<td>");
        out.println("<form action=\"\" method=\"post\">");
        printLevelSelector(out, logger.getLevel().toString());
        out.println("<input type=\"hidden\" name=\"" + PARAM_CLASS + "\" value=\"" + loggerName + "\">");
        out.print("<input type=\"submit\" name=\"Set\" value=\"Set \">");
        out.println("</form>");
        out.println("</td>");

        out.println("</tr>");
    }

    /**
     * Set a Logger's level.
     * @param className class name of the Logger to set.
     * @param level the level to set the Logger to.
     * @return String return message for display.
     */
    private synchronized String setClass(String className, String level)
    {
        Logger logger = null;

        try
        {
            logger = (ROOT.equalsIgnoreCase(className) ? Logger.getLogger("") : Logger.getLogger(className));
            logger.setLevel(Level.toLevel(level));
        }
        catch (Throwable e)
        {
            System.out.println("ERROR Setting LOG4J Logger:" + e);
        }

        return "Message Set For " + (logger.getName().equals("") ? ROOT : logger.getName());
    }

    /**
     * Get a sorted list of all current Loggers.
     * @param sortByLevel if <code>true</code> sort Loggers by level instead of name.
     * @return List the list of sorted Loggers.
     */
    private List getSortedLoggers(boolean sortByLevel)
    {
    	Enumeration enume = LogManager.getCurrentLoggers();
        Comparator comp = new LoggerComparator(sortByLevel);
        List list = new ArrayList();

        // Add all current Loggers to the list
        while (enume.hasMoreElements())
        {
            list.add(enume.nextElement());
        }

        // sort the Loggers
        Collections.sort(list, comp);

        return list;
    }

    /**
     * Prints the page header.
     * @param out The output writer
     * @param request The request
     */
    private void printHeader(PrintWriter out, HttpServletRequest request)
    {
        out.println("<html><head><title>jCompany Log4j Console</title>");
        out.println("<meta http-equiv=\"Content-type\" content=\"text/html; charset=ISO-8859-1\" />");


        out.println("<style type=\"text/css\">");
        out.println("body{ background-color:#fff; }");
        out.println("body, td, th, select, input{ font-family:Verdana, Geneva, Arial, sans-serif; font-size: 8pt;}");
        out.println("select, input{ border: 1px solid #ccc;}");
        out.println("table.log4jtable, table.log4jtable td { border-collapse:collapse; border: 1px solid #ccc; ");
        out.println("white-space: nowrap; text-align: left; }");
        out.println("form { margin:0; padding:0; }");
        out.println("table.log4jtable thead tr th{ background-color: #5991A6; padding: 2px; }");
        out.println("table.log4jtable tr.even { background-color: #eee; }");
        out.println("table.log4jtable tr.odd { background-color: #fff; }");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");
        out.println("<h3>jCompany Log4j Console</h3>");
    }

    /**
     * Prints the Level select HTML.
     * @param out The output writer
     * @param currentLevel the current level for the log (the selected option).
     */
    private void printLevelSelector(PrintWriter out, String currentLevel)
    {
        out.println("<select id=\"" + PARAM_LEVEL + "\" name=\"" + PARAM_LEVEL + "\">");

        for (int j = 0; j < LEVELS.length; j++)  {
            out.print("<option");
            if (LEVELS[j].equals(currentLevel))
            {
                out.print(" selected=\"selected\"");
            }
            out.print(">");
            out.print(LEVELS[j]);
            out.println("</option>");
        }
        out.println("</select>");
    }

    /**
     * Compare the names of two <code>Logger</code>s. Used for sorting.
     */
    private static class LoggerComparator implements Comparator
    {

        /**
         * Sort by level? (default is sort by class name)
         */
        private boolean sortByLevel;

        /**
         * instantiate a new LoggerComparator
         * @param sortByLevel if <code>true</code> sort Loggers by level instead of name.
         */
        public LoggerComparator(boolean sortByLevel)
        {
            this.sortByLevel = sortByLevel;
        }

        /**
         * Compare the names of two <code>Logger</code>s.
         * @param object1 an <code>Object</code> value
         * @param object2 an <code>Object</code> value
         * @return an <code>int</code> value
         */
        public int compare(Object object1, Object object2)
        {
            Logger logger1 = (Logger) object1;
            Logger logger2 = (Logger) object2;

            if (!sortByLevel)
            {
                return logger1.getName().compareTo(logger2.getName());
            }
            return logger1.getEffectiveLevel().toInt() - logger2.getEffectiveLevel().toInt();
        }

        /**
         * Return <code>true</code> if the <code>Object</code> is a <code>LoggerComparator</code> instance.
         * @param object an <code>Object</code> value
         * @return a <code>boolean</code> value
         */
        public boolean equals(Object object)
        {
            if (!(object instanceof LoggerComparator))
            {
                return false;
            }
            return this.sortByLevel == ((LoggerComparator) object).sortByLevel;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return super.hashCode();
        }
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException
    {
        String fragmentParam = config.getInitParameter(CONFIG_FRAGMENT);
        isFragment = ("true".equalsIgnoreCase(fragmentParam) || "yes".equalsIgnoreCase(fragmentParam));
        super.init(config);
    }

    
//    private static AspectDefinition lookupAspectDefinition(final ClassLoader visibleFrom, final String qName) {
//        AspectDefinition aspectDefinition = null;
//
//        Set definitions = SystemDefinitionContainer.getDefinitionsFor(visibleFrom);
//        if (qName.indexOf('/')>0) {
//            // has system uuid ie real qName
//            for (Iterator iterator = definitions.iterator(); iterator.hasNext();) {
//                SystemDefinition systemDefinition = (SystemDefinition) iterator.next();
//                for (Iterator iterator1 = systemDefinition.getAspectDefinitions().iterator(); iterator1.hasNext();) {
//                    AspectDefinition aspectDef = (AspectDefinition) iterator1.next();
//                    if (qName.equals(aspectDef.getQualifiedName())) {
//                        aspectDefinition = aspectDef;
//                        break;
//                    }
//                }
//            }
//        } else {
//            // fallback on class name lookup
//            // must find at most one
//            int found = 0;
//            for (Iterator iterator = definitions.iterator(); iterator.hasNext();) {
//                SystemDefinition systemDefinition = (SystemDefinition) iterator.next();
//                for (Iterator iterator1 = systemDefinition.getAspectDefinitions().iterator(); iterator1.hasNext();) {
//                    AspectDefinition aspectDef = (AspectDefinition) iterator1.next();
//                    if (qName.equals(aspectDef.getClassName())) {
//                        aspectDefinition = aspectDef;
//                        found++;
//                    }
//                }
//            }
//            if (found > 1) {
//                throw new NoAspectBoundException("More than one AspectDefinition found, consider using other API methods", qName);
//            }
//
//        }
//
//        if (aspectDefinition == null) {
//            throw new NoAspectBoundException("Could not find AspectDefinition", qName);
//        }
//
//        return aspectDefinition;
//    }    
}

