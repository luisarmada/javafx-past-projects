package engine;

import entities.*;
import entities.enemies.BasicEnemyHealthBar;
import entities.enemies.EnemyManager;
import entities.other.Arrow;
import javafx.animation.AnimationTimer;
import level.*;

public class Animation {

    public static long TimerNow;
    
    Animation(){
        
        AnimationTimer animTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                
                TimerNow = now;
                
                PhysicsManager.update();
                Camera.update();
                SpriteManager.update();
                Player.update();
                EnemyManager.update();
                Arrow.update();
                //BasicEnemyHealthBar.update();
                
            }
        }; animTimer.start();
        
    }
    
}
