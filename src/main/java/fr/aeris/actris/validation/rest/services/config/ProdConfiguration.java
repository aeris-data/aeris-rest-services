package fr.aeris.actris.validation.rest.services.config;

import org.glassfish.hk2.external.org.objectweb.asm.commons.GeneratorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.aeris.actris.validation.rest.dao.AdminDao;
import fr.aeris.actris.validation.rest.dao.AdminDaoFakeImpl;
import fr.aeris.actris.validation.rest.services.ActrisValidationConfig;
import fr.aeris.actris.validation.rest.services.CropConfig;
import fr.aeris.actris.validation.rest.services.NepheloConfig;
import fr.aeris.actris.validation.rest.services.NepheloGraphConfig;
import fr.aeris.actris.validation.rest.services.NephelometerValidationFlowGenerator;
import fr.aeris.actris.validation.rest.services.SensorConfigList;
import fr.aeris.actris.validation.rest.services.data.FtpConfig;

@Configuration
public class ProdConfiguration implements ActrisConstants{
	
	@Bean
	public ActrisValidationConfig getActrisValidationConfig() {
		
		ActrisValidationConfig result = new ActrisValidationConfig();
		result.setWorkingDirectory("/tmp/actris");
		result.setMetadataService("https://sedoo.aeris-data.fr/catalogue/rest/metadatarecette/");
		return result;
	}
	
	@Bean
	public FtpConfig getIcareFtpConfig() {
		FtpConfig result = new FtpConfig();
		result.setAddress("ftp.icare.univ-lille1.fr");
		result.setLogin("francoisandre");
		result.setPassword("nrvgkcTK7");
		result.setRootFolder("/");
		return result;
		
	}
	
	@Bean
	public CropConfig getIcareNepheloCropConfig() {
		CropConfig result = new CropConfig();
		result.setBottom(99);
		result.setTop(100);
		result.setLeft(100);
		result.setRight(309);
		
		return result;
	}
	
	@Bean
	public AdminDao getAdminDao() {
		return new AdminDaoFakeImpl();
	}
	
	@Bean
	public SensorConfigList getSensorConfigList() {
		SensorConfigList result = new SensorConfigList();
		int[] logScale = {10, 100, 500};
		NepheloConfig nepheloPicDuMidi = new NepheloConfig();
		NepheloGraphConfig nepheloPicDuMidiGraph = new NepheloGraphConfig();
		nepheloPicDuMidi.setUuid(NEPEHELO_PIC_DU_MIDI_UUID);
		nepheloPicDuMidi.setFtpConfig(getIcareFtpConfig());
		nepheloPicDuMidi.setCropConfig(getIcareNepheloCropConfig());
		nepheloPicDuMidi.setFolder("/GROUND-BASED/P2OA_Pic-Du-Midi/NEPHE/NEPHE_RAW");
		nepheloPicDuMidi.setFileTemplate("${model}_${model2}_${yyyy}${MM}${dd}.scattering_coefficient_520nm.png");
		nepheloPicDuMidi.setFlowGenerator(new NephelometerValidationFlowGenerator());
		
		nepheloPicDuMidiGraph.setColor("red");
		nepheloPicDuMidiGraph.setScaleXType("time");
		nepheloPicDuMidiGraph.setScaleYType("log");
		nepheloPicDuMidiGraph.setLogScaleUnit(logScale);
		nepheloPicDuMidiGraph.setTimeXmin("0:00");
		nepheloPicDuMidiGraph.setTimeXmax("24:00");
		nepheloPicDuMidiGraph.setYmin(1);
		nepheloPicDuMidiGraph.setYmax(500);
				
		nepheloPicDuMidi.setGraphConfig(nepheloPicDuMidiGraph);
		result.add(nepheloPicDuMidi);
		return result;
	}

}
