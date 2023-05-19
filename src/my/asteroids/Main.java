package my.asteroids;

import my.asteroids.sprite.GameView;

public class Main {
    public static void main(String[] args) {
        KSController kController = new KSController();
        KeyStrokeManager keyStrokeManager = new KeyStrokeManager(kController);

        GameController gameObjects = new GameController();

        GameView gameView = new GameView(gameObjects);
        GameLogic gameLogic = new GameLogic(kController, gameObjects, gameView);

        kController.addListener(gameLogic);
        gameView.addKeyListener(keyStrokeManager);

        gameLogic.start();
    }
}
