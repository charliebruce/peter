package importers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Import an OBJ file and make it into a more useful form. 
 * OBJ and MTL  files parsed
 * Return vertices (optionally normals) and texcoords
 * as well as materials
 * 
 * Convert into a renderable form
 * @author Charlie
 *
 */
public class ObjConvert {

	static int MAX_VERTICES=2^16;
	
	public static void main(String[] args){
		ObjConvert.load(null);
	}
	public static Model load(File f){
		//parseObjFile("data/sponza/sponza.obj");
		parseObjFile("data/sponza/sponza.obj");
		return new Model(vertices, faces, normals, vts, materials );
	}
	
	static void parseObjFile(String fname){
		//Iterate through f's lines and run parseline.
		//TODO same material named in different file fucks shit up. Ah well.
		vertices.ensureCapacity(MAX_VERTICES);
		vertices.add(null);//0th one is useless.
		faces.add(null);
		//vts.ensureCapacity(MAX_VERTICES);
		vts.add(null);//0th one is useless.
		normals.add(null);
		
		currentVertex=1;
		BufferedReader bufferedReader = null;
        
        try {
            
            //Construct the BufferedReader object
            bufferedReader = new BufferedReader(new FileReader(fname));
            String line = null;            
            
            while ((line = bufferedReader.readLine()) != null) {
                parseObjLine(line);
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedReader
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Info: "+(vertices.size()-1)+ " vertices, "+(vts.size()-1) + " vts, "+(faces.size()-1) + " faces, "+(normals.size()-1)+" normals, currface is "+currentFace+", "+ materials.size() + " materials");
	}
	
	int numvertices;
	static int currentVertex;
	static int currentvt;
	static int currentFace;
	static int shadegroup = -1;
	static String currentMaterial;
	static String currentGroup;

	static ArrayList<vec3> vertices = new ArrayList<vec3>();
	static ArrayList<vec3> normals = new ArrayList<vec3>();
	static ArrayList<vec2> vts = new ArrayList<vec2>();
	static ArrayList<Face> faces = new ArrayList<Face>();
	
	static void parseObjLine(String line){
		line = line.replaceAll("\t", "");
		line = line.replaceAll("\\s+", " ");//Regex removes multi-spaces, new lines
			
		//Material File (external)
		if (line.startsWith("mtllib ")){
			parseMtlFile("data/sponza/"+line.replace("mtllib ",""));
			return;
		}
	
		if (line.startsWith("v ")){
			String vertex = line.replace("v ", "");
			//System.out.println(vertices.size());
			vertices.add(currentVertex, vec3.fromString(vertex));
			currentVertex++;
			return;
		}
		if (line.startsWith("vt ")){
			String vertex = line.replace("vt ", "");
			//System.out.println(vertices.size());
			vts.add(currentvt, vec2.fromString(vertex));
			currentvt++;
			return;
		}
		if (line.startsWith("f ")){
			String face = line.replace("f ","");
			String[ ]split = face.split(" ");
			if(split.length==4){
				//4-pointed face. Wound clockwise. Select 1,2,3 and 1,3,4
				faces.add(Face.fromString(split[0]+" "+split[1]+ " "+ split[2], currentMaterial, shadegroup));
				currentFace++;
				faces.add(Face.fromString(split[0]+" "+split[2]+ " "+split[3], currentMaterial, shadegroup));
			} else {
			faces.add(Face.fromString(face, currentMaterial, shadegroup));
			}
			currentFace++;
			return;
		}
		if (line.startsWith("g ")){
			String grp = line.replace("g ","");
			currentGroup = grp;
			return;
		}
		if (line.startsWith("s ")){
			String s = line.replace("s ","");
			if (s.equals("off")){
				shadegroup=-1;
			} 
			else{
				shadegroup = Integer.parseInt(s);
			}
			return;
		}
		if (line.startsWith("usemtl ")){
			String s = line.replace("usemtl ","");
			currentMaterial = s;
			return;
		}

		//Ignore comments
		if(line.startsWith("#")) return;
		if(line.startsWith("vn")){
			String vertex = line.replace("vn ", "");
			normals.add(vec3.fromString(vertex));
			return;
		}
		if(line.equals(" ") || line.equals("")) return;
		
		System.out.println("OBJ line unhandled "+line);
	}
	
	
	
	
	
	static void parseMtlFile(String fname){
		//Iterate through f's lines and run parseline.
		//TODO same material named in different file fucks shit up. Ah well.
		
		BufferedReader bufferedReader = null;
        
        try {
            
            //Construct the BufferedReader object
            bufferedReader = new BufferedReader(new FileReader(fname));
            String line = null;            
            
            while ((line = bufferedReader.readLine()) != null) {
                parseMtlLine(line);
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedReader
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
	}
	
	static Map<String,Material> materials = new HashMap<String,Material>();
	static String currentmtl;
	
	static void parseMtlLine(String line){
		//Remove tabs and excess spaces
		line = line.replaceAll("\t", "");
		line = line.replaceAll("\\s+", " ");//Regex removes multi-spaces, new lines
		
		//New Material
		if (line.startsWith("newmtl ")){
			//Name
			String matname = line.replace("newmtl ", "");
			if(materials.containsKey(matname)){System.err.println("Material already exists.");}
			else{
				//System.out.println("New material: "+matname);
				materials.put(matname, new Material(matname));
				currentmtl=matname;
			}
			return;
		}
		
		Material mat = materials.get(currentmtl);
		
		//Colours
		
		if (line.startsWith("Ka ")){
			//Ambient
			String vec = line.replace("Ka ", "");
			mat.setKa(vec3.fromString(vec));
			return;
		}
		if (line.startsWith("Kd ")){
			//Diffuse
			String vec = line.replace("Kd ", "");
			mat.setKd(vec3.fromString(vec));
			return;
		}
		if (line.startsWith("Ks ")){
			//Specular
			String vec = line.replace("Ks ", "");
			mat.setKs(vec3.fromString(vec));
			return;
		}
		if (line.startsWith("Ke ")){
			//Emissive
			String vec = line.replace("Ke ", "");
			mat.setKe(vec3.fromString(vec));
			return;
		}
		
		//Other Properties
		
		if (line.startsWith("Ns ")){
			//Shininess - specular coefficient
			String vec = line.replace("Ns ", "");
			mat.setNs(Float.parseFloat(vec));
			return;
		}
		if (line.startsWith("d ")){
			//Shininess - specular coefficient
			String vec = line.replace("d ", "");
			mat.setDissolve(Float.parseFloat(vec));
			return;
		}
		if (line.startsWith("Tf ")){
			//Transmission Filter
			//Tf can also define a spectral curve. Oh dear. Oh well.
			String vec = line.replace("Tf ", "");
			mat.setTf(vec3.fromString(vec));
			return;
		}
		if (line.startsWith("illum ")){
			//Chosen illumination model
			String vec = line.replace("illum ", "");
			mat.useIllum(Integer.parseInt(vec));
			return;
		}
		if (line.startsWith("Tr ")){
			//Transmission/transparency? 1-completely opaque, 0-completely transparent?
			String vec = line.replace("Tr ", "");
			mat.setTr(Float.parseFloat(vec));
			return;
		}
		if (line.startsWith("Ni ")){
			//Chosen illumination model
			String vec = line.replace("Ni ", "");
			mat.setNi(Float.parseFloat(vec));
			return;
		}
		
		
		//Maps
		if (line.startsWith("map_Ka ")){
			//Ambient map
			String vec = line.replace("map_Ka ", "");
			mat.useTexture("map_Ka", vec);
			return;
		}
		
		if (line.startsWith("map_Kd ")){
			//Diffuse map
			String vec = line.replace("map_Kd ", "");
			mat.useTexture("map_Kd", vec);
			return;
		}
		if (line.startsWith("map_bump ")){
			//Bump
			String vec = line.replace("map_bump ", "");
			mat.useTexture("map_bump", vec);
			return;
		}
		if (line.startsWith("bump ")){
			//Also bump
			String vec = line.replace("bump ", "");
			mat.useTexture("bump", vec);
			return;
		}
		
		if (line.startsWith("map_d ")){
			//Ambient map
			String vec = line.replace("map_d ", "");
			mat.useTexture("map_d", vec);
			return;
		}
		
		
		
		//Misc
		if (line.startsWith("#")){
			//Ignore comments
			return;
		}
		if(line.equals(" ")||line.equals("")){
			//Whitespace
			return;
		}
		
		
		System.out.println("Line of MTL file ignored: "+line);
	}
	public static Model loadSponza() {
		// TODO Auto-generated method stub
		return load(null);
	}
}
