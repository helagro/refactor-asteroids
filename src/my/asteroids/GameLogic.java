package my.asteroids;

/*******************************************************************************
  Based on the Asteroids applet by Mike Hall. Please see http://www.brainjar.com
  for terms of use.
  Migrated to modern Java versions (12+) by Daniel Strüber.

  Keyboard Controls:

  S            - Start Game    P           - Pause Game
  Cursor Left  - Rotate Left   Cursor Up   - Fire Thrusters
  Cursor Right - Rotate Right  Cursor Down - Fire Retro Thrusters
  Spacebar     - Fire Cannon   H           - Hyperspace
  M            - Toggle Sound  D           - Toggle Graphics Detail

*******************************************************************************/

import javax.sound.sampled.Clip;
import my.asteroids.sprite.Asteroid;
import my.asteroids.sprite.Explosion;
import my.asteroids.sprite.FlyingSaucer;
import my.asteroids.sprite.Photon;
import my.asteroids.sprite.Ship;
import my.asteroids.sprite.GameView;

public class GameLogic implements Runnable, KSCListener{
	private static final long serialVersionUID = 1L;

	// Thread control variables.

	Thread loadThread;
	Thread loopThread;

	// Constants

	static final int DELAY = 20; // Milliseconds between screen and
	public static final int FPS = // the resulting frame rate.
			Math.round(1000 / DELAY);

	static final int STORM_PAUSE = 2 * FPS;

	static final int BIG_POINTS = 25; // Points scored for shooting
	static final int SMALL_POINTS = 50; // various objects.
	static final int Missile_POINTS = 500;

	// Number of points the must be scored to earn a new ship or to cause the
	// flying saucer to appear.

	static final int NEW_SHIP_POINTS = 5000;


	// Game data.

	int newShipScore;
	int newUfoScore;

	// Ship data.

	int shipCounter; // Timer counter for ship explosion.

	// Photon data.

	int photonIndex; // Index to next available photon sprite.
	long photonTime; // Time value used to keep firing rate constant.

	// Asteroid data.

	int asteroidsCounter; // Break-time counter.
	double asteroidsSpeed; // Asteroid speed.
	int asteroidsLeft; // Number of active asteroids.


	// Flags for looping sound clips.
	boolean thrustersPlaying;


	// My stuff
	SoundController sound;
	KSController kController;

	GameView gameView;
	GameController gc;


	public GameLogic(KSController kController, GameController gcects, GameView gameView, SoundController sound){
		this.kController = kController;
		this.gc = gcects;
		this.gameView = gameView;
		this.sound = sound;

		init();
	}

	private void init() {
		// Initialize game data and put us in 'game over' mode.
		gc.setHighScore(0);
		gc.setDetail(true);
		initGame();
		endGame();

		gameView.init();
	}

	public void initGame() {

		// Initialize game data and sprites.
		gc.setScore(0);
		gc.setShipsLeft(Ship.MAX_SHIPS);
		asteroidsSpeed = Asteroid.MIN_ROCK_SPEED;
		newShipScore = NEW_SHIP_POINTS;
		newUfoScore = FlyingSaucer.NEW_UFO_POINTS;
		photonIndex = 0;
		initShip(true);

		gc.getUfo().stop();
		gc.getMissile().stop();

		initAsteroids();
		initExplosions();
		gc.setPlaying(true);
		gc.setPaused(false);
		photonTime = System.currentTimeMillis();
	}

	public void endGame() {
		// Stop ship, flying saucer, guided missile and associated sounds.

		gc.setPlaying(false);
		stopShip();
		gc.getUfo().stop();
		gc.getMissile().stop();
	}


	public void start() {

		if (loopThread == null) {
			loopThread = new Thread(this);
			loopThread.start();
		}
		if (!sound.isLoaded() && loadThread == null) {
			loadThread = new Thread(() -> {
				sound.load(()->{
					onSoundLoaded();
				});
			});
			loadThread.start();
		}
	}

	public void stop() {
		if (loopThread != null) {
			loopThread.stop();
			loopThread = null;
		}
		if (loadThread != null) {
			loadThread.stop();
			loadThread = null;
		}
	}

