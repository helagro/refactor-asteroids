package my.asteroids.sprite;

import java.awt.Graphics;


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

    void run(Graphics offGraphics){
        offGraphics.drawPolygon(sprite);
        offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1],
                sprite.ypoints[sprite.npoints - 1], sprite.xpoints[0],
                sprite.ypoints[0]);
    }
}
