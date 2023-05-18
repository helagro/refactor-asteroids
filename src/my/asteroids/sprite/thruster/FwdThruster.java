package my.asteroids.sprite.thruster;

import java.awt.Graphics;


public class FwdThruster extends Thruster{

    public FwdThruster(){
        shape.addPoint(0, 12);
		shape.addPoint(-3, 16);
		shape.addPoint(0, 26);
		shape.addPoint(3, 16);
    }


    @Override
    protected void onDraw(Graphics offGraphics, boolean detailed){
        offGraphics.drawPolygon(sprite);
        offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1],
                sprite.ypoints[sprite.npoints - 1], sprite.xpoints[0],
                sprite.ypoints[0]);
    }
}
