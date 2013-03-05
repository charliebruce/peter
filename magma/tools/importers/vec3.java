package importers;

public class vec3 {

	float x=0,y=0,z=0;
	public vec3(float i, float j, float k) {
		x=i;
		y=j;
		z=k;
	}

	static vec3 fromString(String s){
		String[] tokens = s.split(" "); 
		return new vec3(Float.parseFloat(tokens[0]),Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2]));
	}
}
