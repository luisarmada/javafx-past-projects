package player;

import engine.*;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import level.LevelCharacter;
import level.LevelGenerator;
import level.PushableBox;
import npcs.MagicWisp;

public class Player extends AnimationTimer{
    
    public final LevelCharacter character;
    private String charSelect = "GraveRobber";
   
    public boolean canInteract = true;
    
    private boolean isClimbing = false;
    Rectangle pushBoxCheck = new Rectangle(); // Used to test for pushing animation against walls, boxes and such
    
    public boolean ignoreMovementInput = false;
    public MagicWisp mWisp;
    
    // INITIALISE
    public Player(){
        // Character Initialisation
        character = new LevelCharacter( /* spawnX, spawnY, colBoxWidth, colBoxHeight, */ Main.PLYR_GRP, 225, 400, 48, 62,
                                        /* spriteOffsetX, spriteOffsetY, spriteFlipOffset, */ -10, -33, -29,
                                        /* runMovementSpeed, walkMovementSpeed, jumpIntensity, */ 7, 2, 14);
        character.enableDebug = false; // Set to true to display colBox and other hidden movement check boxes used by PhysicsObject
        // Initialise MovementAnim Info < use idleAnimInfo example above > (filePath, cols, rows, totalFrames, frameWidth, frameHeight, frameScale, framesPerSecond)
        
        character.idleAnimInfo = new SpritesheetInformation("/player/sprites/" + charSelect + "_idle.png", 4, 1, 4, 48, 48, 2, 10);
        character.runAnimInfo = new SpritesheetInformation("/player/sprites/" + charSelect + "_run.png", 6, 1, 6, 48, 48, 2, 7);
        character.walkAnimInfo = new SpritesheetInformation("/player/sprites/" + charSelect + "_walk.png", 6, 1, 6, 48, 48, 2, 7);
        character.jumpAnimInfo = new SpritesheetInformation("/player/sprites/" + charSelect + "_jump.png", 3, 1, 3, 48, 48, 2, 7);
        character.fallAnimInfo = new SpritesheetInformation("/player/sprites/" + charSelect + "_fall.png", 3, 1, 3, 48, 48, 2, 7);
        character.start();
        
        mWisp = new MagicWisp();
        mWisp.start();
    }
    
