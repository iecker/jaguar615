/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.simple;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.atom.Content;
import org.jboss.resteasy.plugins.providers.atom.Link;
import org.jboss.resteasy.plugins.providers.atom.Person;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBContextFinder;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBMarshalException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcEntityId;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;
import com.powerlogic.jcompany.controller.rest.conversors.PlcBaseConversor;
import com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom.PlcClassMapper;
import com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom.PlcEntityConverter;
import com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom.PlcEntry;
import com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom.PlcFeed;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomReader;
import com.thoughtworks.xstream.io.xml.DomWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

@SPlcConversor
@QPlcConversorMediaType( { "application/atom+xml" })
@QPlcConversorToController(type=PlcBaseGridController.class)
public class PlcAtomConversor<C> extends PlcBaseConversor<C> {

	protected static final NamespacePrefixMapper NAMESPACE_PREFIX_MAPPER = new NamespacePrefixMapper() {
		public String getPreferredPrefix(String namespace, String s1, boolean b) {
			if (namespace.equals("http://www.w3.org/2005/Atom"))
				return "";
			if (namespace.equals("http://www.powerlogic.com.br/2009/AtomPlcExt"))
				return "plcAtom";
			return s1;
		}
	};

	@Inject
	protected Logger log;
	@Inject
	protected UriInfo uriInfo;
	@Inject
	protected Providers providers;

	@Inject
	@QPlcEntityId
	protected String entidadeId;

	protected XStream xstream;
	
	@Inject
	@QPlcDefault
	protected PlcEntityConverter entityConverter;

	@Override
	public void writeEntity(C _container, OutputStream outputStream) {

		try {
			PlcBaseGridController container= (PlcBaseGridController)_container;
			
			String nomeClasseEntidade = container.getEntityType().getName();

			PlcEntry entry = createEntryForEntity(nomeClasseEntidade, container.getEntity(), true);

			JAXBContext jaxbContext = getContextFinder().createContext(null, new Class[] { PlcEntry.class });

			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", NAMESPACE_PREFIX_MAPPER);

			marshaller.marshal(entry, outputStream);

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new JAXBMarshalException("Unable to marshal atom", e);
		}
	}

	@Override
	public void readEntity(C _container, InputStream inputStream) {
		try {
			PlcBaseGridController container= (PlcBaseGridController)_container;

			if (inputStream != null) {

				String nomeClasseEntidade = container.getEntityType().getName();
				
				JAXBContext jaxbContext = getContextFinder().createContext(null, new Class[] { PlcEntry.class });
				
				PlcEntry plcEntry = (PlcEntry) jaxbContext.createUnmarshaller().unmarshal(inputStream);
				
				Object obtemEntidadeDeEntry = getEntityEntry(nomeClasseEntidade, plcEntry, entidadeId);
				
				container.setEntity(obtemEntidadeDeEntry);
			}
			else if(container.getIdentifier()!=null){
				Object id = container.getIdentifier();
				if(id instanceof Long){
					Object beanInstance = container.getEntityType().newInstance();
					PropertyUtils.setProperty(beanInstance, "id", id);
					container.setEntity(beanInstance);
				}
			}
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new JAXBMarshalException("Unable to unmarshal atom", e);
		}
	}

	@Override
	public void writeEntityCollection(C _container, OutputStream outputStream) {

		try {
			PlcBaseGridController container= (PlcBaseGridController)_container;

			String nomeClasseEntidade = container.getEntityType().getName();

			List<PlcEntry> listaEntries = createEntriesList(nomeClasseEntidade, container.getEntityCollection());

			PlcFeed plcFeed = createFeed(nomeClasseEntidade, listaEntries);

			JAXBContext jaxbContext = getContextFinder().createContext(null, new Class[] { PlcFeed.class });

			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", NAMESPACE_PREFIX_MAPPER);

			marshaller.marshal(plcFeed, outputStream);
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new JAXBMarshalException("Unable to marshal atom", e);
		}
	}

	@Override
	public void readEntityCollection(C _container, InputStream inputStream) {
		try {

			PlcBaseGridController container= (PlcBaseGridController)_container;

			if (inputStream != null) {

				Class<Object> tipoEntidade = container.getEntityType();

				if (tipoEntidade != null) {

					String nomeClasseEntidade = tipoEntidade.getName();

					JAXBContext jaxbContext = getContextFinder().createContext(null, new Class[] { PlcFeed.class });

					PlcFeed plcFeed = (PlcFeed) jaxbContext.createUnmarshaller().unmarshal(inputStream);

					List<PlcEntry> entries = plcFeed.getEntries();

					Collection<Object> entidades = new ArrayList<Object>(entries.size());

					for (PlcEntry plcEntry : entries) {
						entidades.add(getEntityEntry(nomeClasseEntidade, plcEntry, null));
					}

					container.setEntityCollection(entidades);
				}
			}
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new JAXBMarshalException("Unable to unmarshal atom", e);
		}
	}

