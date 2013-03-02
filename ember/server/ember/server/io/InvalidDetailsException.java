package ember.server.io;

public class InvalidDetailsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8905726711759460913L;

	public String toString(){
		return "Details are invalid: wrong uname or pass";
	}
	
	
}
