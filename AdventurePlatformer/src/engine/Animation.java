package engine;

import entities.*;
import javafx.animation.AnimationTimer;

public class Animation {
    
    public static AnimationTimer animTimer;
    public static long TimerNow;
    
    Animation(){
        animTimer = new AnimationTimer(){
            private final long lastUpdate = 0 ;
            @Override public void handle(long now) {
                
                TimerNow = now;
                
                // Game Loop
                Player.update();
                
            }
        }; animTimer.start();        
    }
    
}
