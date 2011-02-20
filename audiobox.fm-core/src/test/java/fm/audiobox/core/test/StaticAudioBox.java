package fm.audiobox.core.test;

import fm.audiobox.AudioBox;


public class StaticAudioBox extends AudioBox{

	private static StaticAudioBox instance = null; 
	
	public StaticAudioBox(){
		instance = this;
	}
	
	public static Connector getConnector(){
		return instance.getMainConnector();
	}
	
	
}
