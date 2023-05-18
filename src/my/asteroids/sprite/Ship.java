package my.asteroids.sprite;

public class Ship extends AsteroidsSprite{


	// Reset the ship sprite at the center of the screen.
    public void init(){
        active = true;
		angle = 0.0;
		deltaAngle = 0.0;
		x = 0.0;
		y = 0.0;
		deltaX = 0.0;
		deltaY = 0.0;

		render();
    }


    @Override
    public void render() {
        shape.addPoint(0, -10);
		shape.addPoint(7, 10);
		shape.addPoint(-7, 10);

        super.render();
    }

}