	@Override
	public void writeException(C container, OutputStream outputStream, Throwable ex) {

		try {
			PlcEntry entry = createEntryForException(ex);

			JAXBContext jaxbContext = getContextFinder().createContext(null, new Class[] { PlcEntry.class });

			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", NAMESPACE_PREFIX_MAPPER);

			marshaller.marshal(entry, outputStream);
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new JAXBMarshalException("Unable to unmarshal atom", e);
		}
		// super.escreveExcecao(container, outputStream);
	}

	protected JAXBContextFinder getContextFinder() {
		ContextResolver<JAXBContextFinder> resolver = providers.getContextResolver(JAXBContextFinder.class, MediaType.APPLICATION_ATOM_XML_TYPE);
		if (resolver == null) {
			throw new JAXBMarshalException("Unable to find JAXBContext for media type: " + MediaType.APPLICATION_ATOM_XML_TYPE);
		}
		return resolver.getContext(null);
	}

	protected PlcFeed createFeed(String nomeClasseEntidade, List<PlcEntry> listaEntries) {
		PlcFeed feed = new PlcFeed();
		feed.setId(uriInfo.getAbsolutePath());
		feed.setTitle(nomeClasseEntidade);

		feed.setUpdated(new Date());
		feed.getLinks().add(new Link("self", uriInfo.getAbsolutePath()));
		feed.getAuthors().add(new Person("jCompany Service Layer"));
		if (listaEntries != null) {
			feed.setEntries(listaEntries);
		}
		return feed;
	}

	protected List<PlcEntry> createEntriesList(String nomeClasseEntidade, Collection<?> listaEntidades) {

		List<PlcEntry> listaPlcEntry = null;

		if (listaEntidades != null && listaEntidades.size() > 0) {
			listaPlcEntry = new ArrayList<PlcEntry>();
			for (Object obj : listaEntidades) {
				boolean incluiEntryContent = false;
				PlcEntry entry = createEntryForEntity(nomeClasseEntidade, obj, incluiEntryContent);
				listaPlcEntry.add(entry);
			}
		}

		return listaPlcEntry;
	}

	protected PlcEntry createEntryForException(Throwable excecao) {

		StringWriter sw = new StringWriter();
		PrintWriter ps = new PrintWriter(sw);

		PlcEntry entry = new PlcEntry();

		//TODO: Mensagens
		if ((excecao instanceof PlcException) && (((PlcException) excecao).getCausaRaiz() != null)) {
			PlcException plcEx = (PlcException) excecao;
			entry.setErroMensagem(plcEx.getMensagemRaiz());
		} else {
			excecao.printStackTrace(ps);
			entry.setErroLog(sw.toString());
			entry.setErroMensagem(excecao.getMessage());
		}

		return entry;
	}

