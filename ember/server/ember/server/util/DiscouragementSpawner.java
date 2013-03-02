package ember.server.util;

public class DiscouragementSpawner {

	private static String[] quotes = new String[]{"Tell me, Dr. Freeman, if you can. You have destroyed so much. What is it, exactly, that you have created? Can you name even one thing? I thought not. "};
	public static String get(){
		return quotes[(int) (Math.random()*quotes.length)];//TODO test this
	}
}
