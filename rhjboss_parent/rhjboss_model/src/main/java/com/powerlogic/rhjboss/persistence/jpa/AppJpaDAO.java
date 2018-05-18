/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.powerlogic.rhjboss.persistence.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObjectEjb;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;

@SPlcDataAccessObjectEjb
@Stateless
@QPlcDefault
public class AppJpaDAO extends PlcBaseJpaDAO {

	@PersistenceContext(unitName="default")
	private EntityManager emDefault;
	
	public EntityManager getEmDefault() {
		return emDefault;
	}
	
}
