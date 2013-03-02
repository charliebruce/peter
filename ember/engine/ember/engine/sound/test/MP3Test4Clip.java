package ember.engine.sound.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.BooleanControl.Type;
import javax.sound.sampled.Clip;

import com.sun.media.jmc.Media;
import com.sun.media.jmc.MediaProvider;

public class MP3Test4Clip {
private static MediaProvider provider;
static File file = new File("./data/ga.mp3");

	public static void main (String[] args) throws MalformedURLException, URISyntaxException{
		//Clip c = new Clip(new File("."));
		provider = new MediaProvider();
		URI uri = file.toURL().toURI();

		Clip c;

	Media media = new Media(uri);
				//System.out.println("Media: " + media.getDuration() + ", " + media.getContainerType() + ", " + media.getFrameSize() + ", " + media.getProtocolType());
				provider.setPlayerPeerFromMedia(media);
	
		provider.play();
					
	}
	
}
