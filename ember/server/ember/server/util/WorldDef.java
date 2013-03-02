package ember.server.util;

public class WorldDef {

	private String activity;
	private int country;
	private int flag;
	private String ip;
	private int location;
	private String region;
	private int worldId;
	private int players;

	public WorldDef(int worldId, int location, int flag, String activity, String ip, String region, int country) {
		this.worldId = worldId;
		this.location = location;
		this.flag = flag;
		this.activity = activity;
		this.ip = ip;
		this.region = region;
		this.country = country;
	}

	public String getActivity() {
		return activity;
	}

	public int getCountry() {
		return country;
	}

	public int getFlag() {
		return flag;
	}

	public String getIp() {
		return ip;
	}

	public int getLocation() {
		return location;
	}

	public String getRegion() {
		return region;
	}

	public int getWorldId() {
		return worldId;
	}
	
	public int getPlayers() {
		return players;
	}
	
	public void setPlayers(int players) {
		this.players = players;
	}
}