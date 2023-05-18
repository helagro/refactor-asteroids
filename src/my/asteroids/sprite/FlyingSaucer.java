package my.asteroids.sprite;

public class FlyingSaucer extends AsteroidsSprite{
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
		x = -AsteroidsSprite.width / 2;
		y = Math.random() * 2 * AsteroidsSprite.height - AsteroidsSprite.height;
		double angle = Math.random() * Math.PI / 4 - Math.PI / 2;
		double speed = Asteroid.MAX_ROCK_SPEED / 2 + Math.random() * (Asteroid.MAX_ROCK_SPEED / 2);
		deltaX = speed * -Math.sin(angle);
		deltaY = speed * Math.cos(angle);
		if (Math.random() < 0.5) {
			x = AsteroidsSprite.width / 2;
			deltaX = -deltaX;
		}
		render();
    }
}
