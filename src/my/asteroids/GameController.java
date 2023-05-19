package my.asteroids;

import my.asteroids.sprite.Asteroid;
import my.asteroids.sprite.Explosion;
import my.asteroids.sprite.FlyingSaucer;
import my.asteroids.sprite.Missile;
import my.asteroids.sprite.Photon;
import my.asteroids.sprite.Ship;

public class GameController {
    private Ship ship = new Ship();
	private Missile missile = new Missile();
	private FlyingSaucer ufo = new FlyingSaucer();

    private Photon[] photons = new Photon[Photon.MAX_SHOTS];
	private Asteroid[] asteroids = new Asteroid[Asteroid.MAX_ROCKS];
	private Explosion[] explosions = new Explosion[Explosion.MAX_SCRAP];
    
    // Explosion data.
	private int[] explosionCounter = new int[Explosion.MAX_SCRAP]; // Time counters for explosions.
	private int explosionIndex; // Next available explosion sprite.

    private boolean detail;
	private boolean paused;
	private boolean playing;

    private boolean thrustFwd;
    private boolean thrustRev;

    private int score;
	private int highScore;

	private int shipsLeft; // Number of ships left in game, including current one.


    public GameController(){
        for (int i = 0; i < Photon.MAX_SHOTS; i++)
        photons[i] = new Photon();
    
        for (int i = 0; i < Asteroid.MAX_ROCKS; i++)
            asteroids[i] = new Asteroid();

        for (int i = 0; i < Explosion.MAX_SCRAP; i++)
            explosions[i] = new Explosion();
    }


    public Ship getShip(){
        return ship;
    }
    public Missile getMissile() {
        return missile;
    }
    public FlyingSaucer getUfo() {
        return ufo;
    }

    public Photon[] getPhotons() {
        return photons;
    }
    public Asteroid[] getAsteroids() {
        return asteroids;
    }
    public Explosion[] getExplosions() {
        return explosions;
    }

    public Photon getPhoton(int i){
        return photons[i];
    }
    public Explosion getExplosion(int i){
        return explosions[i];
    }
    public Asteroid getAsteroid(int i){
        return asteroids[i];
    }



    public void setDetail(boolean detail){
        this.detail = detail;
    }
    public void toggleDetail(){
        detail = !detail;
    }
    public boolean isDetail(){
        return detail;
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }
    public boolean isPaused(){
        return paused;
    }

    public boolean isPlaying() {
        return playing;
    }
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
    

    public int getExplosionCounterAt(int i){
        return explosionCounter[i];
    }
    public void setExplosionCounterAt(int i, int val){
        explosionCounter[i] = val;
    }
    public void setExplosionIndex(int i){
        explosionIndex = i;
    }
    public int getExplosionIndex() {
        return explosionIndex;
    }

    
    public boolean isThrustFwd() {
        return thrustFwd;
    }
    public void setThrustFwd(boolean thrustFwd) {
        this.thrustFwd = thrustFwd;
    }

    public boolean isThrustRev() {
        return thrustRev;
    }
    public void setThrustRev(boolean thrustRev) {
        this.thrustRev = thrustRev;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getHighScore() {
        return highScore;
    }
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    public void incScore(int delta){
        score += delta;
    }

    public int getShipsLeft() {
        return shipsLeft;
    }
    public void setShipsLeft(int shipsLeft) {
        this.shipsLeft = shipsLeft;
    }
}
