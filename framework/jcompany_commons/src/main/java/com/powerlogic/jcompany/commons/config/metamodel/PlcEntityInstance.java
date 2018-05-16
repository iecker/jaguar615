/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.config.metamodel;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

/**
 * jCompany 2.5.3. Value Object ancestral para todos os VOs de aplicaçõeses com
 * persistência, inclusive VOs de Chave Natural. Traz serviços de equals,
 * toString e hashCode genéricos, bem como manipuladores de identificação genéricos.
 */
public class PlcEntityInstance<T> implements Serializable, Cloneable {
	
	private Logger log = Logger.getLogger(PlcEntityInstance.class.getCanonicalName());
	
	/* A instancia da entidade correspondente */
	private T instancia;

	/* O metadado PlcEntity correspondente */
	private PlcEntity<T> plcEntity;
	
    private static final String ERRO_ORIGINAL = ". Erro original: ";

	private static final long serialVersionUID = -6328544514859752259L;

	protected static String[] PROPS_CHAVE_NATURAL_PLC = new String[]{"id"};
	
	protected PlcMetamodelUtil metamodelUtil;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	public PlcEntityInstance(PlcEntity<T> plcEntity, T instancia) {
		this.plcEntity = plcEntity;
		this.instancia = instancia;
		this.metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
	}
	
	public PlcEntity<T> getPlcEntity() {
		return plcEntity;
	}

	/**
     * Devolve a declaração estática de propriedades chave. Deve ser
     * especializado para permitir polimorfismo sobre declaração estática e montagem
     * de hiperlinks genéricos para recuperação
     *
     * @return String estático com relação de propriedades chave.
     * @since jCompany 3.0
	 */	
    public String[] getPropsChaveNaturalPlc() {
    	PlcPrimaryKey chavePrimaria = plcEntity.getEntityClass().getAnnotation(PlcPrimaryKey.class);
    	if (chavePrimaria==null || metamodelUtil.isEntityClass(chavePrimaria.classe())) {
    		return PROPS_CHAVE_NATURAL_PLC;
    	}
    	return chavePrimaria.propriedades();
    }  

    /** Guarda hashCode **/
    protected int hashCodePlc = 0;

    /**
     * Mapeamento padrão para OID, para todas as classes
     * @since jCompany 3.0
     */
    public java.lang.Long getId() {
    	Long id = null;
    	
    	try {
			id = (Long) propertyUtilsBean.getProperty(instancia, plcEntity.getIdAtributoNome());
		} catch (Exception e) {
			log.debug( "Erro!!!");
		}
    	
        return id;
    }

    public void setId(java.lang.Long novoId) {
    	try {
			propertyUtilsBean.setProperty(instancia, plcEntity.getIdAtributoNome(), novoId);
		} catch (Exception e) {
			log.debug( "Erro!!!", e);
		}
    }

    /**
     * OID auxiliar em forma string para entradas de dados tabulares, que não
     * possam usar o form-bean da struts
     * @since jCompany 3.0
     */
    public String getIdAux() {
        if (getId() != null) {
            return (getId().toString());
        } else if(getId() == null && !"".equals(getRowId())) {
            return getRowId();
        } else { 
        	return "";
        }
    }

    /**
     * @since jCompany 3.0
     */
    public void setIdAux(String novoIdAux) {
    	if (StringUtils.isNumeric(novoIdAux)){
	        if (novoIdAux != null && !novoIdAux.equals("")){
	        	setId(new Long(novoIdAux));
	        } else { 
	        	setId(null);
	        }
    	}else{
    		if (novoIdAux != null && !novoIdAux.equals("")){
	        	setRowId(novoIdAux);
	        } else { 
	        	setRowId(null);
	        }
    	}
        	
    }

	public String getRowId() {
    	String rowId = "";
    	
    	try {
    		rowId = (String) propertyUtilsBean.getProperty(instancia, plcEntity.getRowIdAtributoNome());
		} catch (Exception e) {
			log.debug( e.getMessage());
		}
    	
        return rowId;
	}

	public void setRowId(String rowId) {
    	try {
			propertyUtilsBean.setProperty(instancia, plcEntity.getRowIdAtributoNome(), rowId);
		} catch (Exception e) {
			log.debug( e.getMessage());
		}
	}

    /**
     * Devolve o nome da propriedade padrão para este VO. O padrão é seu nome final com inicial minúscula e
     * sem o VO (nome abstrato). Pode ser sobreposto nos descendentes, se desejado
     * @since jCompany 3.0
     */
    public  String getNomePropriedadePlc() {

    	String sufixoPadraoEntidade = "VO";

    	try{

    		sufixoPadraoEntidade = getConfigUtil().getConfigApplication().suffixClass().entity();
    		
    	}catch(Exception e){
    		log.error( e.getMessage(),e);
    	}


    	String auxProp = instancia.getClass().getName().substring(instancia.getClass().getName().lastIndexOf(".")+1);
    	if (instancia.getClass().getName().endsWith(sufixoPadraoEntidade) && !sufixoPadraoEntidade.isEmpty())
    		return auxProp.substring(0,1).toLowerCase()+auxProp.substring(1,auxProp.indexOf(sufixoPadraoEntidade));
    	else
    		return auxProp.substring(0,1).toLowerCase()+auxProp.substring(1);
    }
  
    /**
     * Chama dinamicamente porque XDoclet exige o método getIdNatural no
     * descenente
     * @since jCompany 3.0
     */
    public Object getIdNaturalDinamico() {
        Object idNatural = null;
        if (propertyUtilsBean.isReadable(instancia, "idNatural")) {
            try {
                idNatural = propertyUtilsBean.getSimpleProperty(instancia, "idNatural");
            } catch (Exception e) {
            	log.error( "Erro ao tentar chamar metodo getIdNatural em "
                                + instancia.getClass().getName());
            }
        }
        return idNatural;
    }

    /**
     * @since jCompany 3.0 
     */
    public void setIdNatural(Object idNatural) {
    	try {
			propertyUtilsBean.setProperty(instancia, plcEntity.getIdAtributoNome(), idNatural);
		} catch (Exception e) {
			log.debug( "Erro!!!", e);
		}
    }

    /**
     * Auxiliar que indica para cada objeto que este deverá ser excluido.
     * Utilizado em padrões Tabular ou Detalhes e preenchido em função do
     * checkbox de inclusão
     * @since jCompany 3.0
     */
    public String getIndExcPlc() {
    	String indExcPlc = null;
    	
    	try {
    		indExcPlc = (String) propertyUtilsBean.getProperty(instancia, plcEntity.getIndExcPlcAtributoNome());
		} catch (Exception e) {
			log.debug( "Erro!!!", e);
		}
    	
        return indExcPlc;

    }


    /**
     * @since jCompany 3.0 
     */
    public void setIndExcPlc(String novoindExcPlc) {
    	try {
			propertyUtilsBean.setProperty(instancia, plcEntity.getIndExcPlcAtributoNome(), novoindExcPlc);
		} catch (Exception e) {
			log.debug( "Erro!!!", e);
		}    
	}



    /**
     * Override no método de java.lang.Object utilizando propriedades não nulas na exibição. 
     * Pode-se sobrepor este método nos descendentes para especializar a visualização.
     *
     * @return todas as propriedades não nulas do VO
     * 
     * @since jCompany 2.5.1
     */
	public String toStringPlc(Object[] props) {

        StringBuffer sb = new StringBuffer("[");
        int cont = 0;

        if (props == null) {
            // Passou null
            PropertyDescriptor[] pds = propertyUtilsBean.getPropertyDescriptors(instancia.getClass());
            if (pds != null) {
                for (int i = 0; i < pds.length; i++) {

                    PropertyDescriptor pd = (PropertyDescriptor) pds[i];
                    if (propertyUtilsBean.isReadable(instancia, pd.getName())) {
                        try {
                            Object valor = propertyUtilsBean.getSimpleProperty(instancia, pd.getName());
                            if (valor != null && !metamodelUtil.isEntityClass(valor.getClass())
                                    && !(valor instanceof java.util.List<?>)
                                    && !(valor instanceof java.util.ArrayList<?>)
                                    && !(valor instanceof java.util.Set<?>)
                                    && !(valor instanceof java.util.Map<?,?>)) {
                                cont++;
                                if (cont > 1) {
                                    sb.append(",");
                                }
                                sb.append(pd.getName() + "=" + valor);
                            }
                        } catch (Exception e) {
                            sb.append(pd.getName() + "=erro:" + e.toString());
                        }
                    }
                }
            }

        } else {
            // Forneceu lista de propriedades
            for (int i = 0; i < props.length; i++) {
                String prop = (String) props[i];
                if (propertyUtilsBean.isReadable(instancia, prop)) {
                    try {
                        Object valor = propertyUtilsBean.getSimpleProperty(instancia, prop);
                        if (valor != null) {
                            cont++;
                            if (cont > 1)
                                sb.append(",");
                            sb.append(prop + "=" + valor);
                        }
                    } catch (Exception e) {
                        sb.append(prop + "=erro:" + e.toString());
                    }
                }
            }

        }

        sb.append("]");
        
        if (sb.toString().equals("[]"))
        	return "";
        else
        	return sb.toString();
    }
    
