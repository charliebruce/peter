package importers;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import magma.client.graphics.Graphics;
import magma.client.graphics.opengl.Renderer;

public class Model {

	float[] vertices;
	int[] indices;
	//private int numFaces=0;
	
	public Model(ArrayList<vec3> vertices2, ArrayList<Face> faces, ArrayList<vec3> normals, ArrayList<vec2> vts,
			Map<String, Material> materials) {

		
		HashMap<Integer, Integer> numFacesWithMaterial = new HashMap<Integer, Integer>();
		
		
		
		String mtlTemp = faces.get(1).getMaterialName();
		int numMaterials = 1;//The first material is on face 1
		int facesSinceRefresh=1;//The num faces since switch
		for(int i=2; i<faces.size(); i++){
			Face f = faces.get(i);
			
			if(!f.getMaterialName().equals(mtlTemp)){
				mtlTemp = f.getMaterialName();
				numFacesWithMaterial.put(numMaterials, facesSinceRefresh);
				//System.out.println("Stored "+numMaterials+" and "+facesSinceRefresh);
				numMaterials++;
				facesSinceRefresh = 0;
				
			}
		
			facesSinceRefresh++;
		}
		//Store the final set of points (no terminator to store).
		numFacesWithMaterial.put(numMaterials, facesSinceRefresh);

		//We now have shit stored.
		//Set up VBO data
		
		numVbos = numMaterials;
		numFaces = new int[numVbos];
		for(int i = 0; i<numVbos;i++){
			numFaces[i]=numFacesWithMaterial.get(i+1);
		}
		
		String[] myMatPaths = new String[numMaterials];
		String[] myNormPaths = new String[numMaterials];
		
		String tempmatname = faces.get(1).getMaterialName();
		myMatPaths[0]=materials.get(faces.get(1).getMaterialName()).texPath();
		myNormPaths[0]=materials.get(faces.get(1).getMaterialName()).getNormalPath();
		int onMaterial = 1;
		facesSinceRefresh = 1;
		float[] temp = new float[numFacesWithMaterial.get(1)*24];
		for(int i=2; i<faces.size(); i++){
			Face f = faces.get(i);
			
			if(!f.getMaterialName().equals(tempmatname)){
				//System.out.println("In fragment"+onMaterial+" and material "+materials.get(f.getMaterialName()).texPath()+" so storing "+materials.get(tempmatname).texPath());
				
				//matpathstodo[onMaterial]=materials.get(tempmatname).texPath();
				myMatPaths[onMaterial]=materials.get(f.getMaterialName()).texPath();
				myNormPaths[onMaterial]="null";
				if(materials.get(f.getMaterialName()).useNormalMap()){
					myNormPaths[onMaterial]=materials.get(f.getMaterialName()).getNormalPath();
					//System.out.println("Stored normal "+myNormPaths[onMaterial]);
				}
				tempmatname = f.getMaterialName();
				//Store
				
				data.put(onMaterial, temp);
				
				onMaterial++;
				temp = new float[numFacesWithMaterial.get(onMaterial)*24];
				facesSinceRefresh = 0;
				
			}
			//System.out.println("Trace at "+i);
			float[] other = f.toVbo(vertices2, normals, vts);
			for(int j=0;j<24;j++){
				temp[24*facesSinceRefresh+j]=other[j];
			}
		
			
			facesSinceRefresh++;
		}
		//matpathstodo[onMaterial]=materials.get(tempmatname).texPath();
		data.put(onMaterial, temp);
		
		//myMatPaths[onMaterial]=materials.get(tempmatname).texPath();
		//myNormPaths[onMaterial]=materials.get(tempmatname).getNormalPath();
		
		
		texpaths = myMatPaths;
		normalpaths = myNormPaths;
		
		//System.out.println("Texpaths goes"+texpaths[0]+","+texpaths[1]);
		
		/*FAILED EXPERIMENTATION
		
		//Calculate the number of materials
		String material = faces.get(1).getMaterialName();
		int numMaterialsUsedInScene=1;
		for(int i=1; i<faces.size(); i++){
			Face f = faces.get(i);
			if(!f.getMaterialName().equals(material)){numMaterialsUsedInScene++;material=f.getMaterialName();}
		}
		
		//Count the number of faces to get the length of the vbos.
		material=faces.get(1).getMaterialName();
		int[] numFacesWithMaterial=new int[numMaterialsUsedInScene-1];
		int temp=0;
		int at=0;
		for(int i=1; i<faces.size(); i++){
			temp++;
			Face f = faces.get(i);
			if(!f.getMaterialName().equals(material)){
				material=f.getMaterialName();
				System.out.println("Face "+(i-1)+" uses "+faces.get(i-1).getMaterialName()+" and next is "+ faces.get(i).getMaterialName()+" so Storing "+temp+" in "+at);
				numFacesWithMaterial[at]=temp;
				temp=0;
				at++;
			}
			
		}
		
		
		//Now get the string paths. This can be loaded using renderer, or late-loaded.
		String[] texpaths = new String[numMaterialsUsedInScene];

		int count=0;
		for(int i=1; i<faces.size(); i++){
			Face f = faces.get(i);
			if(!f.getMaterialName().equals(material)){
				texpaths[count]=materials.get(f.getMaterialName()).texPath();
				material=f.getMaterialName();
				count++;
			}
		}
		
		for(int i=0;i<numFacesWithMaterial.length;i++){
			if(numFacesWithMaterial[i]==0){
				System.out.println("SHIT NUMBER "+i+" BROKEN");
			}
		}
		
		System.out.println("There are "+numMaterialsUsedInScene+" including "+numFacesWithMaterial[0]+" faces with material "+faces.get(1).getMaterialName()+" found in "+texpaths[0]);//TODO not the most efficient, group us to minimise texture switches
		
		//Now we convert each faceset using a single material into a VBO
		HashMap<Integer,float[]> hm = new HashMap<Integer,float[]>();
		*/
		
				
		/*
		String[] mats = new String[numMaterials];
		int[] lengthsOfVboInBytesIThink = new int[numMaterials];
		int whichVboAreWeOn=0;
		HashMap<Integer,float[]> array = new HashMap<Integer,float[]>();
		float[] temp=new float[]{};
		material=faces.get(1).getMaterialName();
		mats[whichVboAreWeOn]=material;
		int index=0;
		
		for(int i=1; i<faces.size(); i++){
			Face f = faces.get(i);
			if(!f.getMaterialName().equals(material)){array.put(whichVboAreWeOn, temp);whichVboAreWeOn++;material=f.getMaterialName();mats[whichVboAreWeOn]=material;}
			float[] result = f.toVbo(vertices2, vts);
			for(int x=0;x<24;x++){
				temp[index+x]=result[x];
			}
			index=index+24;
			
		}*/
		
		
		/*
		 * VERSION 2 HALF WORKS BUT DOES NOT PRESERVE TEXTURES
		 */
		
		//3 points per face, 8 pieces f data (loc, norm, st)
		/*float[] data = new float[8*3*faces.size()];
		Log.info("Num VTs:"+vts.size());
		for(int i=1; i<faces.size(); i++){
			numFaces++;
			//Per face computation
			int startpoint = i*8*3;
			Face f = faces.get(i);

			//Log.debug("Here we go: "+f.getvt(1)+","+f.getvt(2)+","+f.getvt(3));
			int v1 = f.getvertex(1);
			int v2 = f.getvertex(2);
			int v3 = f.getvertex(3);
			
			int vt1 = f.getvt(1);
			int vt2 = f.getvt(2);
			int vt3 = f.getvt(3);
			
			vec3 a = vertices2.get(v1);
			vec3 b = vertices2.get(v2);
			vec3 c = vertices2.get(v3);//-1?
			
			vec2 vta = vts.get(vt1-1);
			vec2 vtb = vts.get(vt2-1);
			vec2 vtc = vts.get(vt3-1);
			
			data[startpoint]=a.x;
			data[startpoint+1]=a.y;
			data[startpoint+2]=a.z;
			///Norm
		//	if(vta!=null){
				
				data[startpoint+6]=vta.x;
				data[startpoint+7]=vta.y;
				System.out.println(vta.x+","+vta.y+","+vtb.x+","+vtb.y+","+vtc.x+","+vtc.y);
		//	}
			
			data[startpoint+8]=b.x;
			data[startpoint+1+8]=b.y;
			data[startpoint+2+8]=b.z;
			///Norm
			//if(vtb!=null){
				data[startpoint+6+8]=vtb.x;
				data[startpoint+7+8]=vtb.y;
		//	}
			data[startpoint+16]=c.x;
			data[startpoint+1+16]=c.y;
			data[startpoint+2+16]=c.z;
			///Norm
			//if(vtc!=null)
		//	{
				data[startpoint+6+16]=vtc.x;
				data[startpoint+7+16]=vtc.y;
		//	}
			
			
		}
		*/
		
		/*
		vertices = new float[vertices2.size()*8-8];//xyz,nx,ny,nz,s,t
		indices = new int[3*faces.size()-3];
		
		for(int i=1; i<(vertices2.size()); i++){//1 to range
			vec3 v = vertices2.get(i);
			vertices[8*(i-1)]=v.x;
			vertices[8*(i-1)+1]=v.y;
			vertices[8*(i-1)+2]=v.z;
			
			vertices[8*(i-1)+3]=0;
			vertices[8*(i-1)+4]=0;//Null vectors. Imma lazy.
			vertices[8*(i-1)+5]=0;
		}

		
		for(int i=1; i<faces.size(); i++){
			//Per face computation
			Face f = faces.get(i);
			int v1 = f.getvertex(1);
			int v2 = f.getvertex(2);
			int v3 = f.getvertex(3);
			
			vertices[8*(v1-1)+6]=vts.get(f.getvt(1)-1).x;
			vertices[8*(v1-1)+7]=vts.get(f.getvt(1)-1).y;
			
			vertices[8*(v2-1)+6]=vts.get(f.getvt(2)-1).x;
			vertices[8*(v2-1)+7]=vts.get(f.getvt(2)-1).y;
			
			vertices[8*(v3-1)+6]=vts.get(f.getvt(3)-1).x;
			vertices[8*(v3-1)+7]=vts.get(f.getvt(3)-1).y;
			
			
		}
		
		for(int i=1; i<faces.size(); i++){
			Face f = faces.get(i);
			indices[3*(i-1)]= f.getvertex(1);
			indices[3*(i-1)+1]= f.getvertex(2);
			indices[3*(i-1)+2]= f.getvertex(3);
		}
		
		*/
		
		
	}

