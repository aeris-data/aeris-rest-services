package fr.aeris.actris.validation.rest.services;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.aeris.actris.validation.rest.services.data.ActrisFtpClient;
import fr.aeris.commons.metadata.domain.temporal.DateUtil;
import fr.aeris.commons.metadata.util.UuidUtils;
import fr.sedoo.commons.util.StringUtil;
import fr.sedoo.commons.util.TemplatedStringParser;
import fr.sedoo.commons.util.TemplatedStringParserException;
import junit.framework.Assert;

public class NephelometerValidationFlowGenerator {
	
	private static final String CONTENT = "content";
	private static final String QUICKLOOK = "QUICKLOOK";
	private static final String CATEGORY = "category";
	private static final String MEDIA = "media";
	private static final String DATE = "date";
	private static final String UUID = "uuid";
	private static final String GRANULES = "granules";
	private static final String ID = "id";
	private static final String TOTAL_RESULTS = "totalResults";
	public static int TOP_CROPPING = 100;

	public String generate(String uuid, NepheloConfig config, Date dayDate, String rootUrl) {
		 JSONObject obj = new JSONObject();
	        obj.put(ID, uuid);
	        
	        
	        
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dayDate);
	        int year = cal.get(Calendar.YEAR);
	        int day = cal.get(Calendar.DAY_OF_MONTH);
	        int month = cal.get(Calendar.MONTH)+1; 
	        
	        String yearFolder = config.getFolder()+"/"+year;
	        ActrisFtpClient ftpClient = new ActrisFtpClient(config.getFtpConfig());
	        