	/**
	 * Cria um PlcEntry para a entidade
	 * 
	 * @param nomeClasseEntidade
	 * @param obj
	 * @param incluiEntryContent
	 * @return
	 * 
	 */
	// TODO - Documentar método criaEntryParaEntidade.
	protected PlcEntry createEntryForEntity(String nomeClasseEntidade, Object obj, boolean incluiEntryContent) {

		PlcEntry entry = new PlcEntry();

		String absolutePathAjustado = uriInfo.getAbsolutePath().toString();

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (obj != null && metamodelUtil.isEntityClass(obj.getClass())) {
			try {

				PlcEntityInstance<Object> entityInstance = metamodelUtil.createEntityInstance(obj);

				@SuppressWarnings("unchecked")
				Map<String, Object> mapaAtributos = PropertyUtils.describe(obj);

				// id
				if (!mapaAtributos.containsKey("id")) {
					throw new PlcException("Impossível definir ID para Atom Entry. Propriedade \"id\" inexistente na entidade: " + obj + ", classe " + obj.getClass());
				}
				String entidadeId = entityInstance.getIdAux();
				
				String entidadePathId = absolutePathAjustado.contains("(" + entidadeId + ")") ? absolutePathAjustado : absolutePathAjustado + "(" + entidadeId + ")" ;
				entry.setId(new URI(entidadePathId));

				// updated
				Date dataUltAlteracao = null;
				if (mapaAtributos.containsKey("dataUltAlteracao") && mapaAtributos.get("dataUltAlteracao") instanceof Date) {
					dataUltAlteracao = (Date) mapaAtributos.get("dataUltAlteracao");
					entry.setUpdated(dataUltAlteracao);
				} else {
					log.debug( "Atributo \"updated\" não definido para Atom Entry: propriedade \"dataUltAlteracao\" inexistente ou não é do tipo java.util.Date na entidade: " + obj + ", classe " + nomeClasseEntidade);
				}

				// published
				if (mapaAtributos.containsKey("dataCriacao") && mapaAtributos.get("dataCriacao") instanceof Date) {
					Date dataCriacao = (Date) mapaAtributos.get("dataCriacao");
					entry.setUpdated(dataCriacao);
				} else {
					if (dataUltAlteracao != null) {
						log.debug( "Usando \"dataUltAlteracao\" como atributo \"published\": propriedade \"dataCriacao\" inexistente ou não é do tipo java.util.Date na entidade: " + obj + ", classe " + nomeClasseEntidade);
						entry.setPublished(dataUltAlteracao);
					} else {
						log.debug( "Atributo \"published\" não definido para Atom Entry: propriedade \"dataCriacao\" inexistente ou não é do tipo java.util.Date na entidade: " + obj + ", classe " + nomeClasseEntidade);
					}
				}

				// title = lookup da entidade
				entry.setTitle(obj.toString());

				String usuarioUltAlteracao = null;
				if (mapaAtributos.containsKey("usuarioUltAlteracao")) {
					usuarioUltAlteracao = mapaAtributos.get("usuarioUltAlteracao").toString();
					entry.getAuthors().add(new Person(usuarioUltAlteracao));
				} else {
					log.debug( "Atributo \"authors\" não definido para Atom Entry: propriedade \"usuarioUltAlteracao\" inexistente: " + obj + ", classe " + nomeClasseEntidade);
				}

				// link rel=alternate
				if (!incluiEntryContent) {
					entry.getLinks().add(new Link("alternate", entidadePathId));
				}

				// link rel=self
				entry.getLinks().add(new Link("self", entidadePathId));

				// link rel=edit
				if (mapaAtributos.containsKey("versao")) {
					entry.getLinks().add(new Link("edit", entidadePathId));
				} else {
					log.debug( "Impossível deduzir correntamente atributo <link rel=\"edit\"> para Atom Entry: propriedade \"versao\" inexistente: " + obj + ", classe " + nomeClasseEntidade);
				}
			} catch(PlcException plcE){
				throw plcE;
			} catch (Exception e) {
				throw new PlcException("Falha de reflexão ao tentar ler atributos do objeto " + obj + ", classe " + obj.getClass() + ": " + e.getMessage());
			}

		}
		
		if (incluiEntryContent) {
			Content entryContent = createEntryContent(obj);
			entry.setContent(entryContent);
		}
		
		return entry;
	}

	protected XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream(null,new PlcClassMapper(),new XppDriver());
			xstream.registerConverter(entityConverter);
		}
		return xstream;
	}

	protected Content createEntryContent(Object obj) {
		Content content = new Content();
		content.setType(MediaType.APPLICATION_XML_TYPE);
		Document docXML = createDocumentXML();
		getXStream().marshal(obj, new DomWriter(docXML));
		content.setElement(docXML.getDocumentElement());
		return content;
	}

	protected Document createDocumentXML() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.newDocument();
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException(e);
		}
	}

	protected Object getEntityEntry(String nomeClasseEntidade, PlcEntry entry, Object entidadeIdentificador) {

		Object obj = null;

		if (entry != null && entry.getContent() != null) {
			Element elementXML = entry.getContent().getElement();
			if (elementXML != null) {
				obj = getXStream().unmarshal(new DomReader(elementXML));
			}
		} else {
			try {
				obj = Class.forName(nomeClasseEntidade).newInstance();
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_OBJECT_INSTANCE, new Object[]{nomeClasseEntidade, e.getClass(), e.getMessage()}, true);
			}
		}

		// Ajusta atributos do objeto
		if (obj != null) {
			try {
				// Map mapaAtributos = PropertyUtils.describe(obj);
				// if (entidadeVersao != null) {
				// if (mapaAtributos.containsKey("versao")) {
				// if (mapaAtributos.get("versao") instanceof Integer) {
				// Integer versaoInteger = Integer.parseInt(entidadeVersao);
				// PropertyUtils.setProperty(obj, "versao", versaoInteger);
				// }
				// } else {
				// log.warn(
				// "Propriedade \"versao\" inexistente: "+obj+", classe "+nomeClasseEntidade);
				// }
				// }
				// if (entidadeIdentificador != null) {
				// if (mapaAtributos.containsKey("id")) {
				// if (PropertyUtils.getPropertyType(obj,
				// "id").equals(Long.class)) {
				// Long identificadorLong =
				// Long.parseLong(entidadeIdentificador);
				// PropertyUtils.setProperty(obj, "id", identificadorLong);
				// }
				// } else {
				// log.warn(
				// "Propriedade \"id\" inexistente: "+obj+", classe "+nomeClasseEntidade);
				// }
				// }
			} catch (Exception e) {
				throw new PlcException("Falha de reflexão ao tentar ler atributos do objeto " + obj + ", classe " + obj.getClass() + ": " + e.getMessage());
			}
		}
		return obj;
	}
}
