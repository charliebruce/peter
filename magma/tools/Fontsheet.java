
public class Fontsheet {

	public static void main(String[] args){
		String test = "abcdefghijklmnopqrstuvwxyz";
		String caps = test.toUpperCase();
		
		String nums = "1234567890";
		
		for ( int i = 0; i < nums.length(); i++ ) {
			char c = nums.charAt( i );
			int j = (int) c;
			System.out.println(j);
		}

	}
}
