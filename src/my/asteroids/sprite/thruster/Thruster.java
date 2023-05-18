package my.asteroids.sprite.thruster;

import my.asteroids.sprite.AsteroidsSprite;
import my.asteroids.sprite.Ship;

public class Thruster extends AsteroidsSprite{
    public void position(Ship ship){
        x = ship.x;
		y = ship.y;
		angle = ship.angle;
		render();
    }
}
