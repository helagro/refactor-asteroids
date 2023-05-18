package my.asteroids.sprite;

import java.awt.Color;
import java.awt.Graphics;

import javax.sound.sampled.Clip;

import my.asteroids.Game;
import my.asteroids.Sound;

public class FlyingSaucer extends SpriteObj{

	public static final int NEW_UFO_POINTS = 600; //TODO : 2570
	public static final int POINTS = 250;
    static final int PASSES = 3; // Number of passes for flying saucer per appearance.
	static final double Missile_PROBABILITY = 0.45 / Game.FPS;

	private int ufoCounter; // Timer counter used to track each flying saucer pass.
	private int passesLeft; // Counter for number of flying saucer passes.
    private Sound sound = Sound.getInstance();


    public FlyingSaucer(){
        shape.addPoint(-15, 0);
		shape.addPoint(-10, -5);
		shape.addPoint(-5, -5);
		shape.addPoint(-5, -8);
		shape.addPoint(5, -8);
		shape.addPoint(5, -5);
		shape.addPoint(10, -5);
		shape.addPoint(15, 0);
		shape.addPoint(10, 5);
		shape.addPoint(-10, 5);
    }

    public void init(){
        active = true;

        position();
        passesLeft = PASSES;
        sound.play(sound.getSaucerSound(), Clip.LOOP_CONTINUOUSLY);

		render();
    }


    private void position(){
		x = -SpriteObj.width / 2;
		y = Math.random() * 2 * SpriteObj.height - SpriteObj.height;
		double angle = Math.random() * Math.PI / 4 - Math.PI / 2;
		double speed = Asteroid.MAX_ROCK_SPEED / 2 + Math.random() * (Asteroid.MAX_ROCK_SPEED / 2);
		deltaX = speed * -Math.sin(angle);
		deltaY = speed * Math.cos(angle);
		if (Math.random() < 0.5) {
			x = SpriteObj.width / 2;
			deltaX = -deltaX;
		}

		ufoCounter = (int) Math.abs(SpriteObj.width / deltaX);
    }


    public void update(Ship ship, Missile missile) {
        if(!active) return;

		// Move the flying saucer and check for collision with a photon. Stop it
		// when its counter has expired.
        if (--ufoCounter <= 0) {
            if (--passesLeft > 0)
                position();
            else
                stop();
        }

		if(shouldFireMissile(ship, missile)){
			fireMissile(missile);
		}

        advance();
        render();
	}

    private boolean shouldFireMissile(Ship ship, Missile missile){
        int d = (int) Math.max(Math.abs(x - ship.x), Math.abs(y - ship.y));
        boolean goodShipDistance = d > Asteroid.MAX_ROCK_SPEED * Game.FPS / 2;
        boolean canFireRandom = Math.random() < Missile_PROBABILITY;

        return (
            ship.active && 
            !ship.isHyperSpace() && 
            !missile.active && 
            goodShipDistance && 
            canFireRandom
        );
    }

    public void fireMissile(Missile missile){
        missile.init(this);
    }

    public void stop(){
        active = false;
		ufoCounter = 0;

        if(sound.isLoaded())
            sound.getSaucerSound().stop();
    }


    // =========== DRAW ===========

    @Override
    protected void onDraw(Graphics offGraphics, boolean detailed){
        if(detailed){
            offGraphics.setColor(Color.black);
            offGraphics.fillPolygon(sprite);
        }

        offGraphics.setColor(Color.white);
        offGraphics.drawPolygon(sprite);
        offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1], sprite.ypoints[sprite.npoints - 1],
                sprite.xpoints[0], sprite.ypoints[0]);
    }
}
