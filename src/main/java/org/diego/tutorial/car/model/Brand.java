package org.diego.tutorial.car.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity that represents a persistence domain object. This entity is persisted
 * in database through the creation of a table "brand", where every property in
 * that table is a field in this class.
 *
 */
@XmlRootElement
@Entity
@Table(name = "brand")
public class Brand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3100671061051101142L;

	@Id
	@Column(nullable = false, updatable = false)
	@NotNull(message = "brand cannot be null")
	private String brand;
	
	@Column(nullable = false)
	@NotNull(message = "company cannot be null")
	private String company;
	
	@Transient // Links are not stored in the database
	private List<Link> links = new ArrayList<Link>();
	
	public Brand() {
		
	}
	
	public Brand(String brand, String company) {
		this.brand = brand;
		this.company = company;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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
		return "Brand [brand: " + brand + ", company: " + company + ", links: " + links + "]";
	}
}
