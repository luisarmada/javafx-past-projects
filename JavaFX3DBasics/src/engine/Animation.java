package engine;

import javafx.animation.AnimationTimer;

public class Animation {

    Animation(){
        
        AnimationTimer animTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //ain.update();
            }
        }; animTimer.start();
        
    }
    
}