	public void run() {
		long startTime;

		// Lower this thread's priority and get the current time.

		startTime = System.currentTimeMillis();


		// This is the main loop.

		while (Thread.currentThread() == loopThread) {
			if (!gc.isPaused()) {

				// Move and process all sprites.

				updateShip();
				updatePhotons();
				updateUfo();
				updateMissile();
				updateAsteroids();
				updateExplosions();

				// Check the score and advance high score, add a new ship or start the
				// flying saucer as necessary.

				if (gc.getScore() > gc.getHighScore())
					gc.setHighScore(gc.getScore());
				if (gc.getScore() > newShipScore) {
					newShipScore += NEW_SHIP_POINTS;
					gc.setShipsLeft(gc.getShipsLeft() +1);
				}
				if (gc.isPlaying() && gc.getScore() > newUfoScore && !gc.getUfo().active) {
					newUfoScore += FlyingSaucer.NEW_UFO_POINTS;
					gc.getUfo().init();
				}

				// If all asteroids have been destroyed create a new batch.

				if (asteroidsLeft <= 0 && --asteroidsCounter <= 0)
					initAsteroids();
			}

			// Update the screen and set the timer for the next loop.

			gameView.repaint();
			try {
				startTime += DELAY;
				Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				break;
			}
		}
	}


	public void onSoundLoaded(){
		try {
			gameView.repaint();
			Thread.currentThread().sleep(DELAY);
		} catch (InterruptedException e) {
		}

	}


	public void initShip(boolean firstTime) {
		gc.getShip().init(firstTime);

		sound.stop(sound.getThrustersSound());
		thrustersPlaying = false;
	}

	public void updateShip() {
		if (!gc.isPlaying())
			return;

		if (kController.isLeft()) gc.getShip().rotateLeft();
		if (kController.isRight()) gc.getShip().rotateRight();

		if (kController.isUp()) gc.getShip().moveForward();
		if (kController.isDown()) gc.getShip().moveBackward();

		// Move the gcects.getShip(). If it is currently in hyperspace, advance the countdown.

		if (gc.getShip().active) {
			gc.getShip().advance();
			gc.getShip().render();
		}

		// Ship is exploding, advance the countdown or create a new ship if it is
		// done exploding. The new ship is added as though it were in hyperspace.
		// (This gives the player time to move the ship if it is in imminent
		// danger.) If that was the last ship, end the game.

		else if (--shipCounter <= 0)
			if (gc.getShipsLeft() > 0) {
				initShip(false);
			} else
				endGame();
	}

	public void stopShip() {
		gc.getShip().active = false;
		shipCounter = Explosion.SCRAP_COUNT;
		if (gc.getShipsLeft() > 0)
			gc.setShipsLeft(gc.getShipsLeft() -1);
		sound.stop(sound.getThrustersSound());
		thrustersPlaying = false;
	}

	public void updatePhotons() {
		for(Photon photon : gc.getPhotons())
			photon.advance();
	}

	public void updateUfo() {

		// Move the flying saucer and check for collision with a photon. Stop it
		// when its counter has expired.

		boolean collided = Photon.handleCollision(gc.getPhotons(), gc.getUfo());
		if(collided){
			gameView.explode(gc.getUfo());
			gc.getUfo().stop();
			gc.incScore(FlyingSaucer.POINTS);
		}

		if (gc.getUfo().active)
			gc.getUfo().update(gc.getShip(), gc.getMissile());

	}

	public void updateMissile() {
		if(!gc.getMissile().active) return;

		gc.getMissile().update();
		
		boolean notHyper = !gc.getShip().isHyperSpace();

		gc.getMissile().guide(gc.getShip(), notHyper);

		if(gc.getMissile().collidedWith(gc.getShip())){
			sound.play(sound.getCrashSound(), 1);
			gameView.explode(gc.getShip());
			stopShip();
			gc.getUfo().stop();
			gc.getMissile().stop();
		}

		for (int i = 0; i < Photon.MAX_SHOTS; i++)
			if (gc.getPhoton(i).active && gc.getMissile().isColliding(gc.getPhoton(i))) {
				sound.play(sound.getCrashSound(), 1);
				gameView.explode(gc.getMissile());
				gc.getMissile().stop();
				gc.incScore(Missile_POINTS);
			}
	}


	public void initAsteroids() {
		for (Asteroid asteroid : gc.getAsteroids())
			asteroid.init(asteroidsSpeed);

		asteroidsCounter = STORM_PAUSE;
		asteroidsLeft = Asteroid.MAX_ROCKS;
		if (asteroidsSpeed < Asteroid.MAX_ROCK_SPEED)
			asteroidsSpeed += 0.5;
	}

	public void initSmallAsteroids(int n) {
		// Create one or two smaller asteroids from a larger one using inactive
		// asteroids. The new asteroids will be placed in the same position as the
		// old one but will have a new, smaller shape and new, randomly generated
		// movements.

		int count = 0;
		int i = 0;
		double x = gc.getAsteroid(n).x;
		double y = gc.getAsteroid(n).y;
		do {
			if (!gc.getAsteroid(i).active) {
				gc.getAsteroid(i).shrink(x, y, asteroidsSpeed);

				count++;
				asteroidsLeft++;
			}
			i++;
		} while (i < Asteroid.MAX_ROCKS && count < 2);
	}

