package entities;

import engine.*;
import entities.other.Arrow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import level.Camera;
public class Player {

    final public static int COLLISION_WIDTH = 40;
    final public static int COLLISION_HEIGHT = 50;
    public static Rectangle collisionBox;
    
    static int physChildIndex;
    static int sprChildIndex;
    
    static boolean canAirAttack = true;
    static boolean canAttack = false;
    static int attackFrame = 0;
    static enum weaponMode{
        GRD_BOW, AIR_BOW
    }
     
    public Player(int spawnX, int spawnY){
        // Create main collision box for PhysicsEntity
        collisionBox = new Rectangle(spawnX, spawnY, COLLISION_WIDTH, COLLISION_HEIGHT);
        collisionBox.setFill(Color.web("#7ea6f4"));
        //RenderEngine.addToGroup("LV", collisionBox);
        
        physChildIndex = PhysicsManager.addChild(collisionBox, 5);
        sprChildIndex = SpriteManager.createNewSprite(collisionBox, -18, -3, 150, 74);
        SpriteManager.setSpriteSet(sprChildIndex, "player", "idle", 3);
        
        getInput();
        
        //UI_Inventory.create();
    }
    
    private void getInput(){
        
        RenderEngine.ROOT.getScene().addEventHandler(KeyEvent.KEY_PRESSED, e->{
           switch(e.getCode()){
                case A:
                    PhysicsManager.movingLeft.set(physChildIndex, true);
                    break;
                case D:
                    PhysicsManager.movingRight.set(physChildIndex, true);
                    break;
                case SPACE:
                    if(!SpriteManager.playingActionAnimation.get(sprChildIndex))
                        PhysicsManager.wantsJump.set(physChildIndex, true);
                    break;
                case F11:
                    Camera.setFullscreen(!RenderEngine.rootStage.isFullScreen());
                    break;
           } 
        });
        
        RenderEngine.ROOT.getScene().addEventHandler(KeyEvent.KEY_RELEASED, e->{
           switch(e.getCode()){
                case A:
                   PhysicsManager.movingLeft.set(physChildIndex, false);
                   break;
                case D:
                    PhysicsManager.movingRight.set(physChildIndex, false);
                    break;
                case SPACE:
                    PhysicsManager.wantsJump.set(physChildIndex, false);
                    break;
           } 
        });

        RenderEngine.ROOT.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, e-> {
            switch(e.getButton()){
                case PRIMARY:
                    if(!SpriteManager.playingActionAnimation.get(sprChildIndex)){
                        if(PhysicsManager.isOnPlatform.get(physChildIndex)){
                            SpriteManager.setSpriteSet(sprChildIndex, "player_attacks", "bow", 8);
                            SpriteManager.playingActionAnimation.set(sprChildIndex, true);
                            canAttack = true;
                            attackFrame = 8;
                        } else {
                            if(canAirAttack){
                                SpriteManager.setSpriteSet(sprChildIndex, "player_attacks", "bow-jump", 5);
                                SpriteManager.playingActionAnimation.set(sprChildIndex, true);
                                canAirAttack = false;
                                canAttack = true;
                                attackFrame = 5;
                            }
                        }
                        //Arrow.createArrow(collisionBox.getX(), collisionBox.getY() + 20, !SpriteManager.flipped.get(sprChildIndex));
                    }
                    
                    break;
            }
        });
    }
    
    public static void update(){
        
        PhysicsManager.applyGravity.set(physChildIndex, !SpriteManager.playingActionAnimation.get(sprChildIndex));
        
        if(!SpriteManager.playingActionAnimation.get(sprChildIndex)){
            // Player Sprite Managing
            PhysicsManager.lockMovement.set(physChildIndex, false);
            if(PhysicsManager.isOnPlatform.get(physChildIndex)){
                canAirAttack = true;
                if(PhysicsManager.rightVelocities.get(physChildIndex) != 0){
                    SpriteManager.setSpriteSet(sprChildIndex, "player", "run", 5);
                } else {
                    SpriteManager.setSpriteSet(sprChildIndex, "player", "idle", 3);
                }
            } else {
                if(PhysicsManager.downVelocities.get(physChildIndex) > 0){
                    SpriteManager.setSpriteSet(sprChildIndex, "player", "fall", 1);
                } else {
                    SpriteManager.setSpriteSet(sprChildIndex, "player", "jump", 3, false);
                }
            }

            if(PhysicsManager.rightVelocities.get(physChildIndex) > 0){
                SpriteManager.flipped.set(sprChildIndex, false);
            } else if(PhysicsManager.rightVelocities.get(physChildIndex) < 0){
                SpriteManager.flipped.set(sprChildIndex, true);
            }
        } else {
            PhysicsManager.lockMovement.set(physChildIndex, true);
            PhysicsManager.downVelocities.set(physChildIndex, 0.0);
            if(canAttack && attackFrame - 1 == SpriteManager.currentFrame.get(sprChildIndex)){
                Arrow.createArrow(collisionBox.getX(), collisionBox.getY() + 20, !SpriteManager.flipped.get(sprChildIndex), 25);
                canAttack = false;
            }
        }
    }
}
