package my.asteroids.sprite;

import java.awt.Polygon;

import my.asteroids.Asteroids;

public class Asteroid extends AsteroidsSprite{

    public static final int MIN_ROCK_SIDES = 6; // Ranges for asteroid shape, size
	public static final int MAX_ROCK_SIDES = 16; // speed and rotation.
	public static final int MIN_ROCK_SIZE = 20;
	public static final int MAX_ROCK_SIZE = 40;
	public static final double MIN_ROCK_SPEED = 40.0 / Asteroids.FPS;
	public static final double MAX_ROCK_SPEED = 240.0 / Asteroids.FPS;
	public static final double MAX_ROCK_SPIN = Math.PI / Asteroids.FPS;





    public void init(double asteroidsSpeed){
        int x, y;

        // Create a jagged shape for the asteroid and give it a random rotation.

        shape = new Polygon();
        int s = MIN_ROCK_SIDES + (int) (Math.random() * (MAX_ROCK_SIDES - MIN_ROCK_SIDES));
        for (int j = 0; j < s; j++) {
            double theta = 2 * Math.PI / s * j;
            double r = MIN_ROCK_SIZE + (int) (Math.random() * (MAX_ROCK_SIZE - MIN_ROCK_SIZE));
            x = (int) -Math.round(r * Math.sin(theta));
            y = (int) Math.round(r * Math.cos(theta));
            shape.addPoint(x, y);
        }
        active = true;
        angle = 0.0;
        deltaAngle = Math.random() * 2 * MAX_ROCK_SPIN - MAX_ROCK_SPIN;

        // Place the asteroid at one edge of the screen.

        if (Math.random() < 0.5) {
            x = -AsteroidsSprite.width / 2;
            if (Math.random() < 0.5)
                x = AsteroidsSprite.width / 2;
            this.y = Math.random() * AsteroidsSprite.height;
        } else {
            this.x = Math.random() * AsteroidsSprite.width;
            y = -AsteroidsSprite.height / 2;
            if (Math.random() < 0.5)
                y = AsteroidsSprite.height / 2;
        }

        // Set a random motion for the asteroid.

        deltaX = Math.random() * asteroidsSpeed;
        if (Math.random() < 0.5)
            deltaX = -deltaX;
        deltaY = Math.random() * asteroidsSpeed;
        if (Math.random() < 0.5)
            deltaY = -deltaY;

        render();
    }


    public void shrink(double x, double y, double asteroidsSpeed){
        shape = new Polygon();
        int s = Asteroid.MIN_ROCK_SIDES + (int) (Math.random() * (Asteroid.MAX_ROCK_SIDES - Asteroid.MIN_ROCK_SIDES));
        for (int j = 0; j < s; j++) {
            double theta = 2 * Math.PI / s * j;
            double r = (Asteroid.MIN_ROCK_SIZE + (int) (Math.random() * (Asteroid.MAX_ROCK_SIZE - Asteroid.MIN_ROCK_SIZE))) / 2;
            int pointX = (int) -Math.round(r * Math.sin(theta));
            int pointY = (int) Math.round(r * Math.cos(theta));
            shape.addPoint(pointX, pointY);
        }
        active = true;
        angle = 0.0;
        deltaAngle = Math.random() * 2 * Asteroid.MAX_ROCK_SPIN - Asteroid.MAX_ROCK_SPIN;
        this.x = x;
        this.y = y;
        deltaX = Math.random() * 2 * asteroidsSpeed - asteroidsSpeed;
        deltaY = Math.random() * 2 * asteroidsSpeed - asteroidsSpeed;
        render();
    }
}
