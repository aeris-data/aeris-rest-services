package fr.aeris.rest.services.uuid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UuidUtils {

	public static String getUuidFromText(String value) {
        String aux = ""+value+"-";
        String repeated = new String(new char[10]).replace("\0", aux);
        MessageDigest md = null;
        try {
                md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        md.update(repeated.getBytes());
        byte[] mdbytes = md.digest();
StringBuffer sb = new StringBuffer();
for (int i = 0; i < mdbytes.length; i++) {
  sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
}
        String longUuid = sb.toString();
        String shortUuid = longUuid.substring(0, 32);
        String result = shortUuid.substring(0, 8) + "-" + shortUuid.substring(8, 12)+ "-" + shortUuid.substring(12, 16)+ "-" + shortUuid.substring(16, 20)+"-"+ shortUuid.substring(20, shortUuid.length());;
        
        
        return result;
}
	
	
}
