package fr.aeris.actris.validation.rest.services;

public class QualityCode {
	
	private String code;
	private String label;
	
	public String getCode() {
		return code;
	}
	
	public QualityCode() {
	}

	public QualityCode(String code, String label) {
		super();
		this.code = code;
		this.label = label;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

}
