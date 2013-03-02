package ember.server.net;

/**
 * Packet object.
 *
 */
public final class Packet {


    /**
     * The connection (therefore client) which sent this packet
     */
    private ServerConnection connection;
    /**
     * The ID of the packet
     */
    private int id;
    /**
     * The length of the payload
     */
    private int length;
    /**
     * The payload
     */
    private byte[] data;
    /**
     * The current index into the payload buffer for reading
     */
    private int caret = 0;

   
    /**
     * Create a representation of the packet.
     */
   public Packet(int id, byte[] data) {
		this.id = id;
		this.length = data.length;
		this.data = data;
	}
   public Packet(int id, int length, byte[] data) {
		this.id = id;
		this.length = length;
		this.data = data;
	}


   /**
     * Returns the connection associated with the packet, if any.
     *
     * @return The <code>IoSession</code> object, or <code>null</code>
     *         if none.
     */
    public ServerConnection getConnection() {
        return connection;
    }


    /**
     * Returns the packet ID.
     *
     * @return The packet ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the length of the payload of this packet.
     *
     * @return The length of the packet's payload
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the entire payload data of this packet.
     *
     * @return The payload <code>byte</code> array
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Returns the remaining payload data of this packet.
     *
     * @return The payload <code>byte</code> array
     */
    public byte[] getRemainingData() {
        byte[] data = new byte[length - caret];
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i + caret];
        }
        caret += data.length;
        return data;

    }

    /**
     * Reads the next <code>byte</code> from the payload.
     *
     * @return A <code>byte</code>
     */
    public byte readByte() {
        return data[caret++];
    }

    /**
     * Reads the next <code>short</code> from the payload.
     *
     * @return A <code>short</code>
     */
    public short readShort() {
        return (short) ((short) ((data[caret++] & 0xff) << 8) | (short) (data[caret++] & 0xff));
    }

    public int readLEShortA() {
        int i = ((data[caret++] - 128 & 0xff)) + ((data[caret++] & 0xff) << 8);
        if (i > 32767)
            i -= 0x10000;
        return i;
    }

    public int readLEShort() {
        int i = ((data[caret++] & 0xff)) + ((data[caret++] & 0xff) << 8);
        if (i > 32767)
            i -= 0x10000;
        return i;
    }
	
    /**
	 * Gets a 3-byte integer.
	 * @return The 3-byte integer.
	 */
	public int readTriByte() {
		return ((data[caret++] << 16) & 0xFF) | ((data[caret++] << 8) & 0xFF) | (data[caret++] & 0xFF);
	}

    /**
     * Reads the next <code>int</code> from the payload.
     *
     * @return An <code>int</code>
     */
    public int readInt() {
        return ((data[caret++] & 0xff) << 24)
                | ((data[caret++] & 0xff) << 16)
                | ((data[caret++] & 0xff) << 8)
                | (data[caret++] & 0xff);
    }

    public int readInt2() {
        return ((data[caret++] & 0xff) << 16)
                | ((data[caret++] & 0xff) << 24)
                | (data[caret++] & 0xff)
                | ((data[caret++] & 0xff) << 8);
    }

    public int readInt1() {
        return ((data[caret++] & 0xff) << 8)
                | (data[caret++] & 0xff)
                | ((data[caret++] & 0xff) << 24)
                | ((data[caret++] & 0xff) << 16);
    }

    public int readLEInt() {
        return (data[caret++] & 0xff)
                | ((data[caret++] & 0xff) << 8)
                | ((data[caret++] & 0xff) << 16)
                | ((data[caret++] & 0xff) << 24);
    }

    /**
     * Reads the next <code>long</code> from the payload.
     *
     * @return A <code>long</code>
     */
    public long readLong() {
        return ((long) (data[caret++] & 0xff) << 56)
                | ((long) (data[caret++] & 0xff) << 48)
                | ((long) (data[caret++] & 0xff) << 40)
                | ((long) (data[caret++] & 0xff) << 32)
                | ((long) (data[caret++] & 0xff) << 24)
                | ((long) (data[caret++] & 0xff) << 16)
                | ((long) (data[caret++] & 0xff) << 8)
                | ((data[caret++] & 0xff));
    }

    /**
     * Reads the string which is formed by the unread portion
     * of the payload.
     *
     * @return A <code>String</code>
     */
    public String readString() {
        return readString(length - caret);
    }

    public String readRS2String() {
        int start = caret;
        while (data[caret++] != 0) ;
        return new String(data, start, caret - start - 1);
    }

    public void readBytes(byte[] buf, int off, int len) {
        for (int i = 0; i < len; i++) {
            buf[off + i] = data[caret++];
        }
    }

    /**
     * Reads a string of the specified length from the payload.
     *
     * @param length The length of the string to be read
     * @return A <code>String</code>
     */
    public String readString(int length) {
        String rv = new String(data, caret, length);
        caret += length;
        return rv;
    }

    /**
     * Skips the specified number of bytes in the payload.
     *
     * @param x The number of bytes to be skipped
     */
    public void skip(int x) {
        caret += x;
    }


    public int remaining() {
        return data.length - caret;
    }

    /**
     * Returns this packet in string form.
     *
     * @return A <code>String</code> representing this packet
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[id=" + id + ",len=" + length + ",data=0x");
        for (int x = 0; x < length; x++) {
            sb.append(byteToHex(data[x], true));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String byteToHex(byte b, boolean forceLeadingZero) {
        StringBuilder out = new StringBuilder();
        int ub = b & 0xff;
        if (ub / 16 > 0 || forceLeadingZero)
            out.append(hex[ub / 16]);
        out.append(hex[ub % 16]);
        return out.toString();
    }

    private static final char[] hex = "0123456789ABCDEF".toCharArray();
    
	public int readByteA() {
        return (byte) (readByte() - 128);
    }
    public int readShortA() {
        caret += 2;
        return ((data[caret - 2] & 0xFF) << 8) + (data[caret - 1] - 128 & 0xFF);
    }

    public byte readByteC() {
        return (byte) -readByte();
    }
	  public int readUnsignedShortBigEndian128() {
        return (data[caret++]  - 128 & 0xff) + ((data[caret++]  & 0xff) << 8);
    }
    public byte readByteS() {
		return (byte) (128 - readByte());
	}

}
