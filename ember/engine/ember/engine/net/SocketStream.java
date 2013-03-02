package ember.engine.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import ember.engine.Constants;

/**
 * Basic Stream object stores socket, in, out, and buffers writes for performance. 
 * TODO ensure synchronised
 * @author Charlie
 *
 */
public class SocketStream implements Runnable {

	private Socket socket;
	public OutputStream out;
	public InputStream in;
	
	public int status;
	
	public boolean stopped;
	Thread thread;
	
	public SocketStream(Socket s, String name) throws IOException {
		
		stopped = false;
		socket = s;
		socket.setSoTimeout(Constants.SO_TIMEOUT);
		socket.setTcpNoDelay(Constants.TCP_NO_DELAY);
		
		in = socket.getInputStream();
		out = socket.getOutputStream();
		
		if (name==null || name.equals("")){
			name = "SocketStream to " + socket.getInetAddress().getHostAddress();
		}
		
		
		thread = new Thread(this, name);
		thread.start();
		
	}


	public final int available() throws IOException {

		if (stopped) {
			return 0;
		} else {
			return in.available();
		}
	}
	
	
	public void write(byte data){
		//TODO this to a buffer for speed.
		try {
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void write(byte[] datas){
		//TODO this buffered
		try {
			out.write(datas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnectStream(){
		if (stopped)
			return;
		stopped = true;
		status = Constants.ConnectionStatus.DISCONNECTED;
		try {
			
			in.close();
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//The above are necessary?
		
		in = null;//TODO dummies instead
		out = null;

		
	}
	
	public void bookmarkRead() {
		// TODO Auto-generated method stub
		
	}


	public void rewindRead() {
		// TODO Auto-generated method stub
		
	}


	public void unbookmarkRead() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	@Override
	public void run() {
		while (!stopped){
			//TODO write any buffered data
			//TODO check if disconnected?
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO timing better
				e.printStackTrace();
			}
		}
	}
	
	public Socket getSocket(){
		return socket;
	}
}
