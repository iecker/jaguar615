/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information in your installation directory. 
*  Contact Powerlogic for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br  */ 
package ###NOME_PACOTE###.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import com.powerlogic.jcompany.domain.PlcBaseMapEntity;
/**
 * @since jCompany 3.2 Ancestral para todas as classes da aplicação, 
 * com pre-mapeamentos para OID, versao e auditoria minima herdados.
 */
@MappedSuperclass
public abstract class AppBaseEntity extends PlcBaseMapEntity implements Serializable {
	private static final long serialVersionUID = 1307334639090980365L;
}