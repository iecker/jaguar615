/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.aggregation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Comparator;

import javax.persistence.OneToMany;

import org.apache.commons.lang.ArrayUtils;

import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.FormPatternModality;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigGoogleMap;
import com.powerlogic.jcompany.config.collaboration.PlcConfigHelp;
import com.powerlogic.jcompany.config.collaboration.PlcConfigNestedCombo;
import com.powerlogic.jcompany.config.collaboration.PlcConfigPrint;
import com.powerlogic.jcompany.config.collaboration.PlcConfigProgrammaticValidation;
import com.powerlogic.jcompany.config.collaboration.PlcConfigReport;
import com.powerlogic.jcompany.config.collaboration.PlcConfigRssSelection;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;

/**
 * POJO que mantém metadados para definição do grafo de entidades envolvido em uma "Colaboração" (formulário). É um espelho das anotações
 * definidas em PlcConfigAgregacao, para aplicação da "convenção sobre configuração". 
 * 
 * TODO Verificar se é possível obter o resultado removendo-se esta classe
 */
public class PlcConfigAggregationPOJO {
	
    private Class entity;
    
    private PlcConfigForm pattern;
    
    private PlcConfigComponent[] components;
    
    private PlcConfigDescendent[] descendents;
    
    private PlcConfigDetail[] details;
       
    private PlcConfigUserPref userpref;
    
    public PlcConfigAggregationPOJO() {
    	
    }
    
    public PlcConfigAggregationPOJO(boolean inicializa) {
    	if (inicializa) {
    		setComponents((Field[])null);
    		setDescendents();
    		setDetails(null, null);
    		setPattern();
    		setUserPref();
    	}
	}
    
    
	private void setUserPref() {
		userpref = new PlcConfigUserPref() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				
				return PlcConfigUserPref.class;
			}
			
