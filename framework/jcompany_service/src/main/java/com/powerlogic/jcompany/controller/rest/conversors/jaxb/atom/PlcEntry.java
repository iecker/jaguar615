/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jboss.resteasy.plugins.providers.atom.Category;
import org.jboss.resteasy.plugins.providers.atom.CommonAttributes;
import org.jboss.resteasy.plugins.providers.atom.Content;
import org.jboss.resteasy.plugins.providers.atom.Link;
import org.jboss.resteasy.plugins.providers.atom.Person;
import org.jboss.resteasy.plugins.providers.atom.Source;

@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "title", "links", "categories", "updated", "id", "published", "authors", "contributors", "source", "rights", "content", "summary", "okMensagem", "erroMensagem", "erroLog" })
public class PlcEntry extends CommonAttributes {
	private List<Person> authors = new ArrayList<Person>();

	private List<Category> categories = new ArrayList<Category>();

	private Content content;

	private List<Person> contributors = new ArrayList<Person>();

	private URI id;

	private List<Link> links = new ArrayList<Link>();

	private Date published;

	private String title;
	private Date updated;

	private String rights;

	private Source source;

	private String summary;

	// Extenssoes PLC
	private String okMensagem;

	private String erroMensagem;

	private String erroLog;

	@XmlElement
	public URI getId() {
		return id;
	}

	public void setId(URI id) {
		this.id = id;
	}

	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Link getLinkByRel(String name) {
		for (Link link : links)
			if (link.getRel().equals(name))
				return link;
		return null;
	}

	@XmlElementRef
	public List<Link> getLinks() {
		return links;
	}

	@XmlElementRef
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@XmlElement(name = "author")
	public List<Person> getAuthors() {
		return authors;
	}

	@XmlElementRef
	public List<Category> getCategories() {
		return categories;
	}

	@XmlElement(name = "contributor")
	public List<Person> getContributors() {
		return contributors;
	}

	@XmlElement
	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	@XmlElement
	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	@XmlElement
	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	@XmlElement
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@XmlElement(namespace = "http://www.powerlogic.com.br/2009/AtomPlcExt")
	public String getOkMensagem() {
		return okMensagem;
	}

	public void setOkMensagem(String okMensagem) {
		this.okMensagem = okMensagem;
	}

	@XmlElement(namespace = "http://www.powerlogic.com.br/2009/AtomPlcExt")
	public String getErroMensagem() {
		return erroMensagem;
	}

	public void setErroMensagem(String erroMensagem) {
		this.erroMensagem = erroMensagem;
	}

	@XmlElement(namespace = "http://www.powerlogic.com.br/2009/AtomPlcExt")
	public String getErroLog() {
		return erroLog;
	}

	public void setErroLog(String erroLog) {
		this.erroLog = erroLog;
	}
}