    /**
     * Utilizada para montagem de hashCode recursiva, situação onde somente é montada a chave
     * @since jCompany 3.1
     */
    public int hashCodeChavePlc() {
    	String[] props = getPropsChaveNaturalPlc();
    	int result = 17;
    	if (props != null) {
    		
    		// Nao informou relacao de propriedades
    		for (int i = 0; i < props.length; i++) {
    			
    			try {
    				// Se tiver getter e nao for colecao
    				if (propertyUtilsBean.isReadable(instancia,props[i]) && 
    						(!Collection.class.isAssignableFrom(propertyUtilsBean.getPropertyType(instancia,props[i])))) {
    					Object valor = propertyUtilsBean.getNestedProperty( instancia, props[i]);
    					int valorAux = valor == null ? 0 : valor.hashCode();
    					result = result * 37 + valorAux;
    				}
    			} catch (Exception e) {
    				log.error( "Erro ao tentar pegar hashCode de propriedade " + props[i] + " Erro:" + e);
    				e.printStackTrace();
    			}
    		}
    		
    	}  	
    	
    	return result;
    }
    
    /**
     * Implementação de hashCode em conformidade com o padrão Bloch.
     * Importante: O resultado do "hashCode" deve ser compatível com o resultado do "equals". Assim,
     * como o "equals" do jCompany percorre todas as propriedades, o mesmo deve se dar com o hashCode
     * @since jcompany 2.5.3
     */
	public int hashCodePlc() {
    	
    	int hashCodePlc = 0;
    	
    	Field hashCodePlcAtributo = plcEntity.getHashCodePlcAtributo();
    	
    	if (hashCodePlcAtributo != null) {
			try {
				hashCodePlcAtributo.setAccessible(true);
				hashCodePlc = hashCodePlcAtributo.getInt(instancia);
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug( "Erro ao acessar o atributo hashCodePlc da entidade", e);
				}
				hashCodePlcAtributo = null;
			}
    	}
    	
    	if (hashCodePlc==0) {
    		
    		int result = 17;
    		Object valor = null;
    		
    		try {
    			Set<String> propsTodas = plcEntity.getNomesAtributos();
    			for (String prop : propsTodas) {
    				valor = propertyUtilsBean.getProperty(this, prop);
    				//System.out.println("Vai tentar pegar hashCode de "+valor);
    				int valorAux = 0;
    				if (valor!=null) {
    					if (valor.getClass().isArray()) {
    						Class<?> componentType = valor.getClass().getComponentType();
    						if (componentType.isPrimitive()) {
    							if (componentType.equals(boolean.class)) {
    								valorAux =  Arrays.hashCode((boolean[])valor);
    							} else if (componentType.equals(byte.class)) {
    								valorAux =  Arrays.hashCode((byte[])valor);
    							} else if (componentType.equals(char.class)) {
    								valorAux =  Arrays.hashCode((char[])valor);
    							} else if (componentType.equals(double.class)) {
    								valorAux =  Arrays.hashCode((double[])valor);
    							} else if (componentType.equals(float.class)) {
    								valorAux =  Arrays.hashCode((float[])valor);
    							} else if (componentType.equals(int.class)) {
    								valorAux =  Arrays.hashCode((int[])valor);
    							} else if (componentType.equals(long.class)) {
    								valorAux =  Arrays.hashCode((long[])valor);
    							} else if (componentType.equals(short.class)) {
    								valorAux =  Arrays.hashCode((short[])valor);
    							}
    						} else {
    							valorAux = Arrays.hashCode((Object[]) valor);
    						}
    					} else if (valor !=null && metamodelUtil.isEntityClass(valor.getClass())) {
    						PlcEntityInstance<?> valorInstance = metamodelUtil.createEntityInstance(valor);
    						valorAux = valorInstance.hashCodeChavePlc();
    					} else if (valor instanceof Collection<?>) {
    						//valorAux = ((Collection)valor).size();
    					} else {
    						valorAux = valor.hashCode();
    					}
    				}
    				result = result * 37 + valorAux;
    			}
    		} catch (Exception e) {
    			log.error( "Erro ao tentar pegar hashCode de propriedade "+ valor + " Erro:" + e);
    			e.printStackTrace();
    		}
    		
    		hashCodePlc = result;
    		
    		if (hashCodePlcAtributo != null) {
    			try {
    				hashCodePlcAtributo.setInt(instancia, hashCodePlc);
    			} catch (Exception e) {
					//throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "hashCodePlc - " + e }, e, log);
    				throw new PlcException("PlcEntityInstance", "hashCodePlc", e, log, "");
				}	
    		}
    	}
    	
