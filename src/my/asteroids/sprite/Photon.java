package my.asteroids.sprite;

import java.awt.Color;
import java.awt.Graphics;

import my.asteroids.Sound;

public class Photon extends SpriteObj{
	public static final int MAX_SHOTS = 80; // Maximum number of sprites TODO: 8

    public Photon(){
        shape.addPoint(1, 1);
        shape.addPoint(1, -1);
        shape.addPoint(-1, 1);
        shape.addPoint(-1, -1);
    }

    public void launch(Ship fromShip){
        active = true;
        x = fromShip.x;
        y = fromShip.y;
        deltaX = 2 * Asteroid.MAX_ROCK_SPEED * -Math.sin(fromShip.angle);
        deltaY = 2 * Asteroid.MAX_ROCK_SPEED * Math.cos(fromShip.angle);
    }

    @Override
    protected void onDraw(Graphics offGraphics, boolean detailed){
		offGraphics.setColor(Color.white);
        offGraphics.drawPolygon(sprite);
    }

    public static boolean handleCollision(Photon[] photons, SpriteObj target){
        if(!target.active) return false;

        Sound sound = Sound.getInstance();

        for(Photon photon : photons){
            if(photon.active && photon.isColliding(target)){
                photon.active = false;
                target.active = false;
                sound.play(sound.getCrashSound(), 1);
                return true;
            }
        }

        return false;
    }

}
