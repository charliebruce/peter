package ember.engine.sound.test;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.sun.media.jmc.control.AudioControl;

public class BasicMP3Test implements Runnable {

	
	 AudioInputStream audioInputStream;
	   

	 //static File file = new File("./data/sos.mp3");
	 //static File file = new File("./data/nyg.mp3");locks
	 //static File file = new File("./data/lt.mp3");nusupportedformat
	 //	 static File file = new File("./data/technicolour.mp3");weird
	 //static File file = new File("./data/pe.wav");
	 static File file = new File("./data/sos.mp3");
	 
    SourceDataLine line;
    static Thread thread;

    String filename;
    int bufSize = 16384*4;//16384
    double duration, seconds;
    
    
	public static void main (String[] args){
		//if (file.exists())
		
		thread = new Thread(new BasicMP3Test());
        thread.setName("Playback");
        thread.setPriority(Thread.MAX_PRIORITY);//This should be highish otherwise audio stutters occasionally on low spec hw
        //TODO fix stutter, multithread, fadeable, multispeaker
        thread.start();
       //s thread.start();
	}
	
	public void shutDown(String s){
		
		System.err.println(s);
		thread.stop();
		
	}
	
	
	@Override
	public void run() {
		
        

        // reload the file if loaded by file
        if (file != null) {
            createAudioInputStream(file, false);
        }

        // make sure we have something to play
        if (audioInputStream == null) {
            shutDown("No loaded audio to play back");
            return;
        }
        // reset to the beginnning of the stream KILLS SOME WAVs
        try {
           // audioInputStream.reset();
        } catch (Exception e) {
            shutDown("Unable to reset the stream\n" + e);
            return;
        }

        // get an AudioInputStream of the desired format for playback
        AudioFormat format = getFormat();
        AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
        
        System.out.println(format.toString());
        
        if (playbackInputStream == null) {
            shutDown("Unable to convert stream of format " + audioInputStream + " to format " + format);
            return;
        }

        // define the required attributes for our line, 
        // and make sure a compatible line is supported.

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, 
            format);
        if (!AudioSystem.isLineSupported(info)) {
            shutDown("Line matching " + info + " not supported.");
            return;
        }

        // get and open the source data line for playback.

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufSize);
        } catch (LineUnavailableException ex) { 
            shutDown("Unable to open the line: " + ex);
            return;
        }

        // play back the captured audio data

        int frameSizeInBytes = format.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead = 0;

        // start the source data line
        line.start();

        while (thread != null) {
            try {
            	
            	//AudioControl ac = AudioSystem.getClip().getControl(Control.);
            	
                if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                    break;
                }
                int numBytesRemaining = numBytesRead;
                while (numBytesRemaining > 0 ) {
                    numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                }
            } catch (Exception e) {
                shutDown("Error during playback: " + e);
                break;
            }
        }
        // we reached the end of the stream.  let the data play out, then
        // stop and close the line.
        if (thread != null) {
            line.drain();
        }
        line.stop();
        line.close();
        line = null;
        //shutDown(null);
	}

	
	
	 public void createAudioInputStream(File file, boolean updateComponents) {
	        if (file != null && file.isFile()) {
	            try {
	                this.file = file;
	                audioInputStream = AudioSystem.getAudioInputStream(file);
	                filename = file.getName();
	                long milliseconds = (long)((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
	                duration = milliseconds / 1000.0;
	        
	                if (updateComponents) {
	                    //formatControls.setFormat(audioInputStream.getFormat());
	                }
	            } catch (Exception ex) { 
	            	ex.printStackTrace();
	            }
	        } else {
	        }
	    }

	 public AudioFormat getFormat() {

        
         

         AudioFormat.Encoding encoding = AudioFormat.Encoding.ULAW;
         String encString = "linear";
         float rate = 44100;
         int sampleSize = 16;
         String signedString = "signed";
         boolean bigEndian = false;//((String) v.get(4)).startsWith("big");
         int channels = 2;

         if (encString.equals("linear")) {
             if (signedString.equals("signed")) {
                 encoding = AudioFormat.Encoding.PCM_SIGNED;
             } else {
                 encoding = AudioFormat.Encoding.PCM_UNSIGNED;
             }
         } else if (encString.equals("alaw")) {
             encoding = AudioFormat.Encoding.ALAW;
         }
         return new AudioFormat(encoding, rate, sampleSize, 
                       channels, (sampleSize/8)*channels, rate, bigEndian);
     }

}
