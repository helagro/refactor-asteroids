package my.asteroids;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundLoader {
    private static final String FOLDER = "sound/";

	private int clipsLoaded = 0;

    public int getClipsLoaded() {
        return clipsLoaded;
    }


    public Clip load(String fileName, Runnable onSoundLoaded){
        Clip clip = getAudioClip(fileName);

        clip.start();
        clip.stop();

        clipsLoaded++;
        onSoundLoaded.run();
        
        return clip;
    }

    private Clip getAudioClip(String filename) {
		if (filename == null)
			throw new IllegalArgumentException();
		// code adapted from:
		// http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
		try {
			Clip clip = AudioSystem.getClip();
			InputStream is = GameLogic.class.getResourceAsStream(FOLDER + filename);
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			clip.open(ais);
			return clip;
		} catch (UnsupportedAudioFileException e) {
			throw new IllegalArgumentException("unsupported audio format: '" + filename + "'", e);
		} catch (LineUnavailableException e) {
			throw new IllegalArgumentException("could not play '" + filename + "'", e);
		} catch (IOException e) {
			throw new IllegalArgumentException("could not play '" + filename + "'", e);
		}
	}
}
