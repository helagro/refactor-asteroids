package my.asteroids.sprite;

public class Photon extends AsteroidsSprite{


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

}
