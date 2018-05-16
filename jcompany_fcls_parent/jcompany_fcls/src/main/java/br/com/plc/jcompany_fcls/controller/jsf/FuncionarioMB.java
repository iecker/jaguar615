/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity;
import br.com.plc.jcompany_fcls.facade.IFuncionarioFacade;
import br.com.plc.jcompany_fcls.model.FuncionarioRepository;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.util.PlcBeanCloneUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity.class,

		components = {@PlcConfigComponent(clazz=br.com.plc.jcompany_fcls.entity.departamento.Endereco.class, property="endereco")},
		
		details = {
//			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.funcionario.HistoricoFuncionalEntity.class,
//								collectionName = "historicoFuncional", numNew = 4,
//								onDemand = false),
			@PlcConfigDetail(clazz = br.com.plc.jcompany_fcls.entity.funcionario.DependenteEntity.class,
								collectionName = "dependente", numNew = 4,
								onDemand = false)


		}
	)

@SPlcMB
@PlcUriIoC("funcionario")
@PlcHandleException
public class FuncionarioMB extends AppMB {

	private static final long serialVersionUID = 1L;
	
	private @Inject Conversation conversation;

	@Inject
	protected IFuncionarioFacade funcionarioFacade;
	
	@Inject
	protected FuncionarioSaveMB funcionarioSaveAction;

 	@Inject @QPlcDefault 
 	protected PlcIocModelUtil iocModeloUtil;
 	
	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;	
		
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("funcionario") 
	public FuncionarioEntity criaEntityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new FuncionarioEntity();
			this.newEntity();
		}
		return (FuncionarioEntity)this.entityPlc;
	}

	
	@Override
	public String save() {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		facesContext.getViewRoot().getViewId();
		
		return funcionarioSaveAction.save(entityPlc);
		
	}
	
	public String verifica() {
		
		funcionarioFacade.verificaFuncionario((FuncionarioEntity)this.entityPlc);
		
		/**
		 * Exemplo de TransaÃ§Ã£o do BO sendo utilizado direto pela Action
		 */
		FuncionarioRepository repo = (FuncionarioRepository)iocModeloUtil.getRepository(this.entityPlc.getClass());
		PlcBaseContextVO context = new PlcBaseContextVO();
		repo.update(context, this.entityPlc);
		
		return null;
	}
	
}
