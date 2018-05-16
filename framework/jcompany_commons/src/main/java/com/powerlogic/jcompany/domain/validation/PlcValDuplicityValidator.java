/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.validation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;

/**
 * Verifica restrições de duplicidade em uma coleção de entidades.
 */
@SuppressWarnings("serial")
public class PlcValDuplicityValidator implements ConstraintValidator<PlcValDuplicity, Collection>, Serializable {
	
	private static final Logger log = Logger.getLogger(PlcValDuplicityValidator.class.getCanonicalName());
	
	private String property;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
		
	public void initialize(PlcValDuplicity parameters) {
		property = parameters.property();
	}

	/**
	 * Verifica Se o total de objetos (beans) na colecao é maior ou igual a minimo e menor ou igual a máximo.
	 */
	public boolean isValid(Collection listaEntidade, ConstraintValidatorContext constraintValidatorContext) {

		int cont = 0;
		
		log.info("Verificando restrições de duplicidade em uma coleção de entidades - PlcValDuplicityValidator - isValid");
		if(listaEntidade == null || listaEntidade != null && listaEntidade.getClass().toString().contains("org.hibernate.collection.internal.PersistentBag")) {
			// Ativação da Validação somente quando a lista não estiver como "proxy" do Hibernate, "PersistentBag"
			// Se ela for proxy, ou o usuário deve mapear como "Eager" ou carregar as listas manualmente, evitando erros de inicialização da coleção.
			// Essa validação é ativada manualmente, e pelo próprio Bean Validator, sendo assim ela não vai deixar de ser realizada.
			// Bug descoberto ao definir um Vinculado como Propriedade Desprezar dos Detalhes.
			
			//"Validação de Duplicidade não realizada. Verificar o método 'isValid()' na classe PlcValDuplicityValidator para mais detalhes. "
			return true;
		}
		
		if(cont == 0) {
			
			for (Iterator iterator = listaEntidade.iterator(); iterator.hasNext();) {
				
				cont++;
				Object entidade = (Object) iterator.next();
				
				try {
					// Somente testa para entidades sem marca de exclusão (linhas válidas)
					if (deleteNotChecked(entidade)) {
						Object valor = propertyUtilsBean.getProperty(entidade,property);
						if (testDuplicity(valor,listaEntidade,cont)) {
							return false;
						}
					}
				} catch (Exception e) {
					log.error(e);
					throw new PlcException("PlcValDuplicityValidator", "isValid", e, null, "Erro ao tentar obter valor de property para teste de duplicidade:"+property);
				}
			}
		}
		
		return true;
	}

	/**
	 * Testa a duplicidade em si
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private boolean testDuplicity(Object valorCorrente, Collection listaEntidade, int aPartirDe) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		if (valorCorrente == null) {
			return false;
		}
		
		int cont=0;
		for (Iterator iterator = listaEntidade.iterator(); iterator.hasNext();) {
			Object entidade = (Object) iterator.next();
			if (cont>=aPartirDe) {
				// Testa duplicidade com valores acima do corrente
				// Caso esteja comparando uma entidade, implementar o método equals(Object obj).
				Object valorIteracao = propertyUtilsBean.getProperty(entidade,property);
				if (valorCorrente.equals(valorIteracao)) {
					return true;
				}
			}
			cont++;
		}
		return false;
	}

	
	/**
	 * Se o bean contem a property padrão indExcPlc com valor "S", então está marcado para excluir, e portanto não será contabilizado.
	 * @param bean Bean a ser investigado
	 * @return true se não está marcado para exclusão
	 */
	private boolean deleteNotChecked (Object bean) {
		try {
			return !("S".equals(propertyUtilsBean.getProperty(bean,"indExcPlc")));
		} catch (Exception e) {
			// se nao tem coluna indExcPlc considera valida
			return true;
		}
	}
		
}
