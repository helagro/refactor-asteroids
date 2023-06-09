package my.asteroids.sprite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;

import my.asteroids.SoundController;
import my.asteroids.GameController;
import my.asteroids.GCListener;


public class GameView extends Panel implements GCListener{
	public static int width;
	public static int height;

    // Copyright information.
	private final String copyName = "Asteroids";
	private final String copyVers = "Version 1.3";
	private final String copyInfo = "Copyright 1998-2001 by Mike Hall";
	private final String copyLink = "http://www.brainjar.com";
	private final String copyText = copyName + '\n' + copyVers + '\n' + copyInfo + '\n' + copyLink;

    // Data for the screen font.
	private Font font = new Font("Helvetica", Font.BOLD, 12);
	private FontMetrics fm = getFontMetrics(font);
	private int fontWidth = fm.getMaxAdvance();
	private int fontHeight = fm.getHeight();

    // Off screen image.
	private Dimension offDimension;
	private Image offImage;
	private Graphics offGraphics;

    private int numStars;
	private Point[] stars;

    private GameController gc;
    private SoundController sound;


    public GameView(GameController gc, SoundController sound){		
        this.gc = gc;
        this.sound = sound;

		gc.setListener(this);

        Frame f = new Frame();
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});

		setSize(700, 400);
		f.add(this);
		f.pack();
		init();
		f.setSize(700, 400 + 20); // add 20, seems enough for the Frame title,
		f.show();
    }

    private void init(){
        Dimension d = new Dimension(700, 400);
		width = d.width;
		height = d.height;

		// Display copyright information.
		System.out.println(copyText);

		requestFocus();
        initStars();
    }

    private void initStars(){
        numStars = GameView.width * GameView.height / 5000;
		stars = new Point[numStars];
		for (int i = 0; i < numStars; i++)
			stars[i] = new Point((int) (Math.random() * GameView.width),
					(int) (Math.random() * GameView.height));
    }


    @Override
    public void update(Graphics g) {
		paint(g);
	}

    @Override
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
		if (gc.isDetail()) {
			offGraphics.setColor(Color.white);
			for (i = 0; i < numStars; i++){
				offGraphics.drawLine(stars[i].x, stars[i].y, stars[i].x, stars[i].y);
			}
		}


		for (i = 0; i < Photon.MAX_SHOTS; i++)
			gc.getPhoton(i).draw(offGraphics, gc.isDetail());

		for (i = 0; i < Asteroid.MAX_ROCKS; i++)
			gc.getAsteroid(i).draw(offGraphics, gc.isDetail());

		gc.getUfo().draw(offGraphics, gc.isDetail());
		gc.getMissile().draw(offGraphics, gc.isDetail());
		gc.getShip().draw(offGraphics, gc.isDetail());

		if(!gc.isPaused()){
			if(gc.isThrustFwd()) gc.getShip().drawFwdThruster(offGraphics, gc.isDetail());
			if(gc.isThrustRev()) gc.getShip().drawRevThruster(offGraphics, gc.isDetail());
		}


		// Draw any explosion debris, counters are used to fade color to black.

		for (i = 0; i < Explosion.MAX_SCRAP; i++)
			if (gc.getExplosion(i).active) {
				c = (255 / Explosion.SCRAP_COUNT) * gc.getExplosionCounterAt(i);
				offGraphics.setColor(new Color(c, c, c));
				offGraphics.drawPolygon(gc.getExplosion(i).sprite);
			}

		// Display status and messages.

		offGraphics.setFont(font);
		offGraphics.setColor(Color.white);

		offGraphics.drawString("Score: " + gc.getScore(), fontWidth, fontHeight);
		offGraphics.drawString("Ships: " + gc.getShipsLeft(), fontWidth, d.height - fontHeight);
		s = "High: " + gc.getHighScore();
		offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), fontHeight);
		if (sound.isMuted()) {
			s = "Mute";
			offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), d.height - fontHeight);
		}

		if (!gc.isPlaying()) {
			s = copyName;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - 2 * fontHeight);
			s = copyVers;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - fontHeight);
			s = copyInfo;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + fontHeight);
			s = copyLink;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + 2 * fontHeight);
			if (!sound.isLoaded()) {
				s = "Loading sounds...";
				w = 4 * fontWidth + fm.stringWidth(s);
				h = fontHeight;
				x = (d.width - w) / 2;
				y = 3 * d.height / 4 - fm.getMaxAscent();
				offGraphics.setColor(Color.black);
				offGraphics.fillRect(x, y, w, h);
				offGraphics.setColor(Color.gray);
				if (sound.CLIP_TOTAL > 0)
					offGraphics.fillRect(x, y, (int) (w * sound.getClipsLoaded() / sound.CLIP_TOTAL), h);
				offGraphics.setColor(Color.white);
				offGraphics.drawRect(x, y, w, h);
				offGraphics.drawString(s, x + 2 * fontWidth, y + fm.getMaxAscent());
			} else {
			s = "Game Over";
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
			s = "'S' to Start";
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4 + fontHeight);
			}
		} else if (gc.isPaused()) {
			s = "Game Paused";
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
		}

		// Copy the off screen buffer to the screen.

		g.drawImage(offImage, 0, 0, this);
	}


	@Override
    public void explode(SpriteObj s) {
		s.render();
		int c = (gc.isDetail() || s.sprite.npoints < 6) ? 1 : 2;

		for (int i = 0; i < s.sprite.npoints; i += c) {

            int explosionIndex = gc.getExplosionIndex();
			explosionIndex++;
			if (explosionIndex >= Explosion.MAX_SCRAP)
				explosionIndex = 0;
            gc.setExplosionIndex(explosionIndex);

			int j = i + 1;
			if (j >= s.sprite.npoints)
				j -= s.sprite.npoints;


			gc.getExplosion(explosionIndex).explode(s, i, j);
            gc.setExplosionCounterAt(explosionIndex, Explosion.SCRAP_COUNT);
		}
	}
}