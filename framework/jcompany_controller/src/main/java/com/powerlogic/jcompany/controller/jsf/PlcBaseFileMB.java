/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OneToOne;
import javax.servlet.ServletOutputStream;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.PlcFileContent;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcFileUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.bindingtype.PlcDownloadFileAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcDownloadFileBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcUploadFileAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcUploadFileBefore;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

/**
 * Encapsula implementações de controle para gerenciar uploads e donwloads de arquivos.
 */
@QPlcDefault
public class PlcBaseFileMB extends PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;		
	
	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;
	
	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;	

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;	
	
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;	
	
	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;
	
	
	/**
	 * Fazer upload de arquivos
	 */
	public void uploadFile(ValueChangeEvent event, Object entityPlc) {
		
		this.entityPlc = entityPlc;

		try {

			// Recuperando arquivo informado pelo usuário no componente de inputFile
			UploadedFile file = (UploadedFile) event.getNewValue();

			/** Achar uma forma de tratar arquivo vazio
				if (file == null) {
					//arquivo vazio
					msgUtil.msgError(PlcBeanMessages.JCOMPANY_ERROR_FILE_EMPTY, new String[] {});
					return;
				}
			*/
			
			// Verificando se o caso de uso corrente existe a configuração de arquivo anexado.
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			
			PlcConfigAggregationPOJO configDominio = configUtil.getConfigAggregation(url);

			Field [] camposAnotados = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(configDominio.entity(), PlcFileAttach.class);

			for (Field field : camposAnotados) {
				
				PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
				
				if(StringUtils.isNotBlank(getNameFile()) && field.getName().equals(getNameFile())) {
					
					if (uploadFileBefore(event,fileAttach,file) && !field.getType().equals(PlcFile.class) && file != null) {
							
						// Se for imagem sempre será arquivo único
						if (!fileAttach.image() && fileAttach.multiple()) {
							uploadMultipleFile(file, fileAttach,field);
						} else {
							uploadUniqueFile(file, fileAttach,field);
						}
						
					}
				}
			}
			
			if (file != null) {
				file.dispose();
			}

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseFileMB", "uploadFile", e, log, "");

		}
		
	}

	/**
	 * Faz upload de um único arquivo
	 * @param uploadedFile
	 * @param fileAttach
	 * @param field Campo que contém os metadados anotados
	 */
	protected void uploadUniqueFile(UploadedFile uploadedFile, PlcFileAttach fileAttach, Field field) throws Exception {
		
		IPlcFile file = null;
		file = (IPlcFile) field.getType().newInstance();
		file.setNome(uploadedFile.getFilename());
		
		DataInputStream dataInputArq = new DataInputStream(uploadedFile.getInputStream());
		int size = Integer.parseInt(String.valueOf(uploadedFile.getLength()));
		byte bytesArq[] = new byte[size];
		
		dataInputArq.readFully(bytesArq);
		dataInputArq.close();
		PlcFileContent conteudo = null;
		Method m = reflectionUtil.findMethodHierarchically(file.getClass(), "getBinaryContent");
		
		if (m!=null && m.getAnnotation(OneToOne.class)!=null) {
			conteudo = (PlcFileContent)m.getAnnotation(OneToOne.class).targetEntity().newInstance();
		} else{ 
			conteudo = (PlcFileContent)reflectionUtil.findFieldHierarchically(file.getClass(), "conteudo").getType().newInstance();
		}
		
		conteudo.setBinaryContent(bytesArq);
		file.setBinaryContent(conteudo);
		file.setLength(size);
		file.setType(uploadedFile.getContentType());
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcFileUtil fileUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcFileUtil.class, QPlcDefaultLiteral.INSTANCE);

		// validando o arquivo
		fileUtil.isValid(file, fileAttach, url);
		
		// Templated Method
		uploadFileAfter(file);
		
		PropertyUtils.setProperty(entityPlc, field.getName(), file);
		
		contextUtil.getRequest().getSession().setAttribute(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()) + "_" + field.getName(),file);
	}

	/**
	 * Faz upload de múltiplos arquivos
	 * @param uploadedFile
	 * @param fileAttach
	 * @param field 
	 */
	protected void uploadMultipleFile(UploadedFile uploadedFile, PlcFileAttach fileAttach, Field field) throws Exception {
		
		List<IPlcFile> files = null;
			
		try {
			files = (List<IPlcFile>) PropertyUtils.getProperty(entityPlc, field.getName()); 
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PROPERTY_NOT_FOUND, 
							new Object[] {field.getName(), entityPlc }, e, log);
		}

		if (files == null) {
			files = new ArrayList();
		}

		if (fileAttach.testDuplicated()) {

			String fileName = uploadedFile.getFilename();

			for (Object object : files) {

				PlcFile arq = (PlcFile) object;

				if (arq.getNome().equals(fileName)) {
					throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_DUPLICATED_MULTIPLE_FILES);
				}
			}

		}

		int numberFiles = fileAttach.fileCount();

		if ((numberFiles == -1 || numberFiles > files.size()) && files.size() >= 0) {

			if (uploadedFile instanceof UploadedFile) {

				PlcFile file = (PlcFile) reflectionUtil.getGenericTypeOfCollection(field.getGenericType()).newInstance(); 
				
				
				file.setNome(uploadedFile.getFilename());
				file.setType(uploadedFile.getContentType());

				DataInputStream inputStream = null;
				try {
					inputStream = new DataInputStream(uploadedFile.getInputStream());

					int size = Integer.parseInt(String.valueOf(uploadedFile.getLength()));
					byte bytesArq[] = new byte[size];

					inputStream.readFully(bytesArq);

					PlcFileContent conteudo = null;
					Method m = reflectionUtil.findMethodHierarchically(file.getClass(), "getBinaryContent");
					
					if (m!=null && m.getAnnotation(OneToOne.class)!=null) { 
						conteudo = (PlcFileContent)m.getAnnotation(OneToOne.class).targetEntity().newInstance();
					} else { 
						conteudo = (PlcFileContent)reflectionUtil.findFieldHierarchically(file.getClass(), "conteudo").getType().newInstance();
					}
					
					conteudo.setBinaryContent(bytesArq);

					file.setBinaryContent(conteudo);

					file.setLength(size);

					String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
					PlcFileUtil fileUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcFileUtil.class, QPlcDefaultLiteral.INSTANCE);

					// validando o arquivo
					fileUtil.isValid(file, fileAttach, url);

					
					files.add(file);

					uploadFileAfter(file);

					PropertyUtils.setProperty(entityPlc, field.getName(), files);
					
				} finally {

					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							log.debug( "Erro ao fechar arquivo de upload", e);
						}
					}
				}
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_MAXIMUM_MULTIPLE_FILES);
			}

		}
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer). Obs.: no caso de arquivos, a utilização difere
	 * ligeiramente dos demais, ficando os métodos antes e após o upload de cada arquivo
	 * @param file Arquivo Arquivo a ser gravado
	 * @param fileAttach Metadados configurados para o arquivo 
	 */
	protected boolean uploadFileBefore(ValueChangeEvent event, PlcFileAttach fileAttach, UploadedFile file) {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcUploadFileBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer). Obs.: no caso de arquivos, a utilização difere
	 * ligeiramente dos demais, ficando os métodos antes e após o upload de cada arquivo
	 * @param arqEntity Arquivo recém gravado
	 */
	protected IPlcFile uploadFileAfter(IPlcFile arqEntity) {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcUploadFileAfter>(){});
		return arqEntity;
	}

	/**
	 * Faz o download de um arquivo
	 */
	public void downloadFile(Object entityPlc) {
		this.downloadFile(entityPlc, getNameFile(), getIndexFile());
	}

	/**
	 * Faz o download de um arquivo
	 */
	public void downloadFile(Object entityPlc, String property, Integer index)  {

		this.entityPlc = entityPlc;

		try {

			// Templated Method
			boolean temDownloadArquivo = downloadFileBefore();
			// Recuperando a referência do request
			if (!temDownloadArquivo) {
				return;
			}
			
			// Se entrou neste método tem que ter configuração de arquivo anexado
			String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
			
			PlcConfigAggregationPOJO configDominio = configUtil.getConfigAggregation(url);
			
			Field field = reflectionUtil.findFieldHierarchically(entityPlc.getClass(), property);
			
			if (field == null){
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_FILE_DOWNLOAD_FILE_NOT_DEFINED, null, log);
			}
			
			PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
				
					
			if (field.getType().equals(PlcFile.class)){
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_FILE_DOWNLOAD_FILE_NOT_DEFINED, null, log);
			}
			
			if (!fileAttach.multiple() || fileAttach.image()){
				
				// montando um contexParam
				contextMontaUtil.createContextParam(plcControleConversacao);
				PlcBaseContextVO context 		= (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
				
				FacesContext facesContext  = FacesContext.getCurrentInstance();
				
				if (! facesContext.getResponseComplete()){
					IPlcFile arqEntity = null;
					if (IPlcFile.class.isAssignableFrom(field.getType())) {
						arqEntity = (IPlcFile) PropertyUtils.getProperty(entityPlc, property);
						
						// O arquivo é recuperado novamente porque por motivo de otimização o jcompany não recupera todo o VO do arquivo. 
						arqEntity = iocControleFacadeUtil.getFacade(url).downloadFile(context,arqEntity.getClass(),new Long(arqEntity.getId()));
						
						// Templated Method
						arqEntity = downloadFileAfter(arqEntity);
						
						String mime 	= arqEntity.getType() != null ? arqEntity.getType().trim(): null;
						byte[] document = (byte[]) arqEntity.getBinaryContent().getBinaryContent();
						
						ServletOutputStream out = contextUtil.getResponse().getOutputStream();
						try {
							contextUtil.getResponse().reset();
							if (mime == null || mime.equals("")) {
								mime = "application/octet-strStream";
								contextUtil.getResponse().setContentType(mime);
							} else
								contextUtil.getResponse().setContentType(mime);
							
							//Ajuste para o IE não substituir espaço por "_"
							if (contextUtil.getRequest().getHeader("User-Agent").contains("MSIE")) {
								contextUtil.getResponse().setHeader("Content-Disposition","attachment; filename=\"" + arqEntity.getNome().replace(" ", "%20") + "\"");	
							} else {
								contextUtil.getResponse().setHeader("Content-Disposition","attachment; filename=\"" + arqEntity.getNome() + "\"");
							}
							
							contextUtil.getResponse().setContentLength(document.length);
							out.write(document, 0, document.length);
							
						} finally {
							// Último comando deve ser o close
							contextUtil.getResponse().flushBuffer();
							out.flush();
							out.close();
							FacesContext.getCurrentInstance().responseComplete();
						}
					}
				}
				
			} else {
				
				ServletOutputStream out = contextUtil.getResponse().getOutputStream();
				
				try {
					
					List arquivosAnexados = (List)PropertyUtils.getProperty(entityPlc, field.getName());
					IPlcFile arqEntity = (IPlcFile)arquivosAnexados.get(index);
					contextMontaUtil.createContextParam(plcControleConversacao);
					PlcBaseContextVO context 		= (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
					// O arquivo é recuperado novamente porque por motivo de otimização o jcompany não recupera todo o VO do arquivo. 
					arqEntity = iocControleFacadeUtil.getFacade(url).downloadFile(context, arqEntity.getClass(), arqEntity.getId());
					byte[] document = (byte[]) arqEntity.getBinaryContent().getBinaryContent();
					
					String mime 	= arqEntity.getType() != null ? arqEntity.getType().trim(): null;
					contextUtil.getResponse().reset();
					if (mime == null || mime.equals("")) {
						mime = "application/octet-strStream";
						contextUtil.getResponse().setContentType(mime);
					} else
						contextUtil.getResponse().setContentType(mime);
					
					//Ajuste para o IE não substituir espaço por "_"
					if (contextUtil.getRequest().getHeader("User-Agent").contains("MSIE")) {
						contextUtil.getResponse().setHeader("Content-Disposition","attachment; filename=\"" + arqEntity.getNome().replace(" ", "%20") + "\"");	
					} else {
						contextUtil.getResponse().setHeader("Content-Disposition","attachment; filename=\"" + arqEntity.getNome() + "\"");
					}
					
					contextUtil.getResponse().setContentLength(document.length);
					out.write(document, 0, document.length);
					
				} finally {
					// Último comando deve ser o close
					contextUtil.getResponse().flushBuffer();
					out.flush();
					out.close();
					FacesContext.getCurrentInstance().responseComplete();
				}
			}
			
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcBaseFileMB", "downloadFile", e, log, "");
		}
	}
	
	/**
	 * @return Posição do arquivo clicado quando se tratar de múltiplos arquivos anexados
	 */
	protected Integer getIndexFile() {
		String posicaoArquivo = contextUtil.getRequestParameter("indexFile");
		if (StringUtils.isNumeric(posicaoArquivo)) {
			return Integer.valueOf(posicaoArquivo);
		}
		return 0;
	}
	
	/**
	 * @return Nome do arquivo quando se tratar de múltiplos arquivos anexados
	 */
	protected String getNameFile() {
		
		String name = null;
		
		name = contextUtil.getRequestParameter("nameFile");
		
		if (StringUtils.isEmpty(name) ) {
			name = "";
		}
		return name;
	
	}

	/**
	 * @since jCompany 5.0 Design Pattern: Template Method ou Observer. Os métodos com
	 *        sufixo "Before" ou "After" ou "api" são eventos (método vazios)
	 *        destinados a especializações nos descendentes.
	 */
	protected boolean downloadFileBefore()  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcDownloadFileBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * @since jCompany 5.0 Design Pattern: Template Method ou Observer. Os métodos com
	 *        sufixo "Before" ou "After" ou "api" são eventos (método vazios)
	 *        destinados a especializações nos descendentes.
	 */
	protected IPlcFile downloadFileAfter(IPlcFile file) {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcDownloadFileAfter>(){});
		return file;
	}

	/**
	 * Habilita para exibir arquivo a ser anexado. Necessário para evitar forms
	 * do tipo multipart sem necessidade.
	 */
	public String modeFileAttach()  {

		log.debug( "Entrou em modoAnexarArquivo");

		contextUtil.getRequest().setAttribute(PlcConstants.ARQUIVO.IND_ARQ_ANEXADO, "S");
		
		return defaultNavigationFlow;
	}
	
	/*
	public void removeFileAttach(Object entityPlc) {
		PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(entityPlc);
		entityPlcInstance.setFileAttach(null);
		baseCreateMB.clearFileAttachInSession(entityPlc, false);
	}
	*/
}
