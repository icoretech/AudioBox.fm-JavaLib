package fm.audiobox.core;

import fm.audiobox.core.models.AudioBoxClient;

public class StaticAudioBox extends AudioBoxClient{

	private static StaticAudioBox instance = null; 
	
	public StaticAudioBox(){
		instance = this;
	}
	
	public static AudioBoxConnector getConnector(){
		return instance.getMainConnector();
	}
	
	
}
