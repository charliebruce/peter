package magma.client.graphics.opengl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2;

import magma.client.Client;
import magma.logger.Log;

import com.jogamp.common.nio.Buffers;

public class Shader {

	String frag;
	String vert;
	String n;
	
	int vertid, fragid, id;
	
	public void load(String name, File vertex, File fragment){
		n = name;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Client.basedir+vertex);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		StringBuilder sb = new StringBuilder();
		
		String line;
		try {
			while ((line = br.readLine()) != null){
				sb.append(line);
				sb.append('\n');
			}
		} catch (IOException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		vert = sb.toString();
		
		try {
			fis = new FileInputStream(Client.basedir+fragment);
		} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//TODO handle meeee
				Log.err("Shader vertex text not found. TODO handle very late loading albeit w stutter");
		}
		
		br = new BufferedReader(new InputStreamReader(fis));
		sb = new StringBuilder();
		
		line = "";
		
		try {
			while ((line = br.readLine()) != null)
			{
				sb.append(line);
				sb.append('\n');
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		frag = sb.toString();
		
	}
	
	public void init(GL2 gl){
		
		vertid = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		fragid = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);

		
		gl.glShaderSource(fragid, 1, new String[]{frag}, new int[]{frag.length()},0);
		gl.glCompileShader(fragid);
		
		gl.glShaderSource(vertid, 1, new String[]{vert}, new int[]{vert.length()},0);
		gl.glCompileShader(vertid);

		//Program
		id = gl.glCreateProgram();
		gl.glAttachShader(id, vertid);
		gl.glAttachShader(id, fragid);
		
		//gl.glLinkProgram(id);
		//gl.glValidateProgram(id);
		
		
	}
	public void link(GL2 gl){
		gl.glLinkProgram(id);
	}
	public void validate(GL2 gl){
		gl.glValidateProgram(id);
		logProgram(gl, getShaderProgram(), n);

	}
	
	public int getShaderProgram(){
		return id;
	}
	public int getFragProgram(){
		return fragid;
	}
	public int getVertProgram(){
		return vertid;
	}
	
	private final void logProgram(final GL2 gl, final int programId, final String name)
    {
            final IntBuffer lenBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGetProgramiv(programId, GL2.GL_INFO_LOG_LENGTH, lenBuffer);
            final int length = lenBuffer.get();
            
            if (length > 1)
            {
                    lenBuffer.clear();
                    final ByteBuffer logBuffer = Buffers.newDirectByteBuffer(length);
                    gl.glGetProgramInfoLog(programId, length, lenBuffer, logBuffer);
                    final byte[] logData = new byte[length];
                    logBuffer.get(logData);
                    Log.err("Error in " + name + "!");
                    Log.err("Error in program: "+new String(logData));
                    checkVertLogInfo(gl);
                    checkFragLogInfo(gl);
            }
            else
            {
                    Log.debug("Shader " + name + " linked successfully.");
            }
    }
	
	
	void checkVertLogInfo(GL2 gl)  
	  {
		IntBuffer iVal = Buffers.newDirectIntBuffer(1);
		gl.glGetShaderiv(vertid, GL2.GL_INFO_LOG_LENGTH, iVal);
		int length = iVal.get();
		if (length <= 1){return;}
	    ByteBuffer infoLog = Buffers.newDirectByteBuffer(length);
	    iVal.flip();
	    gl.glGetShaderInfoLog(vertid, length, iVal, infoLog);
	    byte[] infoBytes = new byte[length];
	    infoLog.get(infoBytes);
	    Log.info("GLSL Vert log: " + new String(infoBytes));
	  }
	void checkFragLogInfo(GL2 gl)  
	  {
		IntBuffer iVal = Buffers.newDirectIntBuffer(1);
		gl.glGetShaderiv(fragid, GL2.GL_INFO_LOG_LENGTH, iVal);
		int length = iVal.get();
		if (length <= 1){return;}
		ByteBuffer infoLog = Buffers.newDirectByteBuffer(length);
		iVal.flip();
		gl.glGetShaderInfoLog(fragid, length, iVal, infoLog);
		byte[] infoBytes = new byte[length];
		infoLog.get(infoBytes);
		Log.info("GLSL Frag log: " + new String(infoBytes));
	}

	public void setUniform1i(GL2 gl, String string, int i) {
		int location = gl.glGetUniformLocation(this.getShaderProgram(), string);
		gl.glUniform1i(location, i);
	}

	public void printLogs(GL2 gl) {
		checkFragLogInfo(gl);
		checkVertLogInfo(gl);
	}

	public void setUniform1f(GL2 gl, String string, float f) {
		int location = gl.glGetUniformLocation(this.getShaderProgram(), string);
		gl.glUniform1f(location, f);
	} 
}
