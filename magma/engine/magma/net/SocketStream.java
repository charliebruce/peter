package magma.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import magma.constants.ConnectionStatus;


/**
 * Basic Stream object stores socket, in, out, and buffers writes for performance. 
 * TODO ensure synchronised
 * @author Charlie
 *
 */
public class SocketStream {
	
	private Socket socket;
	public OutputStream out;
	public InputStream in;
	
	public int status;
	
	public boolean stopped;
	public String myname;
	
	public SocketStream(Socket s, String name) throws IOException {
		
		stopped = false;
		socket = s;
		
		//Already done by Listener
		//socket.setSoTimeout(Server.SO_TIMEOUT);
		//socket.setTcpNoDelay(Server.TCP_NO_DELAY);
		
		in = socket.getInputStream();
		out = socket.getOutputStream();
		
		if (name==null || name.equals("")){
			name = "SocketStream to " + socket.getInetAddress().getHostAddress();
		}
		myname=name;
		
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
		status = ConnectionStatus.DISCONNECTED;
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//The above are necessary?
		
		in = null;//TODO dummies instead
		out = null;

		
	}
	
	public Socket getSocket(){
		return socket;
	}
}
