package entities;

import engine.*;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class SpriteManager {
   
    static ImageView ivSprite = new ImageView(new Image(SpriteManager.class.getResourceAsStream("/images/player/unarmed/adventurer-idle-00.png"), 150, 74, true, false));
    static Rectangle target;
    int ivOffsetX, ivOffsetY;
    
    int nextSpriteIndex = 0;
    boolean loopAnimation = true;
    boolean flipHorizontally = false;
    ArrayList<String> spriteList = new ArrayList();
    
    
    SpriteManager(Rectangle targetSprite, int offsetX, int offsetY){
        target = targetSprite;
        ivOffsetX = offsetX;
        ivOffsetY = offsetY;
    }
    
    public void updateSpriteLoc(){
        ivSprite.setLayoutX(target.getX() + ivOffsetX);
        ivSprite.setLayoutY(target.getY() + ivOffsetY);
    }
    
    public void updateSprites(){
        if(nextSpriteIndex < spriteList.size() - 1){
            nextSpriteIndex++;
        } else {
            if(loopAnimation)
                nextSpriteIndex = 0;
        }
        
        MainClass.removeFromRoot(ivSprite);
        ivSprite = new ImageView(new Image(SpriteManager.class.getResourceAsStream(spriteList.get(nextSpriteIndex)), 150, 74, true, false));
        
        if(flipHorizontally){
            ivSprite.setScaleX(-1);
        }
        
        updateSpriteLoc();
        MainClass.addToRoot(ivSprite);
        
        //Thread.sleep(1000);
    }
    
}