    	return hashCodePlc;
    }

    /**
     * @since jCompany 3.0
     * Código padronizado para equals, baseado em valores declarados como chave de negócio do VO. 
     */
	public  boolean equalsChaveNatural(Object outro) {
    	if (outro != null && outro instanceof PlcEntityInstance<?>) {
    		PlcEntityInstance<?> outroInstance = (PlcEntityInstance<?>) outro;
    		PlcPrimaryKey chavePrimaria 	= (PlcPrimaryKey) plcEntity.getEntityClass().getAnnotation(PlcPrimaryKey.class);
    		
    		PlcEntityCommonsUtil entityCommonsUtil = (PlcEntityCommonsUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcEntityCommonsUtil.class , QPlcDefaultLiteral.INSTANCE);
    		
    		if (chavePrimaria != null){
    			Object thisIdNatural 	= this.getIdNaturalDinamico();
    			Object outroIdNatural 	= outroInstance.getIdNaturalDinamico();
    	        return entityCommonsUtil.equalsPlc(thisIdNatural, outroIdNatural, getPropsChaveNaturalPlc());
    		}
    		else {
    			return entityCommonsUtil.equalsPlc(instancia, outroInstance.getInstancia(), getPropsChaveNaturalPlc());
    		}
    	}
		return false;
    }

    /**
     * @since jCompany 3.0
     * Código padronizado para equals, baseado em todos os valores do VO. 
     * Recomenda-se especializar fazendo teste específico ou passando propriedades suficientes
     * como array de String
     */
	public boolean equals(Object outro) {
    	
    	PlcEntityCommonsUtil entityCommonsUtil = (PlcEntityCommonsUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcEntityCommonsUtil.class , QPlcDefaultLiteral.INSTANCE);
    	
        boolean igual = outro != null && outro.getClass().equals(this.getClass()) && metamodelUtil.isEntityClass(((PlcEntityInstance<?>)outro).instancia.getClass()) && entityCommonsUtil.equalsPlc(instancia, ((PlcEntityInstance<?>)outro).instancia);
         
        return igual;
    }

    /**
     * @since jCompany 3.0.
     * Código padronizado para hashCode
     */
    public int hashCode() {
        return hashCodePlc();
    }

    /**
     * @since jCompany 2.5.3.
     * Código genérico para toString
     */
    public String toString() {
        return toStringPlc(getPropsChaveNaturalPlc());
    }

    /**
     * @since jCompany 2.5.3.
     * Link de edição genérico
     */
    public String getLinkEdicaoPlc() {

        StringBuilder link = new StringBuilder();

        if (getId() != null ) {

            link.append("&id=").append(getId());

        } else if (getIdAux()!=null && !getIdAux().trim().equals("")) {
        	
            try {
				link.append("&id=").append(URLEncoder.encode(getIdAux(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				link.append("&id=").append(getIdAux());
				log.error( "jCompany. Erro fatal ao tentar montar link de edicao do VO "
                        + instancia
                        + " para propriedade "
                        + " idAux"
                        + ERRO_ORIGINAL
                        + e.toString());
                e.printStackTrace();
			}

        } else {
    		Object idNatural = this.getIdNaturalDinamico();

    		if (idNatural != null ){

        	
        	for(String propriedade : getPropsChaveNaturalPlc()) {
        		
				try {
                	
                	if(propertyUtilsBean.getPropertyDescriptor(idNatural, propriedade) != null){
                		
                		Class<?> propertyType = propertyUtilsBean.getPropertyDescriptor(idNatural, propriedade).getPropertyType();
    					if (propertyType!=null && propertyType.equals(java.util.Date.class)){
    						Timestamp valorField = (Timestamp)propertyUtilsBean.getProperty(idNatural, propriedade);
    						if (valorField != null ){
    							link.append("&").append(propriedade).append("=");
    							link.append(valorField.getTime());
    						}
    					} else if (propertyType!=null && metamodelUtil.isEntityClass(propertyType)) {
                        	Object b 					= propertyUtilsBean.getProperty(idNatural, propriedade);
                        	if (b != null ){
                        		PlcEntityInstance<?> bInstance = metamodelUtil.createEntityInstance(b);
                        		PlcPrimaryKey chaveNatural 	= (PlcPrimaryKey)b.getClass().getAnnotation(PlcPrimaryKey.class);
                        		if (chaveNatural != null){
                        			Object value  = propertyUtilsBean.getProperty(bInstance.getIdNaturalDinamico(), (chaveNatural.propriedades()[0]));
                        			link.append("&").append(propriedade).append("=");
                        			link.append(value != null ? value.toString() : "");
                        		}
                        		else{
                        			link.append("&").append(propriedade).append("=");
                        			link.append(bInstance.getIdAux());
                        		}
                        	}
                        } else {
                        	if (propertyUtilsBean.getProperty(idNatural, propriedade)!=null){
                        		link.append("&").append(propriedade).append("=");
                        		link.append(URLEncoder.encode(propertyUtilsBean.getProperty(idNatural, propriedade).toString(), "UTF-8"));
                        	}
                        }
                	}
                	
                } catch (Exception e) {
                    // Os VOs não utilizam log4j por padrão, por isso este envio diretamente na console.
                	log.error( "jCompany. Erro fatal ao tentar montar link de edicao do VO " + instancia + " para propriedade " + propriedade + ERRO_ORIGINAL + e.toString());
                    e.printStackTrace();
                }
            }
        	if(!StringUtils.isEmpty(link.toString())){
        		link.append("&evento=y");
        	}
        }
    	}

        return link.toString();

    }
    
    /**
     * @since jCompany 2.5.3.
     * Link de edição que inclui todos os argumentos informados com a anotacao PlcNavegacao.
     */
	public String getLinkEdicaoAvancadoPlc() {
    	
    	StringBuffer link = new StringBuffer("");
    	
    	if ( instancia.getClass().getAnnotation(PlcEntityTreeView.class) != null){
    		String props = ((PlcEntityTreeView)instancia.getClass().getAnnotation(PlcEntityTreeView.class)).navegacao();
    		StringTokenizer st = new StringTokenizer(props,",");
    		while (st.hasMoreElements()) {
    			
    			String prop = (String)st.nextElement();
    			
    			try {
    				if (propertyUtilsBean.getPropertyDescriptor(instancia, prop).getPropertyType().equals(java.util.Date.class))
    					link.append("&" + prop + "=" + propertyUtilsBean.getProperty(instancia, prop+ "Aux"));
    				else if (metamodelUtil.isEntityClass( propertyUtilsBean.getPropertyDescriptor(instancia, prop).getPropertyType())) {
    					Object b = propertyUtilsBean.getProperty(instancia, prop);
    					PlcEntityInstance<?> bInstance = metamodelUtil.createEntityInstance(b);
    					link.append("&" + prop + "=" + bInstance.getIdAux());
    				} else if (prop.equals("id"))
    					link.append("&" + prop + "="+ getIdAux()+"&id="+getIdAux());
    				else
    					link.append("&" + prop + "="+ propertyUtilsBean.getProperty(instancia, prop));
    				
    			} catch (Exception e) {
    				// Os VOs nao utilizam log4j por padrao, por isso este envio diretamente na console.
    				log.error( "jCompany. Erro fatal ao tentar montar link de edicao avancado (com navegacao) do VO "
    						+ instancia
    						+ " para propriedade "
    						+ prop
    						+ ERRO_ORIGINAL
    						+ e.toString());
    				e.printStackTrace();
    			}
    		}
    	}
    	return link.toString();
    	
    }


    /**
     * @since jCompany 2.7
     * Auxiliar para manipulação de VOs com chave natural Se o
     * objeto tem seus identificados informados, seja OID ou chave natural
     *
     * @return true se possui todas as propriedades de identificação informadas
     */
    public boolean isIdentificado() {

    	String rowId = this.getRowId();
        if (!StringUtils.isBlank(rowId))
            return true;

        boolean eIdentificado = true;

        Object[] props = getPropsChaveNaturalPlc();

        if (props == null) {

            // Assume que usa OID
            if (getId() == null)
                eIdentificado = false;

        } else {
            // Forneceu lista de propriedades
            for (int i = 0; i < props.length; i++) {

                String prop = (String) props[i];

                try {
                	 Object valor=null;
                	if (prop.equals("id"))
                		valor = getId();
                	else 
                       valor = propertyUtilsBean.getSimpleProperty(propertyUtilsBean.getSimpleProperty(instancia,"idNatural"), prop);
                    if (valor == null) {
                        eIdentificado = false;
                    }
                } catch (Exception e) {
                	log.error( "Erro ao tentar pegar valor de propriedade "
                                    + prop + " Erro:" + e);
                    e.printStackTrace();
                }

            }

        }

        return eIdentificado;
    }

    public void setIndExcPlc(Boolean value){
    	if (value != null && value.equals(true))
    		setIndExcPlc("S");
    	else	
    		setIndExcPlc("N");
    }

	public T getInstancia() {
		return instancia;
	}


	protected PlcConfigUtil getConfigUtil() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
	}
    
}