package my.asteroids;

import my.asteroids.sprite.SpriteObj;

public interface GCListener{
    public void repaint();
    public void explode(SpriteObj s);
}