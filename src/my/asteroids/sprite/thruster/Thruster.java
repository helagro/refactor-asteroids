package my.asteroids.sprite.thruster;

import my.asteroids.sprite.SpriteObj;
import my.asteroids.sprite.Ship;

public abstract class Thruster extends SpriteObj{
    public void position(Ship ship){
        x = ship.x;
		y = ship.y;
		angle = ship.angle;
		render();
    }
}
