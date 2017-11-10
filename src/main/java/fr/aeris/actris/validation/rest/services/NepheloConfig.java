package fr.aeris.actris.validation.rest.services;

import java.util.Date;

import fr.aeris.actris.validation.rest.services.data.FtpConfig;

public class NepheloConfig extends SensorConfig{

	private String folder;
	private FtpConfig ftpConfig;
	private CropConfig cropConfig;
	private NephelometerValidationFlowGenerator flowGenerator;
	private String fileTemplate;
	private NepheloGraphConfig graphConfig;
	
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public FtpConfig getFtpConfig() {
		return ftpConfig;
	}
	public void setFtpConfig(FtpConfig ftpConfig) {
		this.ftpConfig = ftpConfig;
	}
	public CropConfig getCropConfig() {
		return cropConfig;
	}
	public void setCropConfig(CropConfig cropConfig) {
		this.cropConfig = cropConfig;
	}
	@Override
	public String generate(Date dayDate, String rootUrl) {
		
		return getFlowGenerator().generate(getUuid(), this, dayDate, rootUrl);
	}
	public String getFileTemplate() {
		return fileTemplate;
	}
	public void setFileTemplate(String fileTemplate) {
		this.fileTemplate = fileTemplate;
	}
	public NephelometerValidationFlowGenerator getFlowGenerator() {
		return flowGenerator;
	}
	public void setFlowGenerator(NephelometerValidationFlowGenerator flowGenerator) {
		this.flowGenerator = flowGenerator;
	}
	public NepheloGraphConfig getGraphConfig() {
		return graphConfig;
	}
	public void setGraphConfig(NepheloGraphConfig graphConfig) {
		this.graphConfig = graphConfig;
	}
	@Override
	public String generatedailydetail(Date dayDate, String rootUrl) {
		
		return getFlowGenerator().generatedailydetail(getUuid(), this, dayDate, rootUrl);
	}
	
	
	
}
