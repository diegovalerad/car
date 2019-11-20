package org.diego.tutorial.car.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that represents additional information about a {@link Car} object. Each
 * car will have multiple links, indicating the URL of the entity or important
 * fields so that the client access them.
 *
 */
@XmlRootElement
public class Link {
	private String link;
	private String rel;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	@Override
	public String toString() {
		return "Link [link: " + link + ", rel: " + rel + "]";
	}

}
