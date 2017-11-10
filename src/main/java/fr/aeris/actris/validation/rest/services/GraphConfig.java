package fr.aeris.actris.validation.rest.services;

import java.util.Date;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public abstract class GraphConfig {
	
	private String image;
	private String graphType;
	private String legend;
	private String color;
	private String scaleXType;
	private String scaleYType;
	private int[] logScaleUnit;
	private int Xmin;
	private int Xmax;
	private int Ymin;
	private int Ymax;
	
		
	
	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getGraphType() {
		return graphType;
	}


	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}


	public String getLegend() {
		return legend;
	}


	public void setLegend(String legend) {
		this.legend = legend;
	}


	public String getScaleXType() {
		return scaleXType;
	}


	public void setScaleXType(String scaleXType) {
		this.scaleXType = scaleXType;
	}


	public String getScaleYType() {
		return scaleYType;
	}


	public void setScaleYType(String scaleYType) {
		this.scaleYType = scaleYType;
	}

	public int[] getLogScaleUnit() {
		return logScaleUnit;
	}


	public void setLogScaleUnit(int[] logScaleUnit) {
		this.logScaleUnit = logScaleUnit;
	}


	public int getXmin() {
		return Xmin;
	}


	public void setXmin(int xmin) {
		Xmin = xmin;
	}


	public int getXmax() {
		return Xmax;
	}


	public void setXmax(int xmax) {
		Xmax = xmax;
	}


	public int getYmin() {
		return Ymin;
	}


	public void setYmin(int ymin) {
		Ymin = ymin;
	}


	public int getYmax() {
		return Ymax;
	}


	public void setYmax(int ymax) {
		Ymax = ymax;
	}


	public abstract String generate(Date dayDate, String rootUrl); 
	
	

	 

}
