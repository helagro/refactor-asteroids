package my.asteroids.sprite;

import java.awt.Color;
import java.awt.Graphics;

import my.asteroids.Asteroids;
import my.asteroids.sprite.thruster.FwdThruster;
import my.asteroids.sprite.thruster.RevThruster;


public class Ship extends AsteroidsSprite{

	static final double SHIP_ANGLE_STEP = Math.PI / Asteroids.FPS;
	static final double SHIP_SPEED_STEP = 15.0 / Asteroids.FPS;
	static final double MAX_SHIP_SPEED = 1.25 * Asteroid.MAX_ROCK_SPEED;
	public static final int HYPER_COUNT = 3 * Asteroids.FPS; // calculated using number of


    private FwdThruster fwdThruster;
	private RevThruster revThruster;
	private int hyperCounter; // Timer counter for hyperspace. 



    public Ship(){
        fwdThruster = new FwdThruster();
		revThruster = new RevThruster();

        shape.addPoint(0, -10);
		shape.addPoint(7, 10);
		shape.addPoint(-7, 10);
    }


	// Reset the ship sprite at the center of the screen.
    public void init(boolean firstTime){
        active = true;
		angle = 0.0;
		deltaAngle = 0.0;
		x = 0.0;
		y = 0.0;
		deltaX = 0.0;
		deltaY = 0.0;

        hyperCounter = firstTime ? 0 : HYPER_COUNT;
        positionThrusters();

		render();
    }

    @Override
    public boolean advance() {
        positionThrusters();

        if (isHyperSpace())
            hyperCounter--;

        return super.advance();
    }


    private void positionThrusters(){
        revThruster.position(this);
        fwdThruster.position(this);
    }


    // ======== HYPER SPACE ==========

    public boolean isHyperSpace(){
        return hyperCounter > 0;
    }

    public void enterHyperSpace(){
        hyperCounter = HYPER_COUNT;
    }

    public int getHyperSpaceCounter(){
        return hyperCounter;
    }



    public void draw(Graphics offGraphics, boolean fill, boolean drawFwdThrustor, boolean drawRevThrustor, int c){
        if (fill) {
            offGraphics.setColor(Color.black);
            offGraphics.fillPolygon(sprite);
        }
        offGraphics.setColor(new Color(c, c, c));
        offGraphics.drawPolygon(sprite);
        offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1],
                sprite.ypoints[sprite.npoints - 1], sprite.xpoints[0], sprite.ypoints[0]);


        // Draw thruster exhaust if thrusters are on. Do it randomly to get a
        // flicker effect.

        boolean thrustorFlickerOn = Math.random() < 0.5;

        if(thrustorFlickerOn && drawFwdThrustor)
            fwdThruster.run(offGraphics);

        if(thrustorFlickerOn && drawRevThrustor)
            revThruster.run(offGraphics);
    }


    // =========== MOVE ===========

    public void rotateLeft(){
        angle += SHIP_ANGLE_STEP;
        if (angle > 2 * Math.PI)
            angle -= 2 * Math.PI;
    }

    public void rotateRight(){
        angle -= SHIP_ANGLE_STEP;
        if (angle < 0)
            angle += 2 * Math.PI;
    }

    public void moveForward(){
        double dx = SHIP_SPEED_STEP * -Math.sin(angle);
		double dy = SHIP_SPEED_STEP * Math.cos(angle);

        deltaX += dx;
        deltaY += dy;

        noSpeeding(true);
    }
    
    public void moveBackward(){
        double dx = SHIP_SPEED_STEP * -Math.sin(angle);
		double dy = SHIP_SPEED_STEP * Math.cos(angle);

        deltaX -= dx;
        deltaY -= dy;

        noSpeeding(false);
    }

    private void noSpeeding(boolean fwd){
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (speed > MAX_SHIP_SPEED) {
            double dx = MAX_SHIP_SPEED * -Math.sin(angle);
            double dy = MAX_SHIP_SPEED * Math.cos(angle);
            if (fwd)
                deltaX = dx;
            else
                deltaX = -dx;
            if (fwd)
                deltaY = dy;
            else
                deltaY = -dy;
        }
    }
}
