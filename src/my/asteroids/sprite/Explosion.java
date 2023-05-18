package my.asteroids.sprite;

import java.awt.Polygon;

public class Explosion extends AsteroidsSprite{
    public void init(){
        shape = new Polygon();
        active = false;
    }

    public void explode(AsteroidsSprite s, int i, int j){
        active = true;
        shape = new Polygon();

        int cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
        int cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);

        shape.addPoint(s.shape.xpoints[i] - cx, s.shape.ypoints[i] - cy);
        shape.addPoint(s.shape.xpoints[j] - cx, s.shape.ypoints[j] - cy);
        x = s.x + cx;
        y = s.y + cy;
        angle = s.angle;
        deltaAngle = 4 * (Math.random() * 2 * Asteroid.MAX_ROCK_SPIN - Asteroid.MAX_ROCK_SPIN);
        deltaX = (Math.random() * 2 * Asteroid.MAX_ROCK_SPEED - Asteroid.MAX_ROCK_SPEED + s.deltaX) / 2;
        deltaY = (Math.random() * 2 * Asteroid.MAX_ROCK_SPEED - Asteroid.MAX_ROCK_SPEED + s.deltaY) / 2;
    }
}
