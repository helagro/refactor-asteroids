package my.asteroids.sprite.thruster;

import java.awt.Graphics;


public class RevThruster extends Thruster{
    public RevThruster(){
        shape.addPoint(-2, 12);
		shape.addPoint(-4, 14);
		shape.addPoint(-2, 20);
		shape.addPoint(0, 14);
		shape.addPoint(2, 12);
		shape.addPoint(4, 14);
		shape.addPoint(2, 20);
		shape.addPoint(0, 14);
    }


    public void run(Graphics offGraphics){
        offGraphics.drawPolygon(sprite);
        offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1],
                sprite.ypoints[sprite.npoints - 1], sprite.xpoints[0],
                sprite.ypoints[0]);
    }
}
