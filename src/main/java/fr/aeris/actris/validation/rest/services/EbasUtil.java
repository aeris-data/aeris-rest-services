package fr.aeris.actris.validation.rest.services;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.aeris.commons.metadata.domain.identification.data.dataset.DataProcessingLevel;
import fr.sedoo.commons.util.SeparatorUtil;

public class EbasUtil implements EbasConstants{
	
	public final static String VALID_MEASUREMENT_CODE = "000";
	public final static String MISSING_MEASUREMENT_UNSPECIFIED_REASON_CODE = "999";
	public final static String VALID_MEASUREMENT_LABEL = "Valid measurement";
	public final static String MISSING_MEASUREMENT_UNSPECIFIED_REASON_LABEL = "Missing measurement, unspecified reason";

	public static HashMap<String, String> urls = new HashMap<>();
	
	static {
		urls.put(NEPHELOMETER+SeparatorUtil.AROBAS_SEPARATOR+DataProcessingLevel.L0.toString(), "http://ebas-submit.nilu.no/Submit-Data/Data-Reporting/Templates/Category/Aerosol/Integrating-Nephelometer-Data/level0");
	}
	
	public static QualityCodeList getCodeListByInstrumentAndLevel(String instrument, String level) {
		
		try {
			String key = instrument+SeparatorUtil.AROBAS_SEPARATOR+level;
			String url = urls.get(key);
			if (url == null) {
				return getDefaultCodeList();
			} else {
				return parseUrl(url);
			}
		}
		catch (Exception e)
		{
			return getDefaultCodeList();
		}
		
		
	}
	
	private static QualityCodeList parseUrl(String url) {
		
		QualityCodeList result = new QualityCodeList();

		try {
			Document doc        	= Jsoup.connect(url).get();			
			Elements tableFlag 		= doc.select("div.flag-goup-table table");
			Elements rowValue 		= tableFlag.select("tr:nth-of-type(n+2)");
			Elements flagValue 		  = rowValue.select("td:nth-of-type(1)");
			Elements validityValue 	  = rowValue.select("td:nth-of-type(2)");
			Elements descriptionValue = rowValue.select("td:nth-of-type(3)");
			
			for (Element spantext : flagValue) {
				  
				  int index = flagValue.indexOf(spantext);
			      QualityCode codeLabel = new QualityCode();
			      codeLabel.setCode(spantext.text());
			      codeLabel.setLabel(descriptionValue.get(index).text());
			      result.add(codeLabel);
			      
			      System.out.println("Flag value : " + spantext.text() +", Validity Value : " + validityValue.get(index).text()+ ", Description Value : " + descriptionValue.get(index).text());
			     
			      
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}

	public static QualityCodeList getDefaultCodeList() { 
	QualityCodeList defaultCodeList = new QualityCodeList();
	defaultCodeList.add(new QualityCode(VALID_MEASUREMENT_CODE, VALID_MEASUREMENT_LABEL));
	defaultCodeList.add(new QualityCode(MISSING_MEASUREMENT_UNSPECIFIED_REASON_CODE, MISSING_MEASUREMENT_UNSPECIFIED_REASON_LABEL));
	return defaultCodeList;
	}

}