			@Override
			public String argument() {
				
				return "login";
			}
		};
		
	}

	private void setPattern() {
		setPattern((String)null);
	}
	
	public void setPattern(final String nomeLogica) {
		
		pattern = new PlcConfigForm() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				
				return PlcConfigForm.class;
			}
			
			@Override
			public FormPatternModality modality() {
				return FormPatternModality.A;
			}
			
			@Override
			public FormPattern formPattern() {
				FormPattern l = null;
				try {
					if (nomeLogica!=null)
						l = FormPattern.valueOf(nomeLogica);
				} catch (Exception e) { l = FormPattern.Plx; }
				return l;
			}
			
			@Override
			public String collaborationPatternPlx() {
				return nomeLogica;
			}

			@Override
			public ExclusionMode exclusionMode() {
				return ExclusionMode.FISICAL;
			}

			// TODO Baldini. Analisar o impacto de assumir estes valores abaixo após a 
			// refatoracao do PlcConfigPattern para PlcConfigForm
			@Override
			public PlcConfigFormLayout formLayout() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigHelp help() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigPrint print() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigBehavior behavior() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigProgrammaticValidation[] programmaticValidation() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigTabular tabular() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigReport report() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigNestedCombo[] nestedCombo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigRssSelection rssSelection() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigGoogleMap googleMap() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PlcConfigSelection selection() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String[] ctbHeaderProperties() {
				// TODO Auto-generated method stub
				return null;
			}

		};
		
	}


	public void setPattern(final String nomeLogica, final PlcConfigForm configForm) {
		
		pattern = new PlcConfigForm() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				
				return PlcConfigForm.class;
			}
			
			@Override
			public FormPatternModality modality() {
				return configForm.modality();
			}
			
			@Override
			public FormPattern formPattern() {
				FormPattern l = null;
				try {
					if (nomeLogica!=null)
						l = FormPattern.valueOf(nomeLogica);
				} catch (Exception e) { l = FormPattern.Plx; }
				return l;
			}
			
			@Override
			public String collaborationPatternPlx() {
				return nomeLogica;
			}

			@Override
			public ExclusionMode exclusionMode() {
				return configForm.exclusionMode();
			}

			@Override
			public PlcConfigFormLayout formLayout() {
				return configForm.formLayout();
			}

			@Override
			public PlcConfigHelp help() {
				return configForm.help();
			}

			@Override
			public PlcConfigPrint print() {
				return configForm.print();
			}

			@Override
			public PlcConfigBehavior behavior() {
				return configForm.behavior();
			}

			@Override
			public PlcConfigProgrammaticValidation[] programmaticValidation() {
				return configForm.programmaticValidation();
			}

			@Override
			public PlcConfigTabular tabular() {
				return configForm.tabular();
			}

			@Override
			public PlcConfigReport report() {
				return configForm.report();
			}

			@Override
			public PlcConfigNestedCombo[] nestedCombo() {
				return configForm.nestedCombo();
			}

			@Override
			public PlcConfigRssSelection rssSelection() {
				return configForm.rssSelection();
			}

			@Override
			public PlcConfigGoogleMap googleMap() {
				return configForm.googleMap();
			}

			@Override
			public PlcConfigSelection selection() {
				return configForm.selection();
			}

			@Override
			public String[] ctbHeaderProperties() {
				return configForm.ctbHeaderProperties();
			}

		};
		
	}
	
	public void setDetails(Field[] fields, Field[] subdetalhes) {
		if (fields!=null) {
			PlcConfigDetail[] _detalhes = new PlcConfigDetail[fields.length];
			int cont = 0;
			for (int i = 0; i < fields.length; i++) {
				final Field f = fields[i];
				// Os OneToMany configuração com PLcArquivo Anexado não são detalhes.
				if (f.getAnnotation(PlcFileAttach.class)!=null)
					continue;
				
				Class _classeSubdet = Object.class;
				String _nomeColecaoSubdet = "";
				final Field fSubdet = (subdetalhes!=null && subdetalhes[i]!=null)?subdetalhes[i]:null;
				if (fSubdet!=null){
					_classeSubdet = fSubdet.getAnnotation(OneToMany.class).targetEntity();
					_nomeColecaoSubdet = fSubdet.getName();
				}
				final Class classeSubdet = _classeSubdet;
				final String nomeColecaoSubdet = _nomeColecaoSubdet;
				
				PlcConfigDetail detalhe = new PlcConfigDetail() {
					
					@Override
					public Class<? extends Annotation> annotationType() {
						return PlcConfigDetail.class;
					}
									
					@Override
					public PlcConfigSubDetail subDetail() {
						return  new PlcConfigSubDetail() {
							
							@Override
							public Class<? extends Annotation> annotationType() {
								return PlcConfigSubDetail.class;
							}
							
							@Override
							public int numNew() {
								return 2;
							}
							
							@Override
							public String collectionName() {
								return nomeColecaoSubdet;
							}
							
							@Override
							public Class<? extends Comparator> comparator() {
								return Comparator.class;
							}
							
							@Override
							public Class clazz() {
								return classeSubdet;
							}
							
						/*	@Override
							public String multiplicity() {
								if (fSubdet.getAnnotation(PlcValMultiplicity.class)!=null) {
									String min = "0";
									String max = "*";
									if (fSubdet.getAnnotation(PlcValMultiplicity.class).min()>-1)
										min = Integer.toString(fSubdet.getAnnotation(PlcValMultiplicity.class).min());
									if (fSubdet.getAnnotation(PlcValMultiplicity.class).max()<Integer.MAX_VALUE)
										max = Integer.toString(fSubdet.getAnnotation(PlcValMultiplicity.class).max());
										
									return min+".."+max;
								}
								else
									return "0..*";
							}*/

							@Override
							public String despiseProperty() {
								return null;
							}

							@Override
							public boolean testDuplicity() {
								return true;
							}

							@Override
							public ExclusionMode exclusionMode() {
								return exclusionMode();
							}
						};
					}
					
					@Override
					public boolean onDemand() {
						return false;
					}
					
					@Override
					public int numNew() {
						return 4;
					}
					
					@Override
					public String collectionName() {
						return f.getName();
					}
					
					@Override
					public PlcConfigPagedDetail navigation() {
						return null;
					}
					
					@Override
					public ExclusionMode exclusionMode() {
						return ExclusionMode.FISICAL;
					}
					
					@Override
					public Class<? extends Comparator> comparator() {
						return Comparator.class;
					}
					
					@Override
					public Class clazz() {
						return f.getAnnotation(OneToMany.class).targetEntity();
					}

					@Override
					public PlcConfigComponent[] components() {						
						return null;
					}

				};
				_detalhes[i] = detalhe;
				cont++;
			}
			
			details = (PlcConfigDetail[])ArrayUtils.subarray(_detalhes, 0, cont);
		}
		
	}


	private void setDescendents() {
		descendents = new PlcConfigDescendent[]{};
	}


	public void setComponents(Field[] fields) {
		if (fields!=null) {
			components = new PlcConfigComponent[fields.length];
			for (int i = 0; i < fields.length; i++) {
				final Field f = fields[i];
				PlcConfigComponent componente = new PlcConfigComponent() {
					
					@Override
					public Class<? extends Annotation> annotationType() {
						return PlcConfigComponent.class;
					}
					
					@Override
					public String property() {
						return f.getName();
					}
					
					@Override
					public Class clazz() {
						return f.getType();
					}

					@Override
					public boolean separate() {
						return true;
					}
				};
				components[i] = componente;
			}
			
			
		}

	}


	public Class entity() {
		return entity;
	}

	public void setEntity(Class entidade) {
		this.entity = entidade;
	}

	public PlcConfigForm pattern() {
		return pattern;
	}

	public void setPattern(PlcConfigForm padrao) {
		this.pattern = padrao;
	}

	public PlcConfigComponent[] components() {
		return components;
	}

	public void setComponents(PlcConfigComponent[] componentes) {
		this.components = componentes;
	}

	public PlcConfigDescendent[] descendents() {
		return descendents;
	}

	public void setDescendents(PlcConfigDescendent[] descendentes) {
		this.descendents = descendentes;
	}

	public PlcConfigDetail[] details() {
		return details;
	}

	public void setDetails(PlcConfigDetail[] detalhes) {
		this.details = detalhes;
	}

	public PlcConfigUserPref userpref() {
		return userpref;
	}

	public void setUserpref(PlcConfigUserPref prefusuario) {
		this.userpref = prefusuario;
	}

}
