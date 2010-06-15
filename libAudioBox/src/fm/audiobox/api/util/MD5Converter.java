package fm.audiobox.api.util;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Converter {

	
	public static String digest(File file){
		
		try {
			MessageDigest message = MessageDigest.getInstance( "MD5");
			
			
			
		} catch( NoSuchAlgorithmException e ){
			
		}
		
		return null;
	}
	
	
}
