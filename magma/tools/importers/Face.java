package importers;

import java.util.ArrayList;

public class Face {

	private String material;
	private int[] vertices = new int[3];
	private int[] vts = new int[3];
	private int[] vns = new int[3];
	
	private int shadegroup;
	
	private Face(int[] vert, int[] vt, int[] vn, String mat, int s){
		vertices = vert;
		vts = vt;
		vns = vn;
		material = mat;
		shadegroup = s;
	}
	public String getMaterialName(){return material;}
	
	public static Face fromString(String face, String currentMaterial, int shadegroup) {
		// TODO Auto-generated method stub
		String[] split = face.split(" ");
		if(split[0].contains("/")){
			
			String[] s0 = split[0].split("/");
			String[] s1 = split[1].split("/");
			String[] s2 = split[2].split("/");
			
			int[] vertices = new int[]{Integer.parseInt(s0[0]),Integer.parseInt(s1[0]),Integer.parseInt(s2[0])};
			int[] vts = new int[]{Integer.parseInt(s0[1]),Integer.parseInt(s1[1]),Integer.parseInt(s2[1])};
			
			if(s0.length==3){
				//Form v/vt/vn
				int[] vns = new int[]{Integer.parseInt(s0[2]),Integer.parseInt(s1[2]),Integer.parseInt(s2[2])};
				return new Face(vertices, vts, vns, currentMaterial, shadegroup);
			}
			else {
				//Form v/vt
				return new Face(vertices, vts, null, currentMaterial, shadegroup);
			}
			
		}
		else {
			int[] vertices = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
			return new Face(vertices, null, null, currentMaterial, shadegroup);
		}
		
	}

	public int getvertex(int n) {
		return vertices[n-1];
	}
	public int getvt(int n){
		return vts[n-1];
	}
	public int getvn(int n){
		return vns[n-1];
	}
	public float[] toVbo(ArrayList<vec3> thevertices, ArrayList<vec3>norms, ArrayList<vec2> texcoords) {
		float[] result = new float[24];
		vec3 a = thevertices.get(vertices[0]);
		vec3 b = thevertices.get(vertices[1]);
		vec3 c = thevertices.get(vertices[2]);
		
		vec2 ta = texcoords.get(vts[0]-1);
		vec2 tb = texcoords.get(vts[1]-1);//!!!!!!!!!!!!!!!! TODO BUG FIXME WHY DOES -1 WORK AND DOES IT?ss
		vec2 tc = texcoords.get(vts[2]-1);
		
		vec3 na = norms.get(vns[0]);//-!?
		vec3 nb = norms.get(vns[1]);//-!?
		vec3 nc = norms.get(vns[2]);//-!?
		
		result[0]=a.x;
		result[1]=a.y;
		result[2]=a.z;
		result[3]=na.x;
		result[4]=na.y;
		result[5]=na.z;
		result[6]=ta.x;
		result[7]=ta.y;
	
		
		result[8]=b.x;
		result[9]=b.y;
		result[10]=b.z;
		result[11]=nb.x;
		result[12]=nb.y;
		result[13]=nb.z;
		result[14]=tb.x;
		result[15]=tb.y;
		
		result[16]=c.x;
		result[17]=c.y;
		result[18]=c.z;
		result[19]=nc.x;
		result[20]=nc.y;
		result[21]=nc.z;
		//if(tc==null){System.out.println("Oh shit. I'm a face but I have null TC");}
		result[22]=tc.x;
		result[23]=tc.y;
		//System.out.println("NA IS "+na.x+","+na.y+","+na.z);
		return result;
	}

}