	int numVbos;
	int[] numFaces;
	HashMap<Integer, float[]> data = new HashMap<Integer,float[]>();
	String[] texpaths;
	String[] normalpaths;
	
	int[] myvbos;
	
	//Loop through groups and store VBOs
	public void preload(GL2 gl) {
		myvbos = new int[numVbos];
		gl.glGenBuffers(numVbos, myvbos, 0);
		
		for(int i = 1; i < (numVbos+1);i++){
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, myvbos[i-1]);
			//System.out.println("Loading data chunk"+(i-1));
			gl.glBufferData(GL.GL_ARRAY_BUFFER, numFaces[i-1]*8*3*4/*8bpp 3p 4bpfloat*/,FloatBuffer.wrap(data.get(i)), GL.GL_STATIC_DRAW);
		}
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		gl.glEnable(GL.GL_ARRAY_BUFFER);

	}

	
	public void draw(GL2 gl, Renderer renderer, int shaderLocation, int drawmode){
		
		
		for(int i = 0; i<numVbos;i++){
			String tex = texpaths[i];
			if(tex!=null&&!tex.equals("null")){
				tex=tex.replace("gi_flag.tga", "null.png");//TODO fix me
				tex=tex.replace("sponza/null", "sponza/textures/null");
				gl.glActiveTexture(GL.GL_TEXTURE0);
				gl.glBindTexture(GL.GL_TEXTURE_2D, renderer.texid(tex, gl));//THE TEXTURE TO RENDER to CTFBO
				
			}
			String norm = normalpaths[i];
			if(norm==null){norm="null";}
			if(norm.equals("null")){
				norm = "data/sponza/textures/nullnormal.png";//TANGENT SPACE TO SCREEN SPACE NORMALS TODO FIXME
			}
			//if(Graphics.normify){
			//TODO tex1
			gl.glActiveTexture(GL.GL_TEXTURE1);
			gl.glBindTexture(GL.GL_TEXTURE_2D, renderer.texid(norm, gl));//THE TEXTURE TO RENDER to CTFBO
			//}
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, myvbos[i]);//cubeVbo
			int vlocation;
			vlocation = gl.glGetAttribLocation(shaderLocation, "vertexin");
			gl.glVertexAttribPointer(vlocation, 3, GL.GL_FLOAT, false, 32,0);//3=xyz
			gl.glEnableVertexAttribArray(vlocation);
			vlocation = gl.glGetAttribLocation(shaderLocation, "texcoordin");
			gl.glVertexAttribPointer(vlocation, 2, GL.GL_FLOAT, false, 32,6*4);//3=xyz//0,0
			gl.glEnableVertexAttribArray(vlocation);
			vlocation = gl.glGetAttribLocation(shaderLocation, "normalin");
			gl.glVertexAttribPointer(vlocation, 3, GL.GL_FLOAT, false, 32,3*4);//3=xyz//0,0
			gl.glEnableVertexAttribArray(vlocation);
			vlocation = gl.glGetAttribLocation(shaderLocation, "tangentin");
			gl.glVertexAttribPointer(vlocation, 3, GL.GL_FLOAT, false, 32,3*4);//3=xyz//0,0
			gl.glEnableVertexAttribArray(vlocation);
			gl.glDrawArrays(drawmode, 0, 3*numFaces[i]);//36 for cube
		}
		
	}
	

	
	Material[] mats;

	public int getNumFloats() {
		return vertices.length;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getNumIndices() {
		return indices.length;
	}



	
	
}
