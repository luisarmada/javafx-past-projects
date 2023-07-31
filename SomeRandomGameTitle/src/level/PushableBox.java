package level;

import engine.Main;
import engine.PhysicsObject;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;

public class PushableBox extends AnimationTimer{
    
    public final Rectangle pushBoxCol;
    private final Rectangle pushBoxCol2;
    
    private PhysicsObject phys;
    
    public PushableBox(int x, int y, int width, int height){
        pushBoxCol = new Rectangle(x, y, width, height);
        pushBoxCol2 = new Rectangle(x + 1, y + 1, width - 2, height - 2);
        
        LevelGenerator.colShapeList.add(pushBoxCol2);
        
        phys = new PhysicsObject(pushBoxCol);
        phys.canGoUpSlopes = false;
        //phys.enableDebug = true;
        Main.addToLevelGroup(pushBoxCol2);
        phys.start();
        
    }

    @Override
    public void handle(long now) {
        pushBoxCol2.setX(pushBoxCol.getX() + 1);
        pushBoxCol2.setY(pushBoxCol.getY() + 1);
    }
    
    public void moveBox(int moveRightInput){
        if(moveRightInput > 0){
            pushBoxCol.setX(pushBoxCol.getX() + phys.movableSpacesX(Main.player.character.walkMovementSpeed));
        } else {
            pushBoxCol.setX(pushBoxCol.getX() + phys.movableSpacesX(-Main.player.character.walkMovementSpeed));
        }
    }
    
}
