package magma.server.fileserver;

public class Disallow {

	static boolean isDisallowed(String name){
		//TODO avoid people ../ing their way in here.
		if(name=="data/keys/private.key") return true;//Make a trap here?
		return false;
	}
}
