package ember.server.net.codec;


import java.nio.ByteBuffer;
import ember.server.net.*;
import ember.util.*;

public class GameProtocolEncoder {


    //DO THIS INSIDE PACKET?
    public static byte[] encode(Packet p) {
        try {
            
            byte[] data = p.getData();
            int dataLength = p.getLength();
            ByteBuffer buffer;
            buffer = ByteBuffer.allocate(dataLength + 2);
            
            int id = p.getId();
            if (id > 65535) //trying to send more data then we can represent with 16 bits!
                throw new IllegalArgumentException("Tried to send packet length " + dataLength + " in 16 bits [pid=" + p.getId() + "]");
            if (dataLength > 65535) //trying to send more data then we can represent with 16 bits!
                throw new IllegalArgumentException("Tried to send packet length " + dataLength + " in 16 bits [pid=" + p.getId() + "]");
   
            buffer.put((byte) id);
            buffer.put((byte) dataLength);
            buffer.put(data, 0, dataLength);
            
            buffer.flip();
            return buffer.array();
        } catch (Exception err) {
            Logger.getInstance().stackTrace(err);
        }
		return null;
    }


}
