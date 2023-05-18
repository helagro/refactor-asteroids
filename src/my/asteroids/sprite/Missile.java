package my.asteroids.sprite;

import java.awt.Graphics;

import javax.sound.sampled.Clip;

import my.asteroids.Asteroids;
import my.asteroids.Sound;

public class Missile extends SpriteObj{

	static final int Missile_COUNT = 4 * Asteroids.FPS; // seconds x frames per second.
	private int missileCounter; // Counter for life of 
	private Sound sound = Sound.getInstance();


    public Missile(){
        shape.addPoint(0, -4);
        shape.addPoint(1, -3);
        shape.addPoint(1, 3);
        shape.addPoint(2, 4);
        shape.addPoint(-2, 4);
        shape.addPoint(-1, 3);
        shape.addPoint(-1, -3);
    }

    public void init(FlyingSaucer ufo){
		missileCounter = Missile_COUNT;

        active = true;
		angle = 0.0;
		deltaAngle = 0.0;
		x = ufo.x;
		y = ufo.y;
		deltaX = 0.0;
		deltaY = 0.0;
		render();

		sound.play(sound.getMissileSound(), Clip.LOOP_CONTINUOUSLY);
    }

    public void update(){
        if (--missileCounter <= 0)
            stop();
        else {
            advance();
            render();
        }
    }


    public void guide(Ship ship, boolean notHyper) {
		double dx, dy, angle;
		if (!ship.active || !notHyper)
			return;

		// Find the angle needed to hit the ship.
		dx = ship.x - x;
		dy = ship.y - y;
		if (dx == 0 && dy == 0)
			angle = 0;
		if (dx == 0) {
			if (dy < 0)
				angle = -Math.PI / 2;
			else
				angle = Math.PI / 2;
		} else {
			angle = Math.atan(Math.abs(dy / dx));
			if (dy > 0)
				angle = -angle;
			if (dx < 0)
				angle = Math.PI - angle;
		}

		// Adjust angle for screen coordinates.

		angle = angle - Math.PI / 2;

		// Change the missile's angle so that it points toward the ship.

		deltaX = 0.75 * Asteroid.MAX_ROCK_SPEED * -Math.sin(angle);
		deltaY = 0.75 * Asteroid.MAX_ROCK_SPEED * Math.cos(angle);
	}

    public boolean collidedWith(Ship ship){
        return 
            active &&
            ship.active &&
            !ship.isHyperSpace() &&
            ship.isColliding(this);
    }

    public void stop(){
        active = false;
		missileCounter = 0;
		sound.stop(sound.getMissileSound());
    }

    public int getMissileCounter(){
        return missileCounter;
    }

	@Override
	protected void onDraw(Graphics offGraphics, boolean detailed) {
		offGraphics.drawPolygon(sprite);
		offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1],
		sprite.ypoints[sprite.npoints - 1], sprite.xpoints[0],
		sprite.ypoints[0]);
	}
}
