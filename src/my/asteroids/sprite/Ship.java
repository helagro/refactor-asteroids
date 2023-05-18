package my.asteroids.sprite;

import java.awt.Graphics;

import my.asteroids.Asteroids;


public class Ship extends AsteroidsSprite{

	static final double SHIP_ANGLE_STEP = Math.PI / Asteroids.FPS;

    FwdThruster fwdThruster;
	RevThruster revThruster;


    public Ship(){
        fwdThruster = new FwdThruster();
		revThruster = new RevThruster();
    }


	// Reset the ship sprite at the center of the screen.
    public void init(){
        active = true;
		angle = 0.0;
		deltaAngle = 0.0;
		x = 0.0;
		y = 0.0;
		deltaX = 0.0;
		deltaY = 0.0;

        positionThrusters();

		render();
    }


    public void positionThrusters(){
        revThruster.position(this);
        fwdThruster.position(this);
    }


    @Override
    public void render() {
        shape.addPoint(0, -10);
		shape.addPoint(7, 10);
		shape.addPoint(-7, 10);

        super.render();
    }


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


    public void runFwdThrustor(Graphics offGraphics){
        fwdThruster.run(offGraphics);
    }

    public void runRwdThrustor(Graphics offGraphics){
        revThruster.run(offGraphics);
    }

}
