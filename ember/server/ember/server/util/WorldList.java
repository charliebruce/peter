package ember.server.util;


import java.util.ArrayList;
import java.util.List;

import java.nio.ByteBuffer;

public class WorldList {

	private static final List<WorldDef> worldList = new ArrayList<WorldDef>();

	public void load() {
		worldList.add(new WorldDef(1, 0, FLAG_NON_MEMBERS, "Main Server", "127.0.0.1", "UK", COUNTRY_UK));
		worldList.add(new WorldDef(2, 0, FLAG_MEMBERS, "PvP World", "127.0.0.1", "UK", COUNTRY_UK));
	}

	public static ByteBuffer getData(boolean worldConfiguration,
			boolean worldStatus) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put((byte) (worldStatus ? 1 : 0));
		buffer.put((byte) (worldConfiguration ? 1 : 0));
		if (worldConfiguration) {
			populateConfiguration(buffer);
		}
		if (worldStatus) {
			populateStatus(buffer);
		}
		buffer.flip();
		ByteBuffer finalBuffer = ByteBuffer.allocate(buffer.limit() + 3);
		finalBuffer.put((byte) 0);
		finalBuffer.putShort((short) buffer.limit());
		finalBuffer.put(buffer);
		finalBuffer.flip();
		return finalBuffer;
	}

	private static void populateConfiguration(ByteBuffer buffer) {
		putSmart(buffer, worldList.size());
		setCountry(buffer);
		putSmart(buffer, 0);
		putSmart(buffer, (worldList.size() + 1));
		putSmart(buffer, worldList.size());
		for (WorldDef w : worldList) {
			putSmart(buffer, w.getWorldId());
			buffer.put((byte) w.getLocation());
			buffer.put((byte) w.getFlag());
			putString(buffer, w.getActivity()); // activity
			putString(buffer, w.getIp()); // ip address
		}
		buffer.put((byte) 0x94DA4A87); // != 0
	}

	private static void populateStatus(ByteBuffer buffer) {
		for (WorldDef w : worldList) {
			putSmart(buffer, w.getWorldId()); // world id
			buffer.putShort((short) w.getPlayers()); // player count
		}
	}

	private static void putString(ByteBuffer buffer, String string) {
		buffer.put((byte) 0);
		buffer.put(string.getBytes());
		buffer.put((byte) 0);
	}

	private static void putSmart(ByteBuffer buffer, int value) {
		if ((value ^ 0xffffffff) > -129) {
			buffer.put((byte) value);
		} else {
			buffer.putShort((short) value);
		}
	}

	private static void setCountry(ByteBuffer buffer) {
		for (WorldDef w : worldList) {
			putSmart(buffer, w.getCountry());
			putString(buffer, w.getRegion());
		}
	}


	private static final int COUNTRY_UK = 77;

	private static final int FLAG_MEMBERS = 1;

	private static final int FLAG_NON_MEMBERS = 0;

}