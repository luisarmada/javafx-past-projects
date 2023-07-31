package entities;

import engine.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import level.Ladder;
import level.LevelGenerator;

public class Player {
    
    final public static int COLLISION_WIDTH = 50;
    final public static int COLLISION_HEIGHT = 65;
    public static Rectangle collisionBox;
    
    static PhysicsEntity phys;
    static SpriteManager sprManager;
    
    static boolean movingRight, movingLeft, crouching, onLadder, climbingUp, climbingDown;
    boolean canInteractWithLadder = true;
    boolean facingRightWhenLastLeftLadder;
    static Ladder currentLadder;
    static enum weaponMode{
        UNARMED, SWORD
    }
    static weaponMode currentWeapon = weaponMode.UNARMED;
    
    static long lastUpdate = 0;
    
    static long sprDelay = 100_000_000;
    static String currentSpriteSet = "";
    static weaponMode sprSetWeapon = weaponMode.UNARMED;
    public static boolean rootMontagePlaying = false;
    
    public static String roomChangeDir = "";
    
    public Player(int spawnX, int spawnY){
        
        // Create main collision box for PhysicsEntity
        collisionBox = new Rectangle(spawnX, spawnY, COLLISION_WIDTH, COLLISION_HEIGHT);
        collisionBox.setFill(Color.web("#7ea6f4"));
        MainClass.addToRoot(collisionBox);
        
        phys = new PhysicsEntity(collisionBox);
        sprManager = new SpriteManager(collisionBox, -25, -7);
        setSpriteSet("idle", 4, true);
        
        getInput();
    }
    
    public static void update(){
        
        if(!onLadder)
            phys.addGravity(); // Add gravity, which also handles jumping
        
        // Add right force based on input
        if(!(movingRight && movingLeft) && (movingRight || movingLeft) && !crouching && !onLadder && !rootMontagePlaying){
            if(movingRight){
                phys.moveRight(1);
                sprManager.flipHorizontally = false;
            } else {
                phys.moveRight(-1);
                sprManager.flipHorizontally = true;
            }
        } else {
            phys.rightVelocity = 0;
            if(phys.isOnPlatform)
                if(crouching){
                    setSpriteSet("crouch", 4, true);
                }
        }
        if(!phys.isOnPlatform && phys.downVelocity > 0 && !onLadder) // Falling sprite set
            setSpriteSet("fall", 2, true);
        
        if(phys.rightVelocity != 0 && phys.isOnPlatform && !crouching){
            setSpriteSet("run", 6, true);
        } else if(phys.isOnPlatform && !crouching){
            setSpriteSet("idle", 4, true);
        }
        
        // Update sprite location and change sprite index after specified delay
        sprManager.updateSpriteLoc();
        if(Animation.TimerNow - lastUpdate >= sprDelay){
            sprManager.updateSprites();
            lastUpdate = Animation.TimerNow;
        }
        
        if(!rootMontagePlaying && currentWeapon == weaponMode.UNARMED)
            ladderInteractions(); // Enable ladder functions
        
        //if(collisionBox.getX() > MainClass.WINDOW_WIDTH){
        //    LevelGenerator.generateRoom("TEST_LEVEL", LevelGenerator.currentRoomRow, LevelGenerator.currentRoomColumn + 1, false);
        //    Player.collisionBox.setX(10);
        //}
        
        //if(collisionBox.getX() + collisionBox.getWidth() < 0){
        //    LevelGenerator.generateRoom("TEST_LEVEL", LevelGenerator.currentRoomRow, LevelGenerator.currentRoomColumn - 1, false);
        //    Player.collisionBox.setX(MainClass.WINDOW_WIDTH - (Player.collisionBox.getWidth() + 10));
        //}
        
    }
      
    private void getInput(){
        MainClass.root.getScene().addEventHandler(KeyEvent.KEY_PRESSED, key -> {
            switch(key.getCode()){
                case D: movingRight = true; break;
                case A: movingLeft = true; break;
                case W:
                    if(onLadder){
                        climbingUp = true;
                    }
                    break;
                case S:
                    if(!onLadder && phys.isOnPlatform && !rootMontagePlaying && currentWeapon == weaponMode.UNARMED){
                        crouching = true;
                    } else if(onLadder){
                        climbingDown = true;
                    }
                    break;
                case SPACE:
                    if(onLadder){
                        onLadder = false;
                        climbingUp = false;
                        climbingDown = false;
                        collisionBox.setWidth(COLLISION_WIDTH);
                        sprManager.ivOffsetX = - 25;
                        phys.jump();
                        setSpriteSet("jump", 4, false);
                    } else if(phys.isOnPlatform && currentWeapon == weaponMode.UNARMED){
                        phys.jump(); 
                        setSpriteSet("jump", 4, false);
                    }
                    break;
                case Q:
                    if(phys.isOnPlatform && !onLadder && !rootMontagePlaying){
                        if(currentWeapon == weaponMode.UNARMED){
                            currentWeapon = weaponMode.SWORD;
                            setSpriteSet("drw", 4, false);
                        } else {
                            setSpriteSet("shte", 4, false);
                            currentWeapon = weaponMode.UNARMED;
                        }
                    }
                    break;
            }
        });
        
        MainClass.root.getScene().addEventHandler(KeyEvent.KEY_RELEASED, key -> {
            switch(key.getCode()){
                case D: movingRight = false; break;
                case A: movingLeft = false; break;
                case S: 
                    if(!onLadder){
                        crouching = false; 
                    } else {
                        climbingDown = false;
                    }
                    break;
                case W:
                    if(onLadder){
                        climbingUp = false;
                    }
                    break;
            }
        });
        
        MainClass.root.getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            switch(e.getButton()){
                case PRIMARY:
                    if(!rootMontagePlaying && currentWeapon == weaponMode.SWORD){
                        
                    }
                    break;
            }
        });
        
    }
    
    private static void ladderInteractions(){
        // Ladder Interaction
        if(!onLadder){
            for(int i = 0; i < LevelGenerator.ladderArray.size(); i++){
                if(collisionBox.getBoundsInParent().intersects(LevelGenerator.ladderArray.get(i).ladderCollision.getBoundsInParent()) 
                        && !phys.isOnPlatform && phys.downVelocity > 0.1){
                    currentLadder = LevelGenerator.ladderArray.get(i);
                    if(currentLadder.direction.equals("RIGHT") && sprManager.flipHorizontally == false){
                        onLadder = true;
                        collisionBox.setX(currentLadder.ladderCollision.getX() - 45);
                        collisionBox.setWidth(COLLISION_WIDTH - currentLadder.ladderThickness );
                        phys.downVelocity = 0;
                        sprManager.ivOffsetX = - 20;
                    } else if(currentLadder.direction.equals("LEFT") && sprManager.flipHorizontally == true){
                        onLadder = true;
                        collisionBox.setX(currentLadder.ladderCollision.getX() + 5);
                        collisionBox.setWidth(COLLISION_WIDTH - currentLadder.ladderThickness);
                        phys.downVelocity = 0;
                        sprManager.ivOffsetX = - 30;
                    }
                }
            }
        } else {
            if(!(climbingUp && climbingDown) && (climbingUp || climbingDown)){
                if(climbingUp){
                    phys.climbLadderDown(-1);
                    setSpriteSet("ladder-climb", 4, true);
                }
                if(climbingDown){
                    phys.climbLadderDown(1);
                    setSpriteSet("ladder-climb", 4, true);
                }
            } else {
                setSpriteSet("ladder-idle", 1, false);
            }
            
            if(phys.isOnPlatform){
                onLadder = false;
                climbingUp = false;
                climbingDown = false;
                collisionBox.setWidth(COLLISION_WIDTH);
                sprManager.ivOffsetX = - 25;
            }
        }
    }
    
    private static void setSpriteSet(String setIdentifier, int numberOfSprites, boolean loop){
        if((setIdentifier.equals(currentSpriteSet)) && currentWeapon == sprSetWeapon) // Force exit if we are already using the set
            return;
        
        if(currentSpriteSet.equals("drw") || currentSpriteSet.equals("shte")){ // Check if montage is playing
            if(sprManager.nextSpriteIndex < sprManager.spriteList.size() - 1){
                rootMontagePlaying = true;
                return;
            }
        }
        
        rootMontagePlaying = false;
        sprSetWeapon = currentWeapon;
        sprManager.loopAnimation = loop;
        currentSpriteSet = setIdentifier;
        sprManager.spriteList.removeAll(sprManager.spriteList);
        sprManager.nextSpriteIndex = 0;
        
        String spriteSetPrefix = "/images/player/";
        
        String fileNamePrefix = "adventurer-";
        
        switch(currentWeapon){
            case UNARMED:
                spriteSetPrefix = spriteSetPrefix + "unarmed/";
                fileNamePrefix = "adventurer-";
                break;
            case SWORD:
                fileNamePrefix = "adventurer-swrd-";
                spriteSetPrefix = spriteSetPrefix + "sword/";
                break;
        }
        
        spriteSetPrefix = spriteSetPrefix + fileNamePrefix + setIdentifier + "-";
        
        for(int i = 0; i < numberOfSprites; i++){
            String spriteSetPath = spriteSetPrefix;
            if(i < 10){
                spriteSetPath = spriteSetPath + "0" + i + ".png";
            } else {
                spriteSetPath = spriteSetPath + i + ".png";
            }
            sprManager.spriteList.add(spriteSetPath);
        }
    }
    
}
