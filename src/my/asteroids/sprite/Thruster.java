package my.asteroids.sprite;

public class Thruster extends AsteroidsSprite{
    public void init(Ship ship){
        x = ship.x;
		y = ship.y;
		angle = ship.angle;
		render();
    }
}
