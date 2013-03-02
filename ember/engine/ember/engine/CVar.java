package ember.engine;

import java.util.Map;

public class CVar {

	public String string;
	public float value;

	public static Map<String, CVar> cvars;
	
	
	public static CVar Get(String cvarname) {
		//Get the specified CVar.
		//TODO error handling.
		return cvars.get(cvarname);
	}

	
	public static void Set(String cvarname, String newvalue) {
		// TODO Auto-generated method stub
		
	}

}
