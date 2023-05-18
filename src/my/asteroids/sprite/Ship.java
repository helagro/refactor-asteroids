package my.asteroids.sprite;

public class Ship extends AsteroidsSprite{
    @Override
    public void render() {
        shape.addPoint(0, -10);
		shape.addPoint(7, 10);
		shape.addPoint(-7, 10);

        super.render();
    }
}
