package my.asteroids.sprite;

public class FwdThruster extends Thruster{
    @Override
    public void render() {
        shape.addPoint(0, 12);
		shape.addPoint(-3, 16);
		shape.addPoint(0, 26);
		shape.addPoint(3, 16);

        super.render();
    }
}
