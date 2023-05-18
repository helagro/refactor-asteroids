package my.asteroids.sprite;

import java.awt.Graphics;


public class FwdThruster extends Thruster{
    @Override
    public void render() {
        shape.addPoint(0, 12);
		shape.addPoint(-3, 16);
		shape.addPoint(0, 26);
		shape.addPoint(3, 16);

        super.render();
    }

    void run(Graphics offGraphics){
        offGraphics.drawPolygon(sprite);
        offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1],
                sprite.ypoints[sprite.npoints - 1], sprite.xpoints[0],
                sprite.ypoints[0]);
    }
}
