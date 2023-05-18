package my.asteroids;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Sound {

    // Counter and total used to track the loading of the sound clips.

	private int clipTotal = 0;
	private int clipsLoaded = 0;

    // Sound clips.

    private Clip crashSound;
	private Clip explosionSound;
	private Clip fireSound;
	private Clip missileSound;
	private Clip saucerSound;
	private Clip thrustersSound;
	private Clip warpSound;


    // ========= LOAD ==========


    public void load(Runnable onSoundLoaded) {
        crashSound = loadSound("crash.wav", onSoundLoaded);
        explosionSound = loadSound("explosion.wav", onSoundLoaded);
        fireSound = loadSound("fire.wav", onSoundLoaded);
        missileSound = loadSound("missile.wav", onSoundLoaded);
        saucerSound = loadSound("saucer.wav", onSoundLoaded);
        thrustersSound = loadSound("thrusters.wav", onSoundLoaded);
        warpSound = loadSound("warp.wav", onSoundLoaded);
	}

    private Clip loadSound(String fileName, Runnable onSoundLoaded){
        Clip clip = getAudioClip(fileName);
        clipTotal++;

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
			InputStream is = Asteroids.class.getResourceAsStream(filename);
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



    // ============ GETTERS ===========

    public int getClipTotal() {
        return clipTotal;
    }

    public int getClipsLoaded() {
        return clipsLoaded;
    }

    

    public Clip getCrashSound() {
        return crashSound;
    }

    public Clip getExplosionSound() {
        return explosionSound;
    }

    public Clip getFireSound() {
        return fireSound;
        
    }

    public Clip getMissileSound() {
        return missileSound;
    }

    public Clip getSaucerSound() {
        return saucerSound;
    }

    public Clip getThrustersSound() {
        return thrustersSound;
    }

    public Clip getWarpSound() {
        return warpSound;
    }



    // ============ OTHER ==========

    public void stopAll(){
        crashSound.stop();
        explosionSound.stop();
        fireSound.stop();
        missileSound.stop();
        saucerSound.stop();
        thrustersSound.stop();
        warpSound.stop();
    }

}
