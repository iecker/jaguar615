/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;


import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Metamodel;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.internal.SessionImpl;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.persistence.PlcBaseManager;


/**
 * TODO - 6.0 - Refatorar PlcBaseJpaManager
 *
 */
public abstract class PlcBaseJpaManager extends PlcBaseManager{

	@Inject
	protected transient Logger log;

	/**
	 * Fabrica para criação de EntityManager(Sessões)
	 */
	protected EntityManagerFactory emf = null;

	/**
	 * EntityManager(Sessão) ThreadSafe
	 */
	protected ThreadLocal<EntityManager> emThreadLocal = new ThreadLocal<EntityManager>();

	protected boolean inicializaDados = false;

	/**
	 * 
	 * @since 6.1.5
	 * 
	 * Método para ser implementado nas classes que utilizam uma persistenceUnit diferente da default.
	 * 
	 * Necessário para fazer a injeção da persistenceUnit através da anotação:
	 * 
	 * 	@PersistenceUnit(unitName = "default")
	 * 
	 * Funciona nos servidores - jBoss - WebLogic - WebSphere
	 * 
	 * Informar na unitName o nome da fabrica que está criando, definido no persistence.xml
	 * 
	 * @see PlcDefaultJpaManager para ver exemplo
	 * 
	 */
	public EntityManagerFactory getPersistenceUnit() {
		return null;
	}

	public EntityManager getEntityManager(PlcBaseContextVO context)  {

		init(context);

		EntityManager entityManager = emThreadLocal.get();
		if(entityManager==null) {
			entityManager = emf.createEntityManager() ;
			try {
				entityManager.getTransaction().begin();
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_DATABASE, new Object[]{e}, e, log );

			}	
			entityManager.setFlushMode(FlushModeType.COMMIT);
			emThreadLocal.set(entityManager); //Armazena Entity Manager no ThreadLocal para simular "current" session
		}
		inicializaDadosBanco(context, entityManager);
		return entityManager; 
	}	

	protected void init(PlcBaseContextVO context) {

		if (emf == null) {
			emf = getPersistenceUnit();
		}
		
		if (emf==null) {	
			String sufixo = "";
			if (context!=null && context.getExecutionMode()!=null && context.getExecutionMode().equalsIgnoreCase("T")) {
				sufixo="_teste";
			}
			
			try {
				PlcAnnotationUtil annotationUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcAnnotationUtil.class, QPlcDefaultLiteral.INSTANCE);
				emf = Persistence.createEntityManagerFactory(annotationUtil.getDbFactoryName(this.getClass())+sufixo);
			} catch (PlcException e) {
				emf = Persistence.createEntityManagerFactory("default"+sufixo);
			}
		}

	}
	
	private void inicializaDadosBanco(PlcBaseContextVO context, EntityManager em) throws  PlcException {
		if (!inicializaDados && isModoExecucaoTeste(context)) {
			inicializaDados = true;
			InputStream is = this.getClass().getResourceAsStream("/META-INF/export.xml");

			if (is!=null) {
				try {
					IDataSet dataset = new FlatXmlDataSetBuilder().build(is);
					IDatabaseConnection connection = new DatabaseConnection(((SessionImpl)em.getDelegate()).connection());
					DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
					emThreadLocal.get().flush();
					emThreadLocal.get().getTransaction().commit();
					emThreadLocal.get().getTransaction().begin();					
					connection.close();
				} catch (DataSetException e) {
					e.printStackTrace();
				}catch (DatabaseUnitException e) {
					e.printStackTrace();					
				} catch (SQLException e) {
					e.printStackTrace();					
				}
			}
		}		
	}

	/**
	 * Verifica se está executando em modo teste
	 * @return
	 */
	protected boolean isModoExecucaoTeste(PlcBaseContextVO context) {
		return context!=null && "T".equalsIgnoreCase(context.getExecutionMode());		
	}

	/**
	 * @since jCompany 5.0
	 * 
	 * @return a configuração O/R utilizada na construcao da fabrica
	 */
	public Metamodel getMetamodel(PlcBaseContextVO context) {
		if (emf == null)
			init(context);	
		return emf.getMetamodel();
	}	

	/**
	 * @since jCompany 5.0
	 * 
	 * Executa rollback no "current" Entity Manager
	 * 
	 * Executa a operacao de roolback, liberando o recurso no final.
	 */
	public void commit() {
		releaseTransaction(true);
	}

	/**
	 * @since jCompany 5.0
	 * 
	 * Executa commit no "current" Entity Manager
	 * 
	 * Executa a operacao de commit, liberando o recurso no final.
	 */
	public void rollback() {
		releaseTransaction(false);
	}

	private void releaseTransaction(boolean commit) {

		EntityManager em = emThreadLocal.get();
		if (em != null) {
			try {
				EntityTransaction transaction = em.getTransaction();
				if (transaction.isActive()) {
					if (commit) {
						transaction.commit();
					} else {
						transaction.rollback();
					}
				}
			} catch (PlcException plc) {
				throw plc;
			} catch (Exception e) {
				throw new PlcException("Problemas ao executar " + (commit ? "commit" : "rollback"), e);
			} finally {
				release();
			}
		}
	}

	/**
	 * Libera o Entitymanager da thread.
	 */
	protected void release() {
		EntityManager em = emThreadLocal.get();
		try {
			if (em != null && em.isOpen()) {
				em.clear();
				em.close();
			}
		} catch (Exception e) {
			throw new PlcException("Erro ao fechar a conexao.", e);
		} finally {
			emThreadLocal.set(null);
		}
	}

	public Map<String, Object> getProperties() {
		return emf.getProperties();
	}

}	

