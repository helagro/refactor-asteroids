package my.asteroids.sprite;

public class Thruster extends AsteroidsSprite{
    public void position(Ship ship){
        x = ship.x;
		y = ship.y;
		angle = ship.angle;
		render();
    }
}
