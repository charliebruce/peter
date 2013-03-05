package magma.client.input;

import java.util.HashMap;

import magma.client.console.Console;
import magma.logger.Log;

public class KeyBinding {

	static HashMap<String, Integer> k = new HashMap<String, Integer>();
	public static final int HIGHESTKEY=600;//524 = winkey?
	
	static String[] bindings = new String[HIGHESTKEY];
	
	public static void init(){
		k.put("=", 61);
		k.put("-", 45);
		k.put("`", 192);//Tilde
		k.put(";", 59);
		k.put("control", 17);
		k.put("shift", 16);
		k.put("tab", 9);
		k.put("space", 32);
		k.put("esc", 27);//escape
		k.put("enter", 10);
		k.put("backspace", 8);
		k.put("home", 36);
		k.put("pgup", 33);
		k.put("pgdown", 34);
		k.put("end", 35);
		k.put("del", 127);
		k.put("ins", 155);
		k.put("pausebreak", 19);

		k.put("up", 38);
		k.put("down",40);
		k.put("left",37);
		k.put("right",39);
		
		
		
		//Continues beyond F12?
		for(int i = 1; i<12;i++){
			k.put("f"+i, 111+i);
		}
		
		for(int i=0;i<10;i++){
			k.put(""+i+"", 48+i);
		}
		/*
		k.put("9", 57);
		k.put("8", 56);
		k.put("7", 55);
		k.put("6", 54);
		k.put("5", 53);
		k.put("4", 52);
		k.put("3", 51);
		k.put("2", 50);
		k.put("1", 49);
		k.put("0", 48);
		 */
		
		
		k.put("a", 65);
		k.put("b", 66);	
		k.put("c", 67);
		k.put("d", 68);
		k.put("e", 69);
		k.put("f", 70);
		k.put("g", 71);
		k.put("h", 72);
		k.put("i", 73);
		k.put("j", 74);
		k.put("k", 75);
		k.put("l", 76);	
		k.put("m", 77);
		k.put("n", 78);
		k.put("o", 79);	
		k.put("p", 80);
		k.put("q", 81);
		k.put("r", 82);
		k.put("s", 83);
		k.put("t", 84);
		k.put("u", 85);
		k.put("v", 86);
		k.put("w", 87);
		k.put("x", 88);
		k.put("y", 89);
		k.put("z", 90);


	}

	
	
	public static void bind(String key, String binding){
		//Bind a specific key to an event
		//Command "bind shift +sprint"
		//Command "bind control +crouch" for repeated actions
		//Command "bind w +forward"
		//Command "bind r reload"
		if(!k.containsKey(key)){Log.info("Unknown key "+key);return;}
		int num = k.get(key);
		bindings[num] = binding;
		
	}
	

	
	/*
	 * Is the given key in the binding array?
	 */
	public static boolean isBound(int key){
		if(bindings[key]==""){return true;}
		return false;
	}



	public static void unbind(String string) {
		if(string.equals("all")){
			bindings = new String[HIGHESTKEY];
		}
		else {
			int num = k.get(string);
			bindings[num] = "";
			
		}
	}
	public static void keyPressed(int keyCode) {
		//TODO boolean array of KeyState instead? That's the Mojang way
		
		
		//TODO not if console open!
		String s = bindings[keyCode];
		if(s==null) {System.out.println("kp"+keyCode);return;}
		if(s.startsWith("+")){
			//State change key - pressed 
			if(s.equals("+dirfwd")) {KeyState.dirfwd=true; return;}			
			if(s.equals("+dirback")) {KeyState.dirback=true; return;}			
			if(s.equals("+dirleft")) {KeyState.dirleft=true; return;}
			if(s.equals("+dirright")) {KeyState.dirright=true; return;}	
			if(s.equals("+dirup")) {KeyState.dirup=true; return;}
			if(s.equals("+dirdown")) {KeyState.dirdown=true; return;}
		}
		else {
			Console.executeCommand(s);
		}
	}
	public static void keyReleased(int keyCode) {
		String s = bindings[keyCode];
		if(s==null) return;
		if(s.startsWith("+")){
			//State change key - release
			if(s.equals("+dirfwd")) {KeyState.dirfwd=false; return;}			
			if(s.equals("+dirback")) {KeyState.dirback=false; return;}			
			if(s.equals("+dirleft")) {KeyState.dirleft=false; return;}
			if(s.equals("+dirright")) {KeyState.dirright=false; return;}	
			if(s.equals("+dirup")) {KeyState.dirup=false; return;}
			if(s.equals("+dirdown")) {KeyState.dirdown=false; return;}
		}
		else {
		}
	}

	
}
