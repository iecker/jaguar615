/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.collaboration;

import java.lang.annotation.Annotation;


public class PlcConfigCollaborationPOJO {


	private PlcConfigFormLayout formLayout;

	private PlcConfigHelp help;

	private PlcConfigPrint print;

	private PlcConfigBehavior behavior;

	private PlcConfigProgrammaticValidation[] programmaticValidation;

	private PlcConfigSelection selection;

	private PlcConfigTabular tabular;

	private PlcConfigReport report;

	private PlcConfigNestedCombo[] nestedCombo;

	private PlcConfigRssSelection rssSelection;

	private PlcConfigGoogleMap googleMap;

	public PlcConfigCollaborationPOJO() {
	}	

	public PlcConfigCollaborationPOJO(boolean inicializa) {
		if (inicializa) {
			setHelp();
			setNestedCombo();
			setBehavior();
			setGoogleMap();
			setPrint();
			setFormLayout();
			setReport();
			setRssSelection();
			setSelection();
			setTabular();
			setProgrammaticValidation();
		}

	}

	private void setSelection() {
		selection = new PlcConfigSelection() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				
				return null;
			}
			
			@Override
			public PlcConfigPagination pagination() {
				
				return new PlcConfigPagination() {
					
					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}
					
					@Override
					public TopPosition topPosition() {
						return TopPosition.ABOVE;
					}
					
					@Override
					public TopStyle topStyle() {
						return TopStyle.ARROWS;
					}
					
					@Override
					public int numberByPage() {
						return -1;
					}
					
					@Override
					public DynamicType dynamicType() {
						return DynamicType.STATIC;
					}
				};
			}
			
			@Override
			public PlcConfigExport export() {
				
				return new PlcConfigExport() {
					
					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}
					
					@Override
					public String[] formats() {
						return new String[]{"XML,CSV,CSV2003"};
					}
					
					@Override
					public boolean useExport() {
						return false;
					}
					
					@Override
					public String[] fields() {
						return new String[]{"id"};
					}
				};
			}
			
			
			@Override
			public String apiQuerySel() {
				
				return "";
			}
		};
		
	}

	private void setPrint() {
		print = new PlcConfigPrint() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return null;
			}

			@Override
			public boolean smartPrint() {
				
				return true;
			}
		};

	}

	private void setFormLayout() {
		setFormLayout(new PlcConfigFormLayout() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return PlcConfigFormLayout.class;
			}

			@Override
			public String dirBase() {
				
				return "";
			}

		});

	}


	private void setRssSelection() {
		rssSelection = new PlcConfigRssSelection() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return null;
			}

			@Override
			public String title() {
				
				return "";
			}

			@Override
			public boolean useRss() {
				
				return false;
			}

			@Override
			public String description() {
				
				return "";
			}

			@Override
			public String[] fields() {
				
				return new String[]{};
			}

			@Override
			public String actionMan() {
				
				return "";
			}
		};

	}

	private void setProgrammaticValidation() {
		programmaticValidation = new PlcConfigProgrammaticValidation[]{};
	}


	private void setTabular() {
		tabular = new PlcConfigTabular() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return null;
			}

			@Override
			public String orderProp() {
				
				return "";
			}

			@Override
			public int numNew() {
				
				return 4;
			}
		};

	}

	private void setReport() {
		report = new PlcConfigReport() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return null;
			}

			@Override
			public String urlBirt() {
				
				return "/plcVis/frameset";
			}

			@Override
			public String reportFile() {
				
				return "";
			}
		};

	}


	private void setGoogleMap() {
		googleMap = new PlcConfigGoogleMap() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return null;
			}

			@Override
			public String key() {
				
				return "";
			}
		};

	}

	private void setBehavior() {
		behavior = new PlcConfigBehavior() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return PlcConfigBehavior.class;
			}

			@Override
			public boolean deleteValidationUse() {
				
				return false;
			}


			@Override
			public String windowMode() {
				
				return "";
			}


			@Override
			public boolean useTreeView() {
				
				return false;
			}

			@Override
			public boolean batchInput() {
				
				return false;
			}

			@Override
			public boolean rememberDetail() {
				
				return false;
			}

			@Override
			public boolean useDeleteDetailWarning() {
				
				return false;
			}

			@Override
			public boolean useUpdateWarning() {
				
				return false;
			}
		};

	}

	private void setNestedCombo() {
		nestedCombo = new PlcConfigNestedCombo[]{};
	}

	private void setHelp() {
		help = new PlcConfigHelp() {

			@Override
			public Class<? extends Annotation> annotationType() {
				
				return null;
			}

			@Override
			public String urlHelpOperation() {
				
				return "";
			}

			@Override
			public String urlHelpGlossary() {
				
				return "";
			}

			@Override
			public String urlHelpConcept() {
				
				return "";
			}

			@Override
			public String groupwareHelpDesk() {
				
				return "";
			}

			@Override
			public String groupwareForum() {
				
				return "";
			}

			@Override
			public String groupwareFAQ() {
				
				return "";
			}
		};

	}


	public PlcConfigHelp help() {
		return help;
	}

	public void setHelp(PlcConfigHelp ajuda) {
		this.help = ajuda;
	}

	public PlcConfigPrint print() {
		return print;
	}

	public void setPrint(PlcConfigPrint impressao) {
		this.print = impressao;
	}

	public PlcConfigBehavior behavior() {
		return behavior;
	}

	public void setBehavior(PlcConfigBehavior comportamento) {
		this.behavior = comportamento;
	}


	public PlcConfigProgrammaticValidation[] programmaticValidation() {
		return programmaticValidation;
	}

	public void setProgrammaticValidation(
			PlcConfigProgrammaticValidation[] validacaoProgramatica) {
		this.programmaticValidation = validacaoProgramatica;
	}


	public PlcConfigSelection selection() {
		return selection;
	}

	public void setSelection(PlcConfigSelection selecao) {
		this.selection = selecao;
	}

	public PlcConfigTabular tabular() {
		return tabular;
	}

	public void setTabular(PlcConfigTabular tabular) {
		this.tabular = tabular;
	}

	public PlcConfigReport report() {
		return report;
	}

	public void setReport(PlcConfigReport relatorio) {
		this.report = relatorio;
	}

	public PlcConfigNestedCombo[] nestedCombo() {
		return nestedCombo;
	}

	public void setNestedCombo(PlcConfigNestedCombo[] comboAninhado) {
		this.nestedCombo = comboAninhado;
	}

	public PlcConfigRssSelection rssSelection() {
		return rssSelection;
	}

	public void setRssSelection(PlcConfigRssSelection rssSelecao) {
		this.rssSelection = rssSelecao;
	}

	public PlcConfigGoogleMap googleMap() {
		return googleMap;
	}

	public void setGoogleMap(PlcConfigGoogleMap googleMap) {
		this.googleMap = googleMap;
	}


	/**
	 * @param formLayout the formLayout to set
	 */
	public void setFormLayout(PlcConfigFormLayout formLayout) {
		this.formLayout = formLayout;
	}

	/**
	 * @return the formLayout
	 */
	public PlcConfigFormLayout formLayout() {
		return formLayout;
	}



}
