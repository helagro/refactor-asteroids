package my.asteroids.sprite;

public class RevThruster extends Thruster{
    @Override
    public void render() {
        shape.addPoint(-2, 12);
		shape.addPoint(-4, 14);
		shape.addPoint(-2, 20);
		shape.addPoint(0, 14);
		shape.addPoint(2, 12);
		shape.addPoint(4, 14);
		shape.addPoint(2, 20);
		shape.addPoint(0, 14);

        super.render();
    }
}
