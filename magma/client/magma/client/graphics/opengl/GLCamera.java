package magma.client.graphics.opengl;

import magma.client.graphics.Camera;
import magma.client.input.KeyState;

public class GLCamera extends Camera {

	private float[] cachedProjection;
	private float[] viewMatrix;
	//Equation is fovx = atan(tan(fovy/2)*width/height)*2) RADIANS
	public float[] projection(){
		recalculateProjection();
		return cachedProjection;
	}

	@Override
	public void recalculateProjection() {
		/*
		Given f defined as follows:
		aspect=w/h
		f = cotangent fovy 2

		*/
		if(height==0)height = 1;//Prevent / by 0

		float aspect = (float)width/(float)height;

		float fovy;
		float fovh = 90.0f; //90 degree field of vision horizontal is quite wide. Make this user-adjustable?
		fovy = fovh/aspect;
		
		float f = (float) (1.0/Math.tan(Math.toRadians(fovy/2)));

		float zFar = (float) 10000.0;
		float zNear = (float) 1.0;
		//By http://www.manpagez.com/man/3/gluPerspective/ 
		cachedProjection = new float[]{f/aspect, 0,0,0,0,f,0,0,0,0,(zFar+zNear)/(zNear-zFar),-1,0,0, (2*zFar*zNear)/(zNear-zFar), 1/*0???*/};

	}

	float[] position = new float[]{2,2,2};
	float[] looktarget = new float[]{0.0f,0.0f,0.0f};
	int lookmode = 0;//0 point 1 free 2 fwd 3 back 4 left 5 right
	@Override
	public float[] transformation() {
		switch(lookmode){
		case 0: break;
		case 2: 
			looktarget[0]=position[0]+1.0f;
			looktarget[1]=position[1];
			looktarget[2]=position[2];
		break;
		case 3: 
			looktarget[0]=position[0]-1.0f;
			looktarget[1]=position[1];
			looktarget[2]=position[2];
		break;
		case 4: 
			looktarget[0]=position[0];
			looktarget[1]=position[1];
			looktarget[2]=position[2]-1.0f;
		break;
		case 5: 
			looktarget[0]=position[0];
			looktarget[1]=position[1];
			looktarget[2]=position[2]+1.0f;
		break;
		
		}
		
		setCamera(position[0],position[1],position[2],looktarget[0],looktarget[1],looktarget[2]);//0 2 -1
		
		/*System.out.print("PMV:");
		float[] pmv = multMatrix(cachedProjection, viewMatrix);
		for(int i = 0; i<16;i++){
			System.out.print(pmv[i]);
			System.out.print(",");
		}
		System.out.print("\n");
		
		System.out.print("PROJ:");
		
		for(int i = 0; i<16;i++){
			System.out.print(cachedProjection[i]);
			System.out.print(",");
		}
		System.out.print("\n");
		
		System.out.print("VIEW:");
		for(int i = 0; i<16;i++){
			System.out.print(viewMatrix[i]);
			System.out.print(",");
		}
		System.out.print("\n");*/
		
		return viewMatrix;
	}
	
	public void forcetransform(float[] direction){
		float[] temp = new float[3];
		temp[0]=position[0]+direction[0];
		temp[1]=position[1]+direction[1];
		temp[2]=position[2]+direction[2];
		position = temp;
	}
	
	//BELOW FROM LIGHTHOUSE3D
	 
	// ----------------------------------------------------
	// View Matrix
	//
	// note: it assumes the camera is not tilted,
	// i.e. a vertical up vector (remeber gluLookAt?)
	//
	 
	void setCamera(float posX, float posY, float posZ,
	               float lookAtX, float lookAtY, float lookAtZ) {
	 
	    float[] dir = new float[3];
	    float[] right = new float[3];
	    float[] up = new float[3];
	 
	    up[0] = 0.0f;   up[1] = 1.0f;/*1*/   up[2] = 0.0f;/*0*/
	 
	    dir[0] =  (lookAtX - posX);
	    dir[1] =  (lookAtY - posY);
	    dir[2] =  (lookAtZ - posZ);
	    dir = Vector.normalize3(dir);
	 
	    right = Vector.crossProduct3(dir,up);
	    right = Vector.normalize3(right);
	 
	    up = Vector.crossProduct3(right,dir);
	    up = Vector.normalize3(up);
	 
	    viewMatrix=new float[16];
	    
	    viewMatrix[0]  = right[0];
	    viewMatrix[4]  = right[1];
	    viewMatrix[8]  = right[2];
	    viewMatrix[12] = 0.0f;
	 
	    viewMatrix[1]  = up[0];
	    viewMatrix[5]  = up[1];
	    viewMatrix[9]  = up[2];
	    viewMatrix[13] = 0.0f;
	 
	    viewMatrix[2]  = -dir[0];
	    viewMatrix[6]  = -dir[1];
	    viewMatrix[10] = -dir[2];
	    viewMatrix[14] =  0.0f;
	 
	    viewMatrix[3]  = 0.0f;
	    viewMatrix[7]  = 0.0f;
	    viewMatrix[11] = 0.0f;
	    viewMatrix[15] = 1.0f;
	    
	    float[] aux = new float[16];
	 
	    aux = setTranslationMatrix(-posX, -posY, -posZ);
	 
	    viewMatrix = Matrix.multMatrix(viewMatrix, aux);
	}
	
	
	private float[] setTranslationMatrix( float f, float g, float h) {
		float[] res = new float[16];
		for(int i=0;i<16;i++){
			res[i]=0;
			if(i%5==0){res[i]=1;}
		}
		res[12]=f;
		res[13]=g;
		res[14]=h;
		
		
		
		return res;
	}




	@Override
	public void changelook(String args) {
		if(args.equals("origin"))looktarget=new float[]{0.0f,0.0f,0.0f};

		System.out.println("Changing look mode: '"+args+"'");
	//0 point 1 free 2 fwd 3 back 4 left 5 right
		if(args.equals("dirleft")) lookmode=4;
		if(args.equals("dirright")) lookmode=5;
		if(args.equals("dirfwd")) lookmode=2;
		if(args.equals("dirback")) lookmode=3;
		
		
	}

	@Override
	public void tick() {
		if(KeyState.dirfwd){
			this.position[0]=this.position[0]+1;
		}
		if(KeyState.dirback){
			this.position[0]=this.position[0]-1;
		}
		if(KeyState.dirleft){
			this.position[2]=this.position[2]-1;
		}
		if(KeyState.dirright){
			this.position[2]=this.position[2]+1;
		}
		if(KeyState.dirup){
			this.position[1]=this.position[1]+1;
		}
		if(KeyState.dirdown){
			this.position[1]=this.position[1]-1;
		}
	}

	@Override
	public float[] position() {
		return new float[]{position[0],position[1],position[2],(float)1.0};
	}
}