    @Override 
    public void handle(long now){
        
        ArrayList<KeyCode> currentlyHeldKeys = Main.input.heldInputKeys;       
        KeyCode moveRightInput = Main.input.playerRightInput;
        KeyCode moveLeftInput = Main.input.playerLeftInput;
        KeyCode upwardInput = Main.input.playerUpInput;
        KeyCode downwardInput = Main.input.playerDownInput;
        KeyCode jumpInput = Main.input.playerJumpInput;
        KeyCode interactInput = Main.input.playerInteractInput;
        
        character.phys.ignorePlatforms = currentlyHeldKeys.contains(downwardInput);
        character.wantsJump = currentlyHeldKeys.contains(jumpInput);
        
        if(!ignoreMovementInput){
            //<editor-fold defaultstate="collapsed" desc="Input">
            if(!isClimbing){
                if(character.spriteAnim.filePath.equals("/player/sprites/" + charSelect + "_climb.png") && character.playMovementAnimations == false){
                    character.playMovementAnimations = true;
                }
                if(currentlyHeldKeys.contains(upwardInput)){
                    for(Rectangle ladderCol : LevelGenerator.ladderColList){
                        if(character.colBox.getBoundsInParent().intersects(ladderCol.getBoundsInParent())){
                            isClimbing = true;
                            character.phys.applyGravity = false;
                            character.phys.xVelocity = 0;
                            character.colBox.setX((ladderCol.getX() + (ladderCol.getWidth()/2) - (character.colBox.getWidth()/2)));
                            character.playMovementAnimations = false;
                            character.spriteAnim.changeSprite(character.spriteIV, "/player/sprites/" + charSelect + "_climb.png", 6, 1, 6, 48, 48, 2, 7);
                        }
                    }
                }
            } else {
                if(!character.spriteAnim.filePath.equals("/player/sprites/" + charSelect + "_climb.png")){
                    character.spriteAnim.changeSprite(character.spriteIV, "/player/sprites/" + charSelect + "_climb.png", 6, 1, 6, 48, 48, 2, 7);
                }
                canInteract = false;
                boolean stopClimbingWithJump = currentlyHeldKeys.contains(jumpInput);
                if(currentlyHeldKeys.contains(interactInput) || stopClimbingWithJump){
                    isClimbing = false;
                    canInteract = true;
                    character.playMovementAnimations = true;
                    if(stopClimbingWithJump){
                        character.phys.allowJumpWithoutGravity = true;
                        character.phys.jump(12);
                        character.phys.allowJumpWithoutGravity = false;
                    }
                    character.phys.applyGravity = true;
                } else if(!(currentlyHeldKeys.contains(upwardInput) && currentlyHeldKeys.contains(downwardInput)) &&
                        (currentlyHeldKeys.contains(upwardInput) || currentlyHeldKeys.contains(downwardInput))){
                    if(currentlyHeldKeys.contains(upwardInput)){
                        character.phys.yVelocity = character.phys.movableSpacesY(-4);
                    } else if(currentlyHeldKeys.contains(downwardInput)){
                        character.phys.yVelocity = character.phys.movableSpacesY(2);
                    }
                    boolean ladderHit = false;
                    for(Rectangle ladderCol : LevelGenerator.ladderColList){
                        if(character.phys.yMoveCheck.getBoundsInParent().intersects(ladderCol.getBoundsInParent())){
                            ladderHit = true;
                            character.colBox.setX((ladderCol.getX() + (ladderCol.getWidth()/2) - (character.colBox.getWidth()/2)));
                            break;
                        }
                    }
                    if(ladderHit){
                        if(character.phys.yVelocity != 0){
                            character.spriteAnim.fps = 7;
                            character.colBox.setY(character.colBox.getY() + character.phys.yVelocity);
                        } else {
                            character.spriteAnim.fps = 0;
                        }
                    } else {
                        character.spriteAnim.fps = 0;
                    }
                } else {
                    if(isClimbing){
                        character.spriteAnim.fps = 0;
                    }
                }
            }
            
            if(!(currentlyHeldKeys.contains(moveRightInput) && currentlyHeldKeys.contains(moveLeftInput)) &&
                    (currentlyHeldKeys.contains(moveRightInput) || currentlyHeldKeys.contains(moveLeftInput)) &&
                    !isClimbing){
                if(currentlyHeldKeys.contains(moveRightInput)){
                    character.moveRightInput = 1;
                } else if(currentlyHeldKeys.contains(moveLeftInput)){
                    character.moveRightInput = -1;
                }
                if(character.phys.onPlatformCheck()){
                    int pushBoxWidth = character.isWalking ? character.walkMovementSpeed : character.runMovementSpeed;
                    pushBoxCheck.setX(character.moveRightInput < 0 ? character.colBox.getX() - pushBoxWidth : character.colBox.getX() + character.colBox.getWidth());
                    pushBoxCheck.setY(character.colBox.getY());
                    pushBoxCheck.setHeight(character.colBox.getHeight() - character.phys.slopeClimbHeight);
                    pushBoxCheck.setWidth(pushBoxWidth);
                    boolean hit = false;
                    for(Shape colShape : LevelGenerator.colShapeList){ // Push against walls
                        if(pushBoxCheck.getBoundsInParent().intersects(colShape.getBoundsInParent())){
                            hit = true;
                        }
                    }
                    if(hit){
                        for(PushableBox pushBox : LevelGenerator.pushBoxList){ // Testing for actual pushable boxes
                            if(pushBoxCheck.getBoundsInParent().intersects(pushBox.pushBoxCol.getBoundsInParent())){
                                pushBox.moveBox(character.moveRightInput);
                            }
                        }
                        character.playMovementAnimations = false;
                        character.spriteAnim.changeSprite(character.spriteIV, "/player/sprites/" + charSelect + "_push.png", 6, 1, 6, 48, 48, 2, 7);
                    } else if(character.spriteAnim.filePath.equals("/player/sprites/" + charSelect + "_push.png") && character.playMovementAnimations == false){
                        character.playMovementAnimations = true;
                    }
                } else if(character.spriteAnim.filePath.equals("/player/sprites/" + charSelect + "_push.png") && character.playMovementAnimations == false){
                    character.playMovementAnimations = true;
                }

            } else {
                character.moveRightInput = 0;
                if(character.spriteAnim.filePath.equals("/player/sprites/" + charSelect + "_push.png") && character.playMovementAnimations == false){
                    character.playMovementAnimations = true;
                }
            }
            
            if(!isClimbing && (currentlyHeldKeys.contains(upwardInput))){ // Prevent holding W from overriding getting off the ladder
                currentlyHeldKeys.remove(upwardInput);
            }
//</editor-fold>
        }
        
        if(character.spriteAnim.loopFinish){
            character.playMovementAnimations = true;
            ignoreMovementInput = false;
            character.spriteAnim.loopFinish = false;
        }
        
    }
    
    public void meleeAttack(){
        character.playMovementAnimations = false;
        character.spriteAnim.changeSprite(character.spriteIV, "/player/sprites/" + charSelect + "_attack3.png", 6, 1, 6, 48, 48, 2, 15);
        character.spriteAnim.loop = false;
        character.spriteAnim.animTag = "attack1";
        character.phys.moveRightInput = 0;
        ignoreMovementInput = true;
    }
    
}
