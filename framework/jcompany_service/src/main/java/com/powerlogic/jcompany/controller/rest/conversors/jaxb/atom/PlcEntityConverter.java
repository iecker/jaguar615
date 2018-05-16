/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.ManyToOne;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@QPlcDefault
public class PlcEntityConverter implements Converter {

	@Inject
	private transient Logger log;

	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		boolean canConvert = false;
		for (Annotation anotacaoClasse : type.getAnnotations()) {
			if (javax.persistence.Entity.class.isAssignableFrom(anotacaoClasse.getClass()) || javax.persistence.MappedSuperclass.class.isAssignableFrom(anotacaoClasse.getClass()) || javax.persistence.Embeddable.class.isAssignableFrom(anotacaoClasse.getClass())) {
				canConvert = true;
				break;
			}
		}
		return canConvert;
	}

	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		
		for (PropertyDescriptor atributo : PropertyUtils.getPropertyDescriptors(source.getClass())) {

			if (atributo.getReadMethod()!=null&&atributo.getWriteMethod()!=null) {
				Object valor = null;
				try {
					valor = PropertyUtils.getProperty(source, atributo.getName());
				} catch (Exception e) {
					log.warn( "Erro reflexao: na propriedade "+atributo.getName()+". Provavelmente, ela não existe na entidade, ou a entidade não está inicializada.");
				}
				finally {
					if (valor != null) {
						try{
							touch( valor );
						} catch( Exception x ){
							continue;
						}
						writer.startNode( atributo.getName() );
						// Quando atributo de classe é do tipo java.util.Date, é
						// feita conversão para o padrão esperado pelo xstream
						if (!atributo.getPropertyType().equals(java.util.Date.class)) {
							context.convertAnother( valor );
						} else {
							writer.setValue( PlcConvertRFCUtil.encode3339( ( Date ) valor) );
						}
						writer.endNode();
					}
				}
			}
		}
	}

	/**
	 * Toca a entidade para verificar se ela foi inicializada ou não.
	 * O toque é realizado a partir do toString da entidade. Então, se quando o toString for chamado e, a entidade não
	 * estiver sido inicializada, e o mesmo retornar uma excecao indicará que a entidade não foi inicializada.
	 * @param valor
	 */
	private void touch(Object valor) {
		valor.toString();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Object obj = instanciaNovaInstancia(reader, context);

		if (obj != null) {

			Map<String, Field> mapaAtributos = getAttributesMap(obj);

			while (reader.hasMoreChildren()) {
				reader.moveDown();

				String nodeName = reader.getNodeName();

				if (mapaAtributos.containsKey(nodeName)) {
					Field atributo = mapaAtributos.get(nodeName);
					Class<?> typeClazz = atributo.getType();
					Object valor = null;
					// Quando atributo de classe é do tipo java.util.Date, é
					// feita conversão para o padrão esperado pelo xstream
					if (typeClazz.equals(java.util.Date.class)) {
						valor = PlcConvertRFCUtil.decode3339(reader.getValue());
					} else if (Modifier.isAbstract(typeClazz.getModifiers()) && !typeClazz.isPrimitive()) {
						ManyToOne annotationManyToOne = atributo.getAnnotation(ManyToOne.class);
						if (annotationManyToOne != null) {
							valor = context.convertAnother(obj, annotationManyToOne.targetEntity());
						}
					} else {
						valor = context.convertAnother(obj, typeClazz);
					}
					try {
						PropertyUtils.setProperty(obj, nodeName, valor);
					} catch (Exception e) {
						log.warn( "Erro reflexao:", e);
					}
				}
				reader.moveUp();
			}

		}

		return obj;
	}

	private Map<String, Field> getAttributesMap(Object obj) {

		Map<String, Field> mapa = new HashMap<String, Field>();

		for (Field atributo : getreflectionUtil().findAllFieldsHierarchically(obj.getClass(), false)) {
			mapa.put(atributo.getName(), atributo);
		}

		return mapa;
	}

	private Object instanciaNovaInstancia(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Object objetoAtual = context.currentObject();
		if (objetoAtual == null) {
			Class<?> classe = context.getRequiredType();
			if (classe != null) {
				try {
					objetoAtual = classe.newInstance();
				} catch (Exception e) {
					log.warn( "Erro ao criar intancia de " + classe.getName(), e);
				}
			}
		}
		return objetoAtual;
	}

	// TEMPLATE METHODS

	protected boolean deveIncluirAtributo(Object source, Field atributo, Annotation[] anotacoesAtributo) {
		boolean deveIncluir = false;
		for (Annotation anotacao : anotacoesAtributo) {
			// Inclui os atributos com anotacoes @Column
			if (javax.persistence.Column.class.isAssignableFrom(anotacao.getClass()) || javax.persistence.OneToMany.class.isAssignableFrom(anotacao.getClass()) || javax.persistence.ManyToOne.class.isAssignableFrom(anotacao.getClass()) || javax.persistence.Embedded.class.isAssignableFrom(anotacao.getClass())) {
				deveIncluir = true;
				break;
			}
		}
		return deveIncluir;
	}

	protected PlcReflectionUtil getreflectionUtil() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
	}
}
