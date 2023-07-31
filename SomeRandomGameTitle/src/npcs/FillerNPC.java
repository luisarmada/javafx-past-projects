package npcs;

import engine.Main;
import engine.PhysicsObject;
import engine.SpriteAnimation;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FillerNPC extends AnimationTimer{
        
    private final Rectangle collisionBox;
    private final int colBoxWidth = 48;
    private final int colBoxHeight = 62;
    
    private final ImageView sprite = new ImageView();
    private final SpriteAnimation spriteAnim = null;
    private final PhysicsObject phys;
    
    private final int spriteOffsetX = -10;
    private final int spriteOffsetY = -33;
    
    // INITIALISE
    public FillerNPC(){
        collisionBox = new Rectangle();
        collisionBox.setWidth(colBoxWidth);
        collisionBox.setX(800);
        collisionBox.setY(200);
        collisionBox.setHeight(colBoxHeight);
        collisionBox.setFill(Color.web("FF5733"));
        //Main.addToLevelGroup(collisionBox);
        
        phys = new PhysicsObject(collisionBox);
        phys.start();
        //phys.setMoveRightInput(1);
        
        //spriteAnim = new SpriteAnimation(sprite, "/npcs/sprites/Wisp_idle.png", 4, 1, 4, 48, 48, 2, 10);
        //spriteAnim.start();
        Main.addToLevelGroup(sprite);
        
    }
    
    @Override 
    public void handle(long now){
        
        // Attach sprite to collision box
        sprite.setX(collisionBox.getX() + spriteOffsetX);
        sprite.setY(collisionBox.getY() + spriteOffsetY);
        
        phys.jump(12);
        
    }
}
