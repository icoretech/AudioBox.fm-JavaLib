package fm;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import fm.audiobox.api.AudioBoxClient;
import fm.audiobox.api.exceptions.LoginException;
import fm.audiobox.api.exceptions.ServiceException;
import fm.audiobox.api.interfaces.ThreadListener;
import fm.audiobox.api.models.User;
import fm.audiobox.sync.MD5Converter;
import fm.audiobox.sync.Scan;
import fm.audiobox.sync.Upload;
import fm.audiobox.util.ThreadItem;
import fm.audiobox.util.ThreadManager;

public class Startup {

	
	public static void main(String[] args){
		
		//Upload upload = new Upload( new File("D:\\Downloads\\asd\\lkXIBgjyzd9fFBxN2R9fQA.mp3") );
		
//		final ThreadManager tmScan = new ThreadManager( 4 );
//		final ThreadManager tmMd5 = new ThreadManager(4);
//		final ThreadManager tmupload = new ThreadManager(4);
//		
		AudioBoxClient abc = new AudioBoxClient();
//		
		try {
			User user = abc.login( "fat@fatshotty.net", "f4tb0x");
//			
			final List<String> hashes = Arrays.asList( user.getUploadedTracks() );
//			
			Scan scan = new Scan( new File( "/home/shotty/Dropbox/Public/" ) , true );
//			scan.setThreadListener( new ThreadListener() {
//				
//				@Override
//				public void onStart(ThreadItem result) {
//					System.out.println( "Scan start " + System.currentTimeMillis() );
//				}
//				
//				@Override
//				public void onProgress(ThreadItem result, long total, long completed, long remaining, Object item) {
//					MD5Converter md5 = new MD5Converter( (File) item );
//					md5.setThreadListener( new ThreadListener() {
//						
//						@Override
//						public void onStart(ThreadItem result) {
//							System.out.println( "MD5 scan " + System.currentTimeMillis() );
//						}
//						
//						@Override
//						public void onProgress(ThreadItem result, long total, long completed, long remaining, Object item) {
//						}
//						
//						@Override
//						public void onComplete(ThreadItem result, Object item) {
//							System.out.println( "MD5 end " + System.currentTimeMillis() + " " + item );
//							if ( ! hashes.contains( item )  ){
//								System.out.println( "\tItem not exists");
//							}
//						}
//					});
//					
//					tmMd5.newThread( md5 );
//				}
//				
//				@Override
//				public void onComplete(ThreadItem result, Object item) {
//					System.out.println( "Scan end " + System.currentTimeMillis() + " " + ((List<File>)item).size() + " file found" );
//				}
//			});
//			
//			
//			tmScan.newThread( scan );
			
			List<File> files = scan.scan();
			for( File file : files ){
				MD5Converter md5 = new MD5Converter(file);
				System.out.println( "MD5: " + file.getName() + " " + md5.digest() );
			}
			
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}