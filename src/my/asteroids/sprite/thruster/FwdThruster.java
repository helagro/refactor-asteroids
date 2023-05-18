package my.asteroids.sprite.thruster;

import java.awt.Graphics;


public class FwdThruster extends Thruster{

    public FwdThruster(){
        shape.addPoint(0, 12);
		shape.addPoint(-3, 16);
		shape.addPoint(0, 26);
		shape.addPoint(3, 16);

        active = true;
    }


    @Override
    protected void onDraw(Graphics offGraphics, boolean detailed){
        if(!detailed) return;

        offGraphics.drawPolygon(sprite);
        offGraphics.drawLine(sprite.xpoints[sprite.npoints - 1],
                sprite.ypoints[sprite.npoints - 1], sprite.xpoints[0],
                sprite.ypoints[0]);
    }
}
