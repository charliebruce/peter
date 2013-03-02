package ember.editor;

import ember.client.Client;
import ember.engine.GameEngine;

public class Editor {

	public static Client instance;
	
	public static void main(String[] args){
		
		//TODO FIX RUNNING BADLY WITHOUT GL
		
		
		instance = new Client();
		
		//instance.instance = instance;
		
		instance.createFrame(800,600);
		
		instance.runGame();
		
		//Now this thread is free to hook into the game, packet system and so on. 
		//I could also spool or interact with a server...
		//Note I'm NOT a client really, and I might be worth extending Engine...?
		
	}


}
