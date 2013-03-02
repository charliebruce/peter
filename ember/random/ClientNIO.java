import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class ClientNIO {
  public static void main(String args[])
  {
  InetSocketAddress ISA=null;
  SocketChannel clientNIO=null;
  int port=11235;
  
  try{
     clientNIO=SocketChannel.open();
     clientNIO.configureBlocking(false);     
     }catch(IOException e)
        {System.out.println(e.getMessage());}

 try{
    InetAddress addr=InetAddress.getByName("localhost");
    ISA=new InetSocketAddress(addr,port);           
    }catch(UnknownHostException e)
      {System.out.println(e.getMessage());}
 
    
    try {
		clientNIO.configureBlocking(false);
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	
 try{
    clientNIO.connect(ISA); 
    }
 catch(IOException e)
      {System.out.println(e.getMessage());}
 Socket s = null;
 try{
	 s = new Socket();
	 s.bind(ISA);
	 s.connect(ISA);
 }
 catch (Exception ex){}
 
 
 while(true){
    	try {
			System.out.println(s.getInputStream().read());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
  }
}
