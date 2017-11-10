package fr.aeris.actris.validation.rest.services;

import java.util.ArrayList;

public class QualityCodeList extends ArrayList<QualityCode>{
	
	public boolean containsCode(String code) {
		for (QualityCode current : this) {
			if (current.getCode().compareToIgnoreCase(code)== 0) {
				return true;
			}
		}
		
		return false;
	}

}
