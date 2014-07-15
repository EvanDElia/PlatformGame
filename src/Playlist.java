import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Playlist extends Thread{
	
	private ArrayList<File> songs1 = new ArrayList<File>();
	private AudioFormat format;
    private byte[] samples;
    private boolean playing = true;
    private boolean kill = false;
    
    public Playlist(){
    	//default contrsuctor
    	addSong(new File("assets/music/fakebit.wav"));
		addSong(new File("assets/music/blast.wav"));
		addSong(new File("assets/music/gameboylove.wav"));
		addSong(new File("assets/music/godbless.wav"));
		addSong(new File("assets/music/iwantyou.wav"));
		addSong(new File("assets/music/meow.wav"));
		addSong(new File("assets/music/akira.wav"));
		addSong(new File("assets/music/everythingexplodes.wav"));
		
    }
    
    public Playlist(boolean metal){
    	if (metal){
    		addSong(new File("assets/music/temptingtime.wav"));
    		addSong(new File("assets/music/thoroughly.wav"));
    		addSong(new File("assets/music/odessa.wav"));
    		addSong(new File("assets/music/weightless.wav"));
    		addSong(new File("assets/music/donotgo.wav"));
    		addSong(new File("assets/music/trojans.wav"));
    		addSong(new File("assets/music/handshake.wav"));
    		addSong(new File("assets/music/pointtopoint.wav"));
    	}
    	else{
    		addSong(new File("assets/music/fakebit.wav"));
    		addSong(new File("assets/music/blast.wav"));
    		addSong(new File("assets/music/gameboylove.wav"));
    		addSong(new File("assets/music/godbless.wav"));
    		addSong(new File("assets/music/iwantyou.wav"));
    		addSong(new File("assets/music/meow.wav"));
    		addSong(new File("assets/music/akira.wav"));
    		addSong(new File("assets/music/everythingexplodes.wav"));
    	}
    }
	
	public void addSong(File name){
		songs1.add(name);
	}
	
	public void run(){
		AudioInputStream stream = null;
		for (int i = (int) (Math.random() * songs1.size()); i < songs1.size(); i++){
	        try {
	            // open the audio input stream
	            stream = AudioSystem.getAudioInputStream(songs1.get(i));

	            format = stream.getFormat();

	            // get the audio samples
	            samples = getSamples(stream);
	        }
	        catch (UnsupportedAudioFileException ex) {
	            ex.printStackTrace();
	        }
	        catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        
	        InputStream stream2 = new ByteArrayInputStream(getSamples());
			play(stream2);
			if (i == songs1.size() - 1 && !kill && playing) i = 0;
		}
	}
	
    private byte[] getSamples(AudioInputStream audioStream) {
        // get the number of bytes to read
        int length = (int)(audioStream.getFrameLength() *
            format.getFrameSize());

        // read the entire stream
        byte[] samples = new byte[length];
        DataInputStream is = new DataInputStream(audioStream);
        try {
            is.readFully(samples);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        // return the samples
        return samples;
    }
    
    /**
    Gets the samples of this sound as a byte array.
*/
public byte[] getSamples() {
    return samples;
}
	
    /**
    Plays a stream. This method blocks (doesn't return) until
    the sound is finished playing.
*/
public void play(InputStream source) {

    // use a short, 100ms (1/10th sec) buffer for real-time
    // change to the sound stream
    int bufferSize = format.getFrameSize() *
        Math.round(format.getSampleRate() / 10);
    byte[] buffer = new byte[bufferSize];

    // create a line to play to
    SourceDataLine line;
    try {
        DataLine.Info info =
            new DataLine.Info(SourceDataLine.class, format);
        line = (SourceDataLine)AudioSystem.getLine(info);
        line.open(format, bufferSize);
    }
    catch (LineUnavailableException ex) {
        ex.printStackTrace();
        return;
    }

    // start the line
    line.start();

    int numBytesRead = 0;

    // copy data to the line
    try {
        while (numBytesRead != -1) {
        	if (playing){
        		numBytesRead = source.read(buffer, 0, buffer.length);
        		if (numBytesRead != -1) {
        			line.write(buffer, 0, numBytesRead);
        		}
        	}
        	else if (kill){
        		numBytesRead = -1;
        	} 	
        }
    }
    catch (IOException ex) {
        ex.printStackTrace();
    }

    // wait until all data is played, then close the line
    line.drain();
    line.close();
    
}

public void stopPlaying(){
	playing = false;
}

public void killIt(){
	kill = true;
}

public void continuePlaying(){
	playing = true;
}

}