	        try {
	        ftpClient.connect();
	        int totalResults = 0;
	        
	        if (ftpClient.directoryExists(yearFolder)) 
	        {
	        	List<FTPFile> listDirectory = ftpClient.listDirectory(yearFolder);
	        	List<FTPFile> matches = new ArrayList<>();
	        	TemplatedStringParser parser = new TemplatedStringParser(config.getFileTemplate());
	        	
	        	for (FTPFile ftpFile : listDirectory) {
				
	        		String aux = ftpFile.getName();
	        		UuidUtils.getUuidFromText(aux+aux);
	        		
	        		try {
	        		parser.parse(aux);
	        		if (parser.matches(aux)) {
	        			
	        			int detectedMonth = -1;
	        			if (StringUtils.isNumeric(StringUtils.trimToEmpty(parser.getValue("MM")))) {
	        				detectedMonth = Integer.parseInt(parser.getValue("MM"));
	        			}
	        			
	        			int detectedDay = -1;
	        			if (StringUtils.isNumeric(StringUtils.trimToEmpty(parser.getValue("dd")))) {
	        				detectedDay = Integer.parseInt(parser.getValue("dd"));
	        			}
	        			if ((detectedMonth == month) && (detectedDay == day)) {
	        				matches.add(ftpFile);
	        			}
	        		}
	        		} catch (TemplatedStringParserException e) {
	        			
	        		}
				}
        		totalResults = matches.size(); 
        		JSONArray granules = new JSONArray();
        		if (totalResults > 0) {
        			//Nepehelometer only produce one file per day
        			FTPFile onlyFile = matches.iterator().next();
        			JSONObject granule = new JSONObject();
        			granule.put(UUID, UuidUtils.getUuidFromText(uuid+" "+onlyFile.getName()));
        			granule.put(DATE, DateUtil.fullFormat(dayDate));
        			
        			JSONObject media = new JSONObject();
        			
        			String rawImageUrl = rootUrl+"/rest/quicklook/download?uuid="+uuid+"&folder="+yearFolder+"&image="+onlyFile.getName();
        			CropConfig cropConfig = config.getCropConfig();
        			String cropUrl = rootUrl+"/rest/imagecrop/png?url="+URLEncoder.encode(rawImageUrl,"UTF-8") +"&top="+cropConfig.getTop()+"&left="+cropConfig.getLeft()+"&right="+cropConfig.getRight()+"&bottom="+cropConfig.getBottom();
        			
        			media.put(CONTENT, cropUrl);
        			media.put(CATEGORY, QUICKLOOK);
        			
        			granule.put(MEDIA, media);
        			
        			granules.add(granule);
        		}
        		obj.put(GRANULES, granules);
	        }
	        obj.put(TOTAL_RESULTS, totalResults );
	        
	        ftpClient.close();
	        
	        }catch (Exception e) {
        	e.printStackTrace();
	        	
	        }
	        
	        
	        String result = obj.toJSONString();
	        System.out.println(result);
	        return result;
	}
	
	
	
	public String generatedailydetail(String uuid, NepheloConfig config, Date dayDate, String rootUrl) {
		 JSONObject obj = new JSONObject();
	        obj.put(ID, uuid);
	        
	        
	        
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dayDate);
	        int year = cal.get(Calendar.YEAR);
	        int day = cal.get(Calendar.DAY_OF_MONTH);
	        int month = cal.get(Calendar.MONTH)+1; 
	        
	        String yearFolder = config.getFolder()+"/"+year;
	        ActrisFtpClient ftpClient = new ActrisFtpClient(config.getFtpConfig());
	        
	        try {
	        ftpClient.connect();
	        int totalResults = 0;
	        
	        if (ftpClient.directoryExists(yearFolder)) 
	        {
	        	List<FTPFile> listDirectory = ftpClient.listDirectory(yearFolder);
	        	List<FTPFile> matches = new ArrayList<>();
	        	TemplatedStringParser parser = new TemplatedStringParser(config.getFileTemplate());
	        	
	        	for (FTPFile ftpFile : listDirectory) {
				
	        		String aux = ftpFile.getName();
	        		UuidUtils.getUuidFromText(aux+aux);
	        		
	        		try {
	        		parser.parse(aux);
	        		if (parser.matches(aux)) {
	        			
	        			int detectedMonth = -1;
	        			if (StringUtils.isNumeric(StringUtils.trimToEmpty(parser.getValue("MM")))) {
	        				detectedMonth = Integer.parseInt(parser.getValue("MM"));
	        			}
	        			
	        			int detectedDay = -1;
	        			if (StringUtils.isNumeric(StringUtils.trimToEmpty(parser.getValue("dd")))) {
	        				detectedDay = Integer.parseInt(parser.getValue("dd"));
	        			}
	        			if ((detectedMonth == month) && (detectedDay == day)) {
	        				matches.add(ftpFile);
	        			}
	        		}
	        		} catch (TemplatedStringParserException e) {
	        			
	        		}
				}
	        	
       		totalResults = matches.size(); 
       		NepheloGraphConfig graphConfig = config.getGraphConfig();
       		
       		JSONObject bounds = new JSONObject();
	        
	        bounds.put("yMin", graphConfig.getYmin());
	        bounds.put("yMax", graphConfig.getYmax());
	        bounds.put("xMin", graphConfig.getTimeXmin());
	        bounds.put("xMax", graphConfig.getTimeXmax());
	        
	        obj.put("bounds", bounds);
	        obj.put("color",graphConfig.getColor());
	        obj.put("legende","");
	        obj.put("scaleXType", graphConfig.getScaleXType());
	        obj.put("scaleYType", graphConfig.getScaleYType());
	        JSONArray arr = new JSONArray();
	        arr.add(10);
	        arr.add(100);
	        arr.add(500);
	        obj.put("logScale", arr);
	        
       		if (totalResults > 0) {
       			//Nepehelometer only produce one file per day
       			FTPFile onlyFile = matches.iterator().next();
       			JSONObject image = new JSONObject();
       			
       			obj.put(UUID, UuidUtils.getUuidFromText(uuid+" "+onlyFile.getName()));

       			
       			JSONObject media = new JSONObject();
       			
       			String rawImageUrl = rootUrl+"/rest/quicklook/download?uuid="+uuid+"&folder="+yearFolder+"&image="+onlyFile.getName();
       			CropConfig cropConfig = config.getCropConfig();
       			String cropUrl = rootUrl+"/rest/imagecrop/png?url="+URLEncoder.encode(rawImageUrl,"UTF-8") +"&top="+cropConfig.getTop()+"&left="+cropConfig.getLeft()+"&right="+cropConfig.getRight()+"&bottom="+cropConfig.getBottom();
       			
       			obj.put("image", cropUrl);

       		}
       		
	        }
	       	        
	        ftpClient.close();
	        
	        }catch (Exception e) {
       	e.printStackTrace();
	        	
	        }
	        
	        
	        String result = obj.toJSONString();
	        System.out.println(result);
	        return result;
	}

}
