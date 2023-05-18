package my.asteroids.sprite;

public class Photon extends AsteroidsSprite{

    public Photon(){
        shape.addPoint(1, 1);
        shape.addPoint(1, -1);
        shape.addPoint(-1, 1);
        shape.addPoint(-1, -1);
    }

}
