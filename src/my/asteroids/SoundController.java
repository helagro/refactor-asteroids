package my.asteroids;

import javax.sound.sampled.Clip;


public class SoundController {

    // Counter and total used to track the loading of the sound clips.
    public final int CLIP_TOTAL = 7;

    SoundLoader soundLoader = new SoundLoader();
    private boolean isLoaded = false;
    private boolean isMuted = false;

    // Sound clips.

    private Clip crashSound;
	private Clip explosionSound;
	private Clip fireSound;
	private Clip missileSound;
	private Clip saucerSound;
	private Clip thrustersSound;
	private Clip warpSound;



    // ========= SINGLETON =========

    private static SoundController instance;

    public static SoundController getInstance(){
        if(instance == null) 
            instance = new SoundController();
        return instance;
    }

    private SoundController(){
    }


    // ========= LOAD ==========

    public void load(Runnable onSoundLoaded) {
        crashSound = soundLoader.load("crash.wav", onSoundLoaded);
        explosionSound = soundLoader.load("explosion.wav", onSoundLoaded);
        fireSound = soundLoader.load("fire.wav", onSoundLoaded);
        missileSound = soundLoader.load("missile.wav", onSoundLoaded);
        saucerSound = soundLoader.load("saucer.wav", onSoundLoaded);
        thrustersSound = soundLoader.load("thrusters.wav", onSoundLoaded);
        warpSound = soundLoader.load("warp.wav", onSoundLoaded);

        isLoaded = true;
	}



    public int getClipsLoaded() {
        return soundLoader.getClipsLoaded();
    }

    public boolean isLoaded(){
        return isLoaded;
    }

    public boolean isMuted(){
        return isMuted;
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



    // ========= PLAY / PAUSE =========

    public void stopAll(){
        if(!isLoaded) return;

        crashSound.stop();
        explosionSound.stop();
        fireSound.stop();
        missileSound.stop();
        saucerSound.stop();
        thrustersSound.stop();
        warpSound.stop();
    }

    public void stopLooping(){
        if(!isLoaded) return;

        missileSound.stop();
        saucerSound.stop();
        thrustersSound.stop();
    }

    public void play(Clip clip, int loopOpt){
        if(isLoaded && !isMuted)
            clip.loop(loopOpt);
    }

    public void stop(Clip clip){
        if(isLoaded)
            clip.stop();
    }



    // ============ OTHER ==========

    public void toggleMute(boolean stopAll){
        isMuted = !isMuted;

        if(isMuted){
            if(stopAll)
                stopAll();
            else
                stopLooping();
        }
    }

}
