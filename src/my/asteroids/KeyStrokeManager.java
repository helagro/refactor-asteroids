package my.asteroids;

import java.awt.event.KeyEvent;

public class KeyStrokeManager implements java.awt.event.KeyListener{
    KSController kController;

    public KeyStrokeManager(KSController kController){
        this.kController = kController;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                kController.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                kController.setRight(true);
                break;
            case KeyEvent.VK_UP:
                kController.setUp(true);
                break;
            case KeyEvent.VK_DOWN:
                kController.setDown(true);
                break;
            default:
                kController.handleCommandChar(e.getKeyChar());
                return;
        }

        kController.onArrowKeyDown();
    }


    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                kController.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                kController.setRight(false);
                break;
            case KeyEvent.VK_UP:
                kController.setUp(false);
                break;
            case KeyEvent.VK_DOWN:
                kController.setDown(false);
                break;
        }

        kController.onArrowKeyUp();
    }
    
}
