import java.net.URL;

import javafx.stage.Stage;
import javafx.scene.Scene;

class MediaBoxImpl {

	public static void main(String[] args){
	URL media = new URL("http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_11810873001_09c01923-00.flv");
	
	MediaBox mediaBox = new MediaBox();
	
	mediaBox.mediaSource = media;
	
		{
		    // your media(video/audio) source and information
		    // Only mediaSource is the required variable. All others are optional.
		    mediaSource: media;
		    mediaTitle: "Big Buck Bunny";
		    mediaDescription: "A well-tempered rabbit finds his day..."
		
		    // controls the visibility of mediaTitle/mediaDescription
		    mediaBox.showMediaInfo= true;
		
		    // the position and size of the media on screen
		    translateX: 0
		    translateY: 0
		    width: 640
		    height: 360
		
		    // make the movie play as soon as it's loaded
		    autoPlay: true
		
		    // media controlbar height at the bottom of the media view
		    mediaControlBarHeight: 25
		
		    // some examples of function variables to interact with outside
		    onEndOfMedia: function() {
		    }
		    onMouseClicked: function(me) {
		    }
		}
	}

Stage {
    title: "MediaBox Player"
    resizable: true
    scene: Scene {
        content: mediaBox
    }
}

}
