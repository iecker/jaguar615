package com.powerlogic.jcompany.model;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.bindingtype.PlcFileUploadAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcPersistenceAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcPersistenceBefore;

@QPlcDefault
@ApplicationScoped
public class PlcBaseCRUDSRepository {

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 *
	 * Tipicamente, para conter regras que envolvem:<P>
	 *
	 * - Complementação de Validação de Entrada de Dados, quando a validação necessita
	 * de recuperar dados persistentes ou necessita comparar dados
	 * recuperados com informados: nestas duas situações, a lógica de validação declarativa
	 * da Struts e complementos do jCompany não são suficientes. Importante: validações
	 * de duplicidade não precisam ser programadas (restrições "não deve existir" podem ser
	 * declaradas no struts-config.xml)
	 * ou <br>
	 * - Complementação de dados informados segundo regras do negócio: Muitas vezes, é
	 * necessário complementar o Value Object antes que seja gravado, segundo critérios
	 * do negócio.
	 *
	 * @param entidade Referência à Entidade raiz da agregação que será persistida
	 * Importante 1: Se for inclusão, estará nulo.<br>
	 * @param operationType Indica qual operação do CRUDS será chamada (cria, recupera, atualiza, exclui ou pesquisa)
	 * Veja códigos de como testar testar. Exemplos:<p>
	 *     	  "if (operationType.equals(PlcOperationType.CREATE)) {" <br> ou
	 *  	  "if (operationType.equals(PlcOperationType.RETRIEVE)) {" <br> ou
	 *  	  "if (operationType.equals(PlcOperationType.UPDATE)) {" <br> ou	 *
	 *  	  "if (operationType.equals(PlcOperationType.DELETE)) {" <br> ou	 *    
	 *        "if (operationType.equals(PlcOperationType.SEARCH)) {"
	 *  O desenvolvedor deve tratar a exceção, com o maior nível de detalhe
	 * possível, disparando uma exceção jCompany, no mínimo da seguinte forma:<p>
	 *
	 *	  } catch (Exception ex) {
	 *	   log.error( "Erro ao tentar verificar o tipo de curso="+ex);
	 *	   throw new PlcException("aplicacao.erro.meuerro",new Object[] {ex},ex);
	 *    }
	 */
	protected boolean persistenceBefore(PlcBaseContextVO context,Object dao,  Object entidade, PlcOperationType operationType)  {
		context.setDefaultProcessFlow(true);
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcPersistenceBefore>(){});
		return context.isDefaultProcessFlow();
	}	
	
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).<p>
	 *
	 * Tipicamente, estas regras envolvem:<P>
	 *
	 * - Gravação de saldos e consolidados em geral, quando a complementação exige
	 * a chave primária do mestre (não disponível em "antesPersistencia")
	 *
	 * @param entidadeAtual Referência ao Value Object que será objeto de persistência.
	 * Importante 1: Se for inclusão, estará nulo.<br>
	 * @param operationType Indica qual operação do CRUDS será chamada (cria, recupera, atualiza, exclui ou pesquisa)
	 * Veja códigos de como testar testar. Exemplos:<p>
	 *     	  "if (operationType.equals(PlcOperationType.CREATE)) {" <br> ou
	 *  	  "if (operationType.equals(PlcOperationType.RETRIEVE)) {" <br> ou
	 *  	  "if (operationType.equals(PlcOperationType.UPDATE)) {" <br> ou	 *
	 *  	  "if (operationType.equals(PlcOperationType.DELETE)) {" <br> ou	 *    
	 *        "if (operationType.equals(PlcOperationType.SEARCH)) {"
	 *
	 *  O desenvolvedor deve tratar a exceção com o maior nível de detalhe
	 *  possível, disparando uma exceção jCompany, no mínimo da seguinte forma:<p>
	 *
	 *	  } catch (Exception ex) {
	 *	   log.error( "Erro ao tentar verificar o tipo de curso="+ex);
	 *	   throw new PlcException("aplicacao.erro.meuerro",new Object[] {ex},ex);
	 *    }
	 */
	protected void persistenceAfter(PlcBaseContextVO context,Object dao, Object entidade, PlcOperationType operationType)  {
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcPersistenceAfter>(){});
	} 
	
	/**
	 * Ocorre após o arquivo anexado ter sido criado, em padrão que fazem uso desta facilidade. 
	 * 
	 * @param operationType Indica qual operação do CRUDS será chamada (cria, recupera, atualiza, exclui ou pesquisa)
	 * Veja códigos de como testar testar. Exemplos:<p>
	 *     	  "if (operationType.equals(PlcOperationType.CREATE)) {" <br> ou
	 *  	  "if (operationType.equals(PlcOperationType.RETRIEVE)) {" <br> ou
	 *  	  "if (operationType.equals(PlcOperationType.UPDATE)) {" <br> ou	 *
	 *  	  "if (operationType.equals(PlcOperationType.DELETE)) {" <br> ou	 *    
	 *        "if (operationType.equals(PlcOperationType.SEARCH)) {"
	 */
	protected void fileUploadAfter(PlcBaseContextVO context,Object dao, Object entidade, PlcOperationType operationType)  {
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcFileUploadAfter>(){});
	}
}
