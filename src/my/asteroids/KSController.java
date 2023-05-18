package my.asteroids;

import java.util.ArrayList;

public class KSController {
    private ArrayList<KSCListener> listeners = new ArrayList<KSCListener>();

	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;


    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }
    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }
    public void setUp(boolean up) {
        this.up = up;
    }
    
    public boolean isDown() {
        return down;
    }
    public void setDown(boolean down) {
        this.down = down;
    }


    public boolean hasVertical(){
        return up || down;
    }


    public void addListener(KSCListener listener){
        listeners.add(listener);
    }


    public void handleCommandChar(char command){
        command = Character.toLowerCase(command);
        for(KSCListener listener : listeners)
            listener.onChar(command);
    }

    public void onArrowKeyDown(){
        for(KSCListener listener : listeners)
            listener.onArrowKeyDown();
    }

    public void onArrowKeyUp(){
        for(KSCListener listener : listeners)
            listener.onArrowKeyUp();
    }
}