	public void updateAsteroids() {

		int i;

		// Move any active asteroids and check for collisions.

		for (i = 0; i < Asteroid.MAX_ROCKS; i++){
			if (gc.getAsteroid(i).active) {
				gc.getAsteroid(i).advance();
				gc.getAsteroid(i).render();

				// If hit by photon, kill asteroid and advance score. If asteroid is
				// large, make some smaller ones to replace it.

				boolean isColliding = Photon.handleCollision(gc.getPhotons(), gc.getAsteroid(i));
				if(isColliding){
					asteroidsLeft--;
					gameView.explode(gc.getAsteroid(i));

					if (!gc.getAsteroid(i).isSmall()) {
						gc.incScore(BIG_POINTS);
						initSmallAsteroids(i);
					} else {
						gc.incScore(SMALL_POINTS);
					}
				}


				// If the ship is not in hyperspace, see if it is hit.

				// if (gcects.getShip().active && hyperCounter <= 0 && gc.getAsteroid(i).active && gc.getAsteroid(i).isColliding(ship)) {
				// 	if (doSound)
				// 		sound.getCrashSound().loop(1);
				// 	gameView.explode(ship);
				// 	stopShip();
				// 	ufo.stop();
				// 	stopMissile();
				// }
			}
		}
	}

	public void initExplosions() {
		int i;

		for (i = 0; i < Explosion.MAX_SCRAP; i++) {
			gc.getExplosion(i).init();
			gc.setExplosionCounterAt(i, 0);
		}

		gc.setExplosionIndex(0);
	}



	public void updateExplosions() {
		// Move any active explosion debris. Stop explosion when its counter has
		// expired.

		for (int i = 0; i < Explosion.MAX_SCRAP; i++)
			if (gc.getExplosion(i).active) {
				gc.getExplosion(i).advance();
				gc.getExplosion(i).render();

				int explosionCounter = gc.getExplosionCounterAt(i);
				explosionCounter--;
				gc.setExplosionCounterAt(i, explosionCounter);

				if (explosionCounter < 0)
					gc.getExplosion(i).active = false;
			}
	}

	@Override
	public void onArrowKeyDown() {
		if (kController.hasVertical() && gc.getShip().active && !thrustersPlaying) {
			sound.play(sound.getThrustersSound(), Clip.LOOP_CONTINUOUSLY);
			thrustersPlaying = true;
		}

		if(kController.isUp()) gc.setThrustFwd(true);
		if(kController.isDown()) gc.setThrustRev(true);
	}


	@Override
	public void onArrowKeyUp() {
		if (!kController.hasVertical() && thrustersPlaying) {
			sound.getThrustersSound().stop();
			thrustersPlaying = false;
		}

		if(kController.isUp()) gc.setThrustFwd(false);
		if(kController.isDown()) gc.setThrustRev(false);
	}


	@Override
	public void onChar(char character) {
		switch (character){
			case ' ':
				if(gc.getShip().active){
					sound.play(sound.getFireSound(), 1);
					photonTime = System.currentTimeMillis();
					photonIndex = (photonIndex +1) % Photon.MAX_SHOTS;
					gc.getPhoton(photonIndex).launch(gc.getShip());
				}
				break;
			case 'h': //Hyperspace
				if(gc.getShip().active && !gc.getShip().isHyperSpace()){
					gc.getShip().teleportRandom();
					gc.getShip().enterHyperSpace();
		
					sound.play(sound.getWarpSound(), 1);
				}
				break;
			case 'p': //Pause
				sound.toggleMute(false);
				if(!sound.isMuted()) resumeLooping();
				gc.setPaused(!gc.isPaused());
				break;
			case 'm': //Mute
				sound.toggleMute(true);
				if(!sound.isMuted()) resumeLooping();
				break;
			case 'd': //graphic Details
				gc.toggleDetail();
				break;
			case 's': // Start
				if(sound.isLoaded() && !gc.isPlaying()) initGame();
				break;
		}
	}

	private void resumeLooping(){
		if (gc.getMissile().active)
			sound.play(sound.getMissileSound(), Clip.LOOP_CONTINUOUSLY);
		if (gc.getUfo().active)
			sound.play(sound.getSaucerSound(), Clip.LOOP_CONTINUOUSLY);
		if (thrustersPlaying)
			sound.play(sound.getThrustersSound(), Clip.LOOP_CONTINUOUSLY);
	}


}
