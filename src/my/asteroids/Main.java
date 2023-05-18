package my.asteroids;

public class Main {
    public static void main(String[] args) {
        KSController kController = new KSController();
        KeyStrokeManager keyStrokeManager = new KeyStrokeManager(kController);

        Game game = new Game(kController);
        kController.addListener(game);
        game.addKeyListener(keyStrokeManager);
        game.start();
    }
}
