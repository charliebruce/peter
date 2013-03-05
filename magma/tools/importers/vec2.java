package importers;

public class vec2{

	float x=0,y=0;
	public vec2(float i, float j) {
		x=i;
		y=j;
		
	}

	static vec2 fromString(String s){
		String[] tokens = s.split(" "); 
		return new vec2(Float.parseFloat(tokens[0]),Float.parseFloat(tokens[1]));
	}
}
