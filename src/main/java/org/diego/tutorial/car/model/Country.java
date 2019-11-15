package org.diego.tutorial.car.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity that represents a persistence domain object. This entity is persisted
 * in database through the creation of a table "country", where every property in
 * that table is a field in this class.
 *
 */
@XmlRootElement
@Entity
@Table(name = "country")
public class Country implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	@NotNull(message = "The name of the country cannot be null")
	private String countryName;

	@Column(nullable = false)
	@NotNull(message = "The abbreviation of the country cannot be null")
	private String countryAbbreviation;

	@Transient // Links are not stored in the database
	private List<Link> links = new ArrayList<Link>();

	public Country() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryAbbreviation() {
		return countryAbbreviation;
	}

	public void setCountryAbbreviation(String countryAbbreviation) {
		this.countryAbbreviation = countryAbbreviation;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(String url, String rel) {
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		this.links.add(link);
	}

	public void removeLinks() {
		links.clear();
	}
	
	@Override
	public String toString() {
		return "Country [countryName: " + countryName + ", countryAbbreviation: " + countryAbbreviation + ", "
				+ "links: " + links + "]";
	}

}
