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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import javax.sound.sampled.Clip;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import my.asteroids.sprite.Asteroid;
import my.asteroids.sprite.AsteroidsSprite;
import my.asteroids.sprite.Explosion;
import my.asteroids.sprite.FlyingSaucer;
import my.asteroids.sprite.Missile;
import my.asteroids.sprite.Photon;
import my.asteroids.sprite.Ship;

public class Asteroids extends Panel implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	// Copyright information.

	String copyName = "Asteroids";
	String copyVers = "Version 1.3";
	String copyInfo = "Copyright 1998-2001 by Mike Hall";
	String copyLink = "http://www.brainjar.com";
	String copyText = copyName + '\n' + copyVers + '\n' + copyInfo + '\n' + copyLink;

	// Thread control variables.

	Thread loadThread;
	Thread loopThread;

	// Constants

	static final int DELAY = 20; // Milliseconds between screen and
	public static final int FPS = // the resulting frame rate.
			Math.round(1000 / DELAY);

	static final int MAX_SHOTS = 80; // Maximum number of sprites TODO: 8
	static final int MAX_ROCKS = 8; // for photons, asteroids and
	static final int MAX_SCRAP = 40; // explosions.

	static final int SCRAP_COUNT = 2 * FPS; // Timer counter starting values
	static final int STORM_PAUSE = 2 * FPS;


	static final int MAX_SHIPS = 3; // Starting number of ships for
									// each game.


	// Ship's rotation and acceleration rates and maximum speed.

	static final int FIRE_DELAY = 10; // Minimum number of milliseconds TODO: 50
										// required between photon shots.

	// Probablility of flying saucer firing a missile during any given frame
	// (other conditions must be met).


	static final int BIG_POINTS = 25; // Points scored for shooting
	static final int SMALL_POINTS = 50; // various objects.
	static final int Missile_POINTS = 500;

	// Number of points the must be scored to earn a new ship or to cause the
	// flying saucer to appear.

	static final int NEW_SHIP_POINTS = 5000;
	static final int NEW_UFO_POINTS = 275; //TODO : 2570

	// Background stars.

	int numStars;
	Point[] stars;

	// Game data.

	int score;
	int highScore;
	int newShipScore;
	int newUfoScore;

	// Flags for game state and options.

	boolean loaded = false;
	boolean paused;
	boolean playing;
	boolean detail;

	// Key flags.

	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;

	// Sprite objects.

	Ship ship;
	Missile missile;

	FlyingSaucer ufo;
	Photon[] photons = new Photon[MAX_SHOTS];
	Asteroid[] asteroids = new Asteroid[MAX_ROCKS];
	Explosion[] explosions = new Explosion[MAX_SCRAP];

	// Ship data.

	int shipsLeft; // Number of ships left in game, including current one.
	int shipCounter; // Timer counter for ship explosion.

	// Photon data.

	int photonIndex; // Index to next available photon sprite.
	long photonTime; // Time value used to keep firing rate constant.


	// Asteroid data.

	int asteroidsCounter; // Break-time counter.
	double asteroidsSpeed; // Asteroid speed.
	int asteroidsLeft; // Number of active asteroids.

	// Explosion data.

	int[] explosionCounter = new int[MAX_SCRAP]; // Time counters for explosions.
	int explosionIndex; // Next available explosion sprite.


	// Flags for looping sound clips.

	boolean thrustersPlaying;


	// Off screen image.

	Dimension offDimension;
	Image offImage;
	Graphics offGraphics;

	// Data for the screen font.

	Font font = new Font("Helvetica", Font.BOLD, 12);
	FontMetrics fm = getFontMetrics(font);
	int fontWidth = fm.getMaxAdvance();
	int fontHeight = fm.getHeight();


	// My stuff

	Sound sound = Sound.getInstance();


	public String getAppletInfo() {

		// Return copyright information.

		return (copyText);
	}

	public static void main(String[] args) {
		Frame f = new Frame();
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});

		Asteroids ut = new Asteroids();
		ut.setSize(700, 400);
		f.add(ut);
		f.pack();
		ut.init();
		f.setSize(700, 400 + 20); // add 20, seems enough for the Frame title,
		f.show();
		ut.start();
	}

	public void init() {

		Dimension d = new Dimension(700, 400);
		int i;

		// Display copyright information.

		System.out.println(copyText);

		// Set up key event handling and set focus to applet window.

		addKeyListener(this);
		requestFocus();

		// Save the screen size.

		AsteroidsSprite.width = d.width;
		AsteroidsSprite.height = d.height;

		// Generate the starry background.

		numStars = AsteroidsSprite.width * AsteroidsSprite.height / 5000;
		stars = new Point[numStars];
		for (i = 0; i < numStars; i++)
			stars[i] = new Point((int) (Math.random() * AsteroidsSprite.width),
					(int) (Math.random() * AsteroidsSprite.height));



		ship = new Ship();
		missile = new Missile();

		for (i = 0; i < MAX_SHOTS; i++)
			photons[i] = new Photon();
		
		ufo = new FlyingSaucer();


		for (i = 0; i < MAX_ROCKS; i++)
			asteroids[i] = new Asteroid();

		for (i = 0; i < MAX_SCRAP; i++)
			explosions[i] = new Explosion();

		// Initialize game data and put us in 'game over' mode.

		highScore = 0;
		detail = true;
		initGame();
		endGame();
	}

	public void initGame() {

		// Initialize game data and sprites.

		score = 0;
		shipsLeft = MAX_SHIPS;
		asteroidsSpeed = Asteroid.MIN_ROCK_SPEED;
		newShipScore = NEW_SHIP_POINTS;
		newUfoScore = NEW_UFO_POINTS;
		initShip(true);
		initPhotons();

		ufo.stop();
		missile.stop();

		initAsteroids();
		initExplosions();
		playing = true;
		paused = false;
		photonTime = System.currentTimeMillis();
	}

	public void endGame() {

		// Stop ship, flying saucer, guided missile and associated sounds.

		playing = false;
		stopShip();
		ufo.stop();
		missile.stop();
	}

	public void start() {

		if (loopThread == null) {
			loopThread = new Thread(this);
			loopThread.start();
		}
		if (!loaded && loadThread == null) {
			loadThread = new Thread(() -> {
				sound.load(()->{
					onSoundLoaded();
				});
				loaded = true;
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
			if (!paused) {

				// Move and process all sprites.

				updateShip();
				updatePhotons();
				updateUfo();
				updateMissile();
				updateAsteroids();
				updateExplosions();

				// Check the score and advance high score, add a new ship or start the
				// flying saucer as necessary.

				if (score > highScore)
					highScore = score;
				if (score > newShipScore) {
					newShipScore += NEW_SHIP_POINTS;
					shipsLeft++;
				}
				if (playing && score > newUfoScore && !ufo.active) {
					newUfoScore += NEW_UFO_POINTS;
					ufo.init();
				}

				// If all asteroids have been destroyed create a new batch.

				if (asteroidsLeft <= 0 && --asteroidsCounter <= 0)
					initAsteroids();
			}

			// Update the screen and set the timer for the next loop.

			repaint();
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
			repaint();
			Thread.currentThread().sleep(DELAY);
		} catch (InterruptedException e) {
		}

	}


	public void initShip(boolean firstTime) {
		ship.init(firstTime);

		if (loaded)
			sound.getThrustersSound().stop();
		thrustersPlaying = false;
	}

	public void updateShip() {
		if (!playing)
			return;

		if (left) ship.rotateLeft();
		if (right) ship.rotateRight();

		if (up) ship.moveForward();
		if (down) ship.moveBackward();

		// Move the ship. If it is currently in hyperspace, advance the countdown.

		if (ship.active) {
			ship.advance();
			ship.render();
		}

		// Ship is exploding, advance the countdown or create a new ship if it is
		// done exploding. The new ship is added as though it were in hyperspace.
		// (This gives the player time to move the ship if it is in imminent
		// danger.) If that was the last ship, end the game.

		else if (--shipCounter <= 0)
			if (shipsLeft > 0) {
				initShip(false);
			} else
				endGame();
	}

	public void stopShip() {
		ship.active = false;
		shipCounter = SCRAP_COUNT;
		if (shipsLeft > 0)
			shipsLeft--;
		if (loaded)
			sound.getThrustersSound().stop();
		thrustersPlaying = false;
	}

	public void initPhotons() {
		int i;

		for (i = 0; i < MAX_SHOTS; i++)
			photons[i].active = false;
		photonIndex = 0;
	}

	public void updatePhotons() {

		int i;

		// Move any active photons. Stop it when its counter has expired.

		for (i = 0; i < MAX_SHOTS; i++)
			if (photons[i].active) {
				if (!photons[i].advance())
					photons[i].render();
				else
					photons[i].active = false;
			}
	}

	public void updateUfo() {

		// Move the flying saucer and check for collision with a photon. Stop it
		// when its counter has expired.

		for (int i = 0; i < MAX_SHOTS; i++){
			if (photons[i].active && ufo.isColliding(photons[i])) {
				sound.play(sound.getCrashSound(), 1);
				explode(ufo);
				ufo.stop();
				score += FlyingSaucer.POINTS;
			}
		}


		if (!ufo.active)
			return;

		ufo.update();

		boolean notHyperSpace = !ship.isHyperSpace();
		boolean shouldFireMissile = ufo.shouldFireMissile(ship, notHyperSpace, missile);

		if(shouldFireMissile){
			ufo.fireMissile(missile);
		}
	}

	public void updateMissile() {
		if(!missile.active) return;

		missile.update();
		
		boolean notHyper = !ship.isHyperSpace();

		missile.guide(ship, notHyper);

		if(missile.collidedWith(ship)){
			sound.play(sound.getCrashSound(), 1);
			explode(ship);
			stopShip();
			ufo.stop();
			missile.stop();
		}

		for (int i = 0; i < MAX_SHOTS; i++)
			if (photons[i].active && missile.isColliding(photons[i])) {
				sound.play(sound.getCrashSound(), 1);
				explode(missile);
				missile.stop();
				score += Missile_POINTS;
			}
		
	}


	public void initAsteroids() {
		// Create random shapes, positions and movements for each asteroid.

		for (int i = 0; i < MAX_ROCKS; i++){
			asteroids[i].init(asteroidsSpeed);
		}

		asteroidsCounter = STORM_PAUSE;
		asteroidsLeft = MAX_ROCKS;
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
		double x = asteroids[n].x;
		double y = asteroids[n].y;
		do {
			if (!asteroids[i].active) {
				asteroids[i].shrink(x, y, asteroidsSpeed);

				count++;
				asteroidsLeft++;
			}
			i++;
		} while (i < MAX_ROCKS && count < 2);
	}

	public void updateAsteroids() {

		int i, j;

		// Move any active asteroids and check for collisions.

		for (i = 0; i < MAX_ROCKS; i++)
			if (asteroids[i].active) {
				asteroids[i].advance();
				asteroids[i].render();

				// If hit by photon, kill asteroid and advance score. If asteroid is
				// large, make some smaller ones to replace it.

				for (j = 0; j < MAX_SHOTS; j++)
					if (photons[j].active && asteroids[i].active && asteroids[i].isColliding(photons[j])) {
						asteroidsLeft--;
						asteroids[i].active = false;
						photons[j].active = false;
						sound.play(sound.getExplosionSound(), 1);
						explode(asteroids[i]);
						if (!asteroids[i].isSmall()) {
							score += BIG_POINTS;
							initSmallAsteroids(i);
						} else
							score += SMALL_POINTS;
					}

				// If the ship is not in hyperspace, see if it is hit.

				// if (ship.active && hyperCounter <= 0 && asteroids[i].active && asteroids[i].isColliding(ship)) {
				// 	if (doSound)
				// 		sound.getCrashSound().loop(1);
				// 	explode(ship);
				// 	stopShip();
				// 	ufo.stop();
				// 	stopMissile();
				// }
			}
	}

	public void initExplosions() {

		int i;

		for (i = 0; i < MAX_SCRAP; i++) {
			explosions[i].init();
			explosionCounter[i] = 0;
		}
		explosionIndex = 0;
	}

	public void explode(AsteroidsSprite s) {

		// Create sprites for explosion animation. The each individual line segment
		// of the given sprite is used to create a new sprite that will move
		// outward from the sprite's original position with a random rotation.

		s.render();
		int c = (detail || s.sprite.npoints < 6) ? 1 : 2;

		for (int i = 0; i < s.sprite.npoints; i += c) {
			explosionIndex++;
			if (explosionIndex >= MAX_SCRAP)
				explosionIndex = 0;

			int j = i + 1;
			if (j >= s.sprite.npoints)
				j -= s.sprite.npoints;


			explosions[explosionIndex].explode(s, i, j);
			explosionCounter[explosionIndex] = SCRAP_COUNT;
		}
	}

	public void updateExplosions() {

		int i;

		// Move any active explosion debris. Stop explosion when its counter has
		// expired.

		for (i = 0; i < MAX_SCRAP; i++)
			if (explosions[i].active) {
				explosions[i].advance();
				explosions[i].render();
				if (--explosionCounter[i] < 0)
					explosions[i].active = false;
			}
	}

	public void keyPressed(KeyEvent e) {
		char c;

		// Check if any cursor keys have been pressed and set flags.

		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			right = true;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = true;

		if ((up || down) && ship.active && !thrustersPlaying) {
			if (!paused)
				sound.play(sound.getThrustersSound(), Clip.LOOP_CONTINUOUSLY);
			thrustersPlaying = true;
		}

		// Spacebar: fire a photon and start its counter.

		if (e.getKeyChar() == ' ' && ship.active) {
			if (!paused)
				sound.play(sound.getFireSound(), 1);

			photonTime = System.currentTimeMillis();
			photonIndex++;
			if (photonIndex >= MAX_SHOTS)
				photonIndex = 0;

			photons[photonIndex].launch(ship);
		}

		// Allow upper or lower case characters for remaining keys.

		c = Character.toLowerCase(e.getKeyChar());

		// 'H' key: warp ship into hyperspace by moving to a random location and
		// starting counter.

		if (c == 'h' && ship.active && !ship.isHyperSpace()) {
			ship.x = Math.random() * AsteroidsSprite.width;
			ship.y = Math.random() * AsteroidsSprite.height;
			ship.enterHyperSpace();
			if (!paused)
				sound.play(sound.getWarpSound(), 1);
		}

		// 'P' key: toggle pause mode and start or stop any active looping sound
		// clips.

		if (c == 'p') {
			sound.toggleMute(false);
			if(!sound.isMuted()){
				resumeLooping();
			}

			paused = !paused;
		}

		// 'M' key: toggle sound on or off and stop any looping sound clips.

		if (c == 'm') {
			sound.toggleMute(true);

			if(!sound.isMuted()){
				resumeLooping();
			}
		}

		// 'D' key: toggle graphics detail on or off.
		if (c == 'd')
			detail = !detail;

		// 'S' key: start the game, if not already in progress.
		if (c == 's' && loaded && !playing)
			initGame();

	}

	public void keyReleased(KeyEvent e) {

		// Check if any cursor keys where released and set flags.

		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			right = false;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = false;

		if (!up && !down && thrustersPlaying) {
			sound.getThrustersSound().stop();
			thrustersPlaying = false;
		}
	}

	private void resumeLooping(){
		if (missile.active)
			sound.play(sound.getMissileSound(), Clip.LOOP_CONTINUOUSLY);
		if (ufo.active)
			sound.play(sound.getSaucerSound(), Clip.LOOP_CONTINUOUSLY);
		if (thrustersPlaying)
			sound.play(sound.getThrustersSound(), Clip.LOOP_CONTINUOUSLY);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {

		Dimension d = getSize();
		int i;
		int c;
		String s;
		int w, h;
		int x, y;

		// Create the off screen graphics context, if no good one exists.

		if (offGraphics == null || d.width != offDimension.width || d.height != offDimension.height) {
			offDimension = d;
			offImage = createImage(d.width, d.height);
			offGraphics = offImage.getGraphics();
		}

		// Fill in background and stars.

		offGraphics.setColor(Color.black);
		offGraphics.fillRect(0, 0, d.width, d.height);
		if (detail) {
			offGraphics.setColor(Color.white);
			for (i = 0; i < numStars; i++)
				offGraphics.drawLine(stars[i].x, stars[i].y, stars[i].x, stars[i].y);
		}

		// Draw photon bullets.

		offGraphics.setColor(Color.white);
		for (i = 0; i < MAX_SHOTS; i++)
			if (photons[i].active)
				photons[i].draw(offGraphics);

		// Draw the guided missile, counter is used to quickly fade color to black
		// when near expiration.

		c = Math.min(missile.getMissileCounter() * 24, 255);
		offGraphics.setColor(new Color(c, c, c));
		if (missile.active) {
			offGraphics.drawPolygon(missile.sprite);
			offGraphics.drawLine(missile.sprite.xpoints[missile.sprite.npoints - 1],
					missile.sprite.ypoints[missile.sprite.npoints - 1], missile.sprite.xpoints[0],
					missile.sprite.ypoints[0]);
		}

		// Draw the asteroids.

		for (i = 0; i < MAX_ROCKS; i++)
			if (asteroids[i].active)
				asteroids[i].draw(detail, offGraphics);

		if (ufo.active)
			ufo.draw(offGraphics, detail);

			
		// Draw the ship, counter is used to fade color to white on hyperspace.
		c = 255 - (255 / Ship.HYPER_COUNT) * ship.getHyperSpaceCounter();
		if (ship.active) {

			boolean fill = detail && !ship.isHyperSpace();

			boolean canDrawThrustor = !paused && detail;
			boolean drawFwdThrustor = up && canDrawThrustor;
			boolean drawRevThrustor = down && canDrawThrustor;

			ship.draw(offGraphics, fill, drawFwdThrustor, drawRevThrustor, c);
		}

		// Draw any explosion debris, counters are used to fade color to black.

		for (i = 0; i < MAX_SCRAP; i++)
			if (explosions[i].active) {
				c = (255 / SCRAP_COUNT) * explosionCounter[i];
				offGraphics.setColor(new Color(c, c, c));
				offGraphics.drawPolygon(explosions[i].sprite);
			}

		// Display status and messages.

		offGraphics.setFont(font);
		offGraphics.setColor(Color.white);

		offGraphics.drawString("Score: " + score, fontWidth, fontHeight);
		offGraphics.drawString("Ships: " + shipsLeft, fontWidth, d.height - fontHeight);
		s = "High: " + highScore;
		offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), fontHeight);
		if (sound.isMuted()) {
			s = "Mute";
			offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), d.height - fontHeight);
		}

		if (!playing) {
			s = copyName;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - 2 * fontHeight);
			s = copyVers;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - fontHeight);
			s = copyInfo;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + fontHeight);
			s = copyLink;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + 2 * fontHeight);
			if (!loaded) {
				s = "Loading sounds...";
				w = 4 * fontWidth + fm.stringWidth(s);
				h = fontHeight;
				x = (d.width - w) / 2;
				y = 3 * d.height / 4 - fm.getMaxAscent();
				offGraphics.setColor(Color.black);
				offGraphics.fillRect(x, y, w, h);
				offGraphics.setColor(Color.gray);
				if (sound.getClipTotal() > 0)
					offGraphics.fillRect(x, y, (int) (w * sound.getClipsLoaded() / sound.getClipTotal()), h);
				offGraphics.setColor(Color.white);
				offGraphics.drawRect(x, y, w, h);
				offGraphics.drawString(s, x + 2 * fontWidth, y + fm.getMaxAscent());
			} else {
			s = "Game Over";
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
			s = "'S' to Start";
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4 + fontHeight);
			}
		} else if (paused) {
			s = "Game Paused";
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
		}

		// Copy the off screen buffer to the screen.

		g.drawImage(offImage, 0, 0, this);
	}
}
