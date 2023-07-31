package game;

import javafx.animation.AnimationTimer;

public class Animation {

    public static AnimationTimer animTimer;
    public static long timerNow;
    
    Animation(){
        animTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timerNow = now;
                Road.update();
            }
        }; animTimer.start();
    }
    
}
