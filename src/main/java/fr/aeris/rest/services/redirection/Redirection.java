package fr.aeris.rest.services.redirection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import fr.sedoo.commons.util.StringUtil;

@Entity
@Table(name = "REDIRECTION")
public class Redirection  {

	@NotBlank
	@Column(name = "AUTHOR")
	private String author;
	
	@NotBlank
	@Column(name = "AERIS_SUFFIX")
	private String aerisSuffix;

	@NotBlank
	@Column(name = "URL")
	private String url;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAerisSuffix() {
		return aerisSuffix;
	}

	public void setAerisSuffix(String aerisSuffix) {
		this.aerisSuffix = aerisSuffix;
	}

	public String getUrl() {
		if (StringUtil.isEmpty(url)) {
			return "";
		} else {
			if (url.startsWith("http") == false) {
				return "http://" + StringUtil.trimToEmpty(url);
			} else {
				return StringUtil.trimToEmpty(url);
			}
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
