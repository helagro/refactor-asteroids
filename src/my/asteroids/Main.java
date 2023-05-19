package my.asteroids;

import my.asteroids.sprite.GameView;


public class Main {
    public static void main(String[] args) {
        KSController kController = new KSController();
        KeyStrokeManager keyStrokeManager = new KeyStrokeManager(kController);
        GameController gc = new GameController();

	    SoundController sound = SoundController.getInstance();

        GameView gameView = new GameView(gc, sound);
        GameLogic gameLogic = new GameLogic(kController, gc, sound);

        kController.addListener(gameLogic);
        gameView.addKeyListener(keyStrokeManager);

        gameLogic.start();
    }
}
