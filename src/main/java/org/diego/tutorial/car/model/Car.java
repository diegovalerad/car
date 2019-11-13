package org.diego.tutorial.car.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.ws.rs.DefaultValue;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.diego.tutorial.car.validations.AddAndUpdateChecks;

/**
 * Entity that represents a persistence domain object. This entity is persisted 
 * in database through the creation of a table "car", where every property in
 * that table is a field in this class.
 *
 */
@XmlRootElement
@Entity
@Table(name = "car")
public class Car implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4239800821516578196L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull(message = "Brand cannot be null", groups = AddAndUpdateChecks.class)
	@ManyToOne(optional = false)
	private Brand brand;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "Registration date cannot be null")
	private Date registration;
	
	@Column(nullable = false)
	@NotNull(message = "Country cannot be null", groups = AddAndUpdateChecks.class)
	private String country;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "Creation date cannot be null")
	private Date createdAt;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "Updated date cannot be null")
	private Date lastUpdated;

	@Transient // Links are not stored in database
	private List<Link> links = new ArrayList<Link>();

	@DefaultValue(value = "false")
	private boolean softRemoved;

	public Car() {
	}

	public Car(long id, Brand brand, Date registration, String country, Date createdAt, Date lastUpdated) {
		this.id = id;
		this.brand = brand;
		this.registration = registration;
		this.country = country;
		this.createdAt = createdAt;
		this.lastUpdated = lastUpdated;
		this.softRemoved = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
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

	public boolean isSoftRemoved() {
		return softRemoved;
	}

	public void setSoftRemoved(boolean softRemoved) {
		this.softRemoved = softRemoved;
	}

	@Override
	public String toString() {
		return "Car [id: " + id + ", brand: " + brand + ", country: " + country + ", registration: " + registration
				+ ", createdAt: " + createdAt + ", lastUpdated: " + lastUpdated + ", links: " + links
				+ ", softRemoved: " + softRemoved + "]";
	}
}
