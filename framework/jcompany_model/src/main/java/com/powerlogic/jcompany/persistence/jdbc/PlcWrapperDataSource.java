/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;


/**
 * Devido a experiências com o JBoss, que, dependendo de sua versão, utiliza
 * classes que não implementam javax.sql.DataSource, e prevendo que outros
 * application servers possam ter o mesmo comportamento, deve-se utilizar
 * este "wrapper" que fará o devido interfaceamento, garantido total compatibilidade
 * sem a necessidade de escrever trechos específicos para cada situação nova.
 * 
 * <p>Importante: Este DataSource não pode ser singleton porque o jCompany aceita 
 * vários! O padrão ServiceLocator garante que haverá uma única instância para cada pool!</p>
 * 
 * @author Roberto Lúcio Badaró, Alvim
 * @since jCompany 1.0
 */
public class PlcWrapperDataSource implements DataSource {
	
	@Inject
	private transient Logger log;
	
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;
	
    /**
     * Place-holder para o objeto DataSource.
     */
    private Object datasource = null;
    
    /**
	 * Indica que o objeto datasource informado implementa a interface
	 * DataSource, ou seja, segue o padrão, tornando desnecessária as chamadas
	 * aos métodos da interface via reflexão.
	 * 
	 * @since jCompany 3
	 */
    private boolean interfaceOK = false;


    /**
     * Alvim. DataSource tornado
     */
    public PlcWrapperDataSource() {
    }
	
    /**
     * Recebe o objeto datasource que será referenciado nas chamadas aos métodos
	 * padrão da interface javax.sql.DataSource.
     *
     * @param dataSourceObject
     */
    public void setDataSource(Object dataSourceObject) {
    	
		datasource = dataSourceObject;
    	interfaceOK = (datasource instanceof DataSource);
    	
		if (log.isDebugEnabled()) {
			log.debug( "PlcWrapperDataSource recebeu: "
					+ dataSourceObject.getClass().getName()
					+ " - Implementa interface DataSource? " + interfaceOK);
		}
	}


    /**
	 * @see javax.sql.DataSource#getConnection()
	 */
    public Connection getConnection() throws SQLException {
    	if (interfaceOK) {
    		return ((DataSource) datasource).getConnection();
    	} else {
    		return (Connection) executeMethod("getConnection", new Object[0]);
    	}
	}

    /**
	 * @see javax.sql.DataSource#getConnection(java.lang.String,
	 *      java.lang.String)
	 */
    public Connection getConnection(String username, String password)
			throws SQLException {
    	
    	if (interfaceOK) {
			return ((DataSource) datasource).getConnection(username, password);
		} else {
			return (Connection) executeMethod("getConnection", new Object[] {
					username, password });
		}
	}

    /**
	 * @see javax.sql.DataSource#getLogWriter()
	 */
    public PrintWriter getLogWriter() throws SQLException {
		if (interfaceOK) {
			return ((DataSource) datasource).getLogWriter();
		} else {
			return (PrintWriter) executeMethod("getLogWriter", new Object[0]);
		}
	}

    /**
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 */
    public void setLogWriter(PrintWriter out) throws SQLException {
		if (interfaceOK) {
			((DataSource) datasource).setLogWriter(out);
		} else {
			executeMethod("setLogWriter", new Object[] { out });
		}
	}

    /**
	 * @see javax.sql.DataSource#setLoginTimeout(int)
	 */
    public void setLoginTimeout(int seconds) throws SQLException {
		if (interfaceOK) {
			((DataSource) datasource).setLoginTimeout(seconds);
		} else {
			executeMethod("setLoginTimeout",
					new Object[] { new Integer(seconds) });
		}
	}

    /**
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
    public int getLoginTimeout() throws SQLException {
		if (interfaceOK) {
			return ((DataSource) datasource).getLoginTimeout();
		} else {
			Integer i = (Integer) executeMethod("getLoginTimeout",
					new Object[0]);
			return i.intValue();
		}
	}


    /**
	 * Recupera o método requisitado em datasource e executa-o.
	 * 
	 * @param methodName -
	 *            O nome do método a ser recuperado e executado.
	 * @param args -
	 *            Array de Object com os argumentos esperados pelo método.
	 * @return Object - Um Object com o retorno do método, se houver.
	 * 
	 */
    private Object executeMethod(String methodName, Object args[])
			throws SQLException {

		try {
			
			return reflectionUtil.executeMethod(datasource, methodName, args);
			
		} catch (PlcException e) {

			Throwable t = e.getCausaRaiz();
			if (t instanceof SQLException) {
				throw (SQLException) t;
			} else {
				throw new SQLException("Erro tentando executar o método '"
						+ methodName + "': " + t.getMessage());
			}
		}
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	/**
	 * Adicionado no JDK 7 (http://docs.oracle.com/javase/7/docs/api/javax/sql/CommonDataSource.html)
	 * 
	 * @return
	 * @throws SQLFeatureNotSupportedException
	 */
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException{
		return java.util.logging.Logger.getLogger(getClass().getCanonicalName());
	}
}
