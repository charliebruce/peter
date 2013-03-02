package ember.language;
/**
 * The given text in localised format.
 */
public class InternationalString {
	
	
	String english = null;
	String second = null;
	
	public InternationalString(String en){
		english = en;
	}
	InternationalString(String en, String secondLang){
		english = en;
		second = secondLang;
	}
	
	String getEnglish() {
		return english;
	}
	
	String get(String language){
	
		if (language=="en"||language=="eng"||language=="english"){
			return english;
		}
		if (language=="other"){
			return second;
		}
		
		return english;
	}
}
