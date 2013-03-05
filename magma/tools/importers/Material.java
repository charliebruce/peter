package importers;

import java.nio.FloatBuffer;

import magma.logger.Log;

public class Material {

	private String name;
	private vec3 Ka, Kd, Ks, Ke, Tf;//Amb, Dif, Spec, Emiss, Transmission Filter
	private float Ns;//Shininess - specular coefficient
	private float d;//Dissolve coefficient - transparency
	private int illum;//Illumination model
	
	/*
	 * illum illum_X
	 * Illumination Properties (claimed) 
	0	 Color on and Ambient off 
	1	 Color on and Ambient on 
	2	 Highlight on 
	3	 Reflection on and Ray trace on 
	4	 Transparency: Glass on 
	Reflection: Ray trace on 
	5	 Reflection: Fresnel on and Ray trace on 
	6	 Transparency: Refraction on 
	Reflection: Fresnel off and Ray trace on 
	7	 Transparency: Refraction on 
	Reflection: Fresnel on and Ray trace on 
	8	 Reflection on and Ray trace off 
	9	 Transparency: Glass on 
	Reflection: Ray trace off 
	10	 Casts shadows onto invisible surfaces 


	 */
	public Material(String matname) {
		name=matname;
	}

	public void setKa(vec3 from) {
		Ka=from;
	}

	public void setKe(vec3 from) {
		Ke=from;
	}
	public void setKd(vec3 from) {
		Kd=from;
	}
	public void setKs(vec3 from) {
		Ks=from;
	}

	public void setNs(float in) {
		Ns = in;
	}

	public void setTf(vec3 from) {
		Tf=from;
	}


	private String mapka="null.png";
	private String mapkd="null.png";
	private String mapd="null.png";
	private String bump="NULLVALUE";
	private String mapbump="NULLVALUE";//Null bump????
	public void useTexture(String type, String name) {
		if(type.equals("map_Ka")){
			mapka=name;
			//System.out.println("map_Ka "+name);
		}
		if(type.equals("map_Kd")){
			mapkd=name;
			//System.out.println("map_Kd "+name);
		}
		if(type.equals("map_bump")){
			mapbump=name;
			//System.out.println("map_bump "+name);
		}
		if(type.equals("bump")){
			bump=name;
			//System.out.println("bump "+name);
		}
		if(type.equals("map_d")){
			mapd=name;
			//System.out.println("map_d "+name);
		}
	}
	

	public void useIllum(int parseInt) {
		illum = parseInt;
	}

	public void setDissolve(float parseFloat) {
		d=parseFloat;
	}

	public void setTr(float parseFloat) {
		//Transparency
		if(parseFloat!=0.0)
		Log.warn("This transparency not supported = " + parseFloat);
	}

	public void setNi(float parseFloat) {
		//Refractive Index
		Log.warn("Refractive index changes not supported = " + parseFloat);
	}

	public int submodels(){
		//Return the number of sub-models. Split per usemtl so shaders can be switched.
		return 1;
	}
	
	public FloatBuffer getVertices(int submodel){
		return null;
	}

	public String texPath() {
		// TODO Auto-generated method stub
		return "data/sponza/"+mapka;
	}

	public boolean useNormalMap() {
		return !(mapbump.equals("NULLVALUE"));
	}

	public String getNormalPath() {
		return "data/sponza/"+mapbump;//bump
	}

}
