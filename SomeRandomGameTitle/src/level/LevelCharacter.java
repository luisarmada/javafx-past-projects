package level;

import engine.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class LevelCharacter extends AnimationTimer{
 
    /* CHARACTER CLASS INITIALISATION BASE */
    //// Character Initialisation
    //Character character = new Character( /* Group, spawnX, spawnY, colBoxWidth, colBoxHeight, */ 0, 0, 0, 0,
    //                                /* spriteOffsetX, spriteOffsetY, spriteFlipOffset, */ 0, 0, 0,
    //                                /* runMovementSpeed, walkMovementSpeed, jumpIntensity, */ 0, 0, 0);
    //character.enableDebug = false; // Set to true to display colBox and other hidden movement check boxes used by PhysicsObject
    //// Initialise MovementAnim Info < use idleAnimInfo example above > (filePath, cols, rows, totalFrames, frameWidth, frameHeight, frameScale, framesPerSecond)
    //character.idleAnimInfo = new SpritesheetInformation();
    //character.runAnimInfo = new SpritesheetInformation();
    //character.walkAnimInfo = new SpritesheetInformation();
    //character.jumpAnimInfo = new SpritesheetInformation();
    //character.fallAnimInfo = new SpritesheetInformation();
    //character.start();
    
    public final Rectangle colBox; // The Collision Box of this character - used to collide with the blocks in the level
    
    public final PhysicsObject phys; // PhysicsObject manages all the movement of the character
    public boolean enableDebug = false; // Set to true to display colBox and other hidden movement check boxes used by PhysicsObject
    public final int jumpIntensity; // How high the character can jump
    public int moveRightInput = 0; // 0 = idle, 1 = moveRight, -1 = moveLeft
    public int runMovementSpeed; // Movement Speed used if isWalking = false
    public int walkMovementSpeed; // Movement Speed used if isWalking = true
    public boolean isWalking = false; // If true, will use walkMovementSpeed instead of runMovementSpeed
    public boolean wantsJump = false; // Character will attempt to jump (if possible) when this is true
    
    public final ImageView spriteIV = new ImageView(); // The image view used to display the sprite images
    public final SpriteAnimation spriteAnim; // SpriteAnimation manages the SpritesheetInformation into frames and displays them using spriteIV
    private final int spriteOffsetX; // spriteIV offset on the X-Axis from colBox
    private final int spriteOffsetY; // spriteIV offset on the Y-Axis from colBox
    private final int spriteFlipOffset; // adds to spriteOffsetX if spriteIV is flipped
    
    public boolean playMovementAnimations = true; // The standard movement animations will not play when this is false - use for overriding (e.g. attack animations)
    // SpritesheetInformation class is a struct used to pass on information about the spritesheet images to the SpriteAnimation class 
    public SpritesheetInformation idleAnimInfo;
    public SpritesheetInformation runAnimInfo;
    public SpritesheetInformation walkAnimInfo;
    public SpritesheetInformation jumpAnimInfo;
    public SpritesheetInformation fallAnimInfo;
    
    public LevelCharacter(Group group, int spawnX, int spawnY, int colBoxWidth, int colBoxHeight,
                        int spriteOffsetX, int spriteOffsetY, int spriteFlipOffset,
                        int runMovementSpeed, int walkMovementSpeed, int jumpIntensity){
        
        colBox = new Rectangle(spawnX, spawnY, colBoxWidth, colBoxHeight);
        phys = new PhysicsObject(colBox);
        this.runMovementSpeed = runMovementSpeed;
        this.walkMovementSpeed = walkMovementSpeed;
        this.jumpIntensity = jumpIntensity;
        
        spriteAnim = new SpriteAnimation(spriteIV, "/player/sprites/GraveRobber_idle.png", 0, 0, 0, 0, 0, 0, 1);
        this.spriteOffsetX = spriteOffsetX;
        this.spriteOffsetY = spriteOffsetY;
        this.spriteFlipOffset = spriteFlipOffset;
        
        phys.start();
        spriteAnim.start();
        group.getChildren().add(spriteIV);
    }

    @Override // Tick
    public void handle(long now) {
        
        // Attach spriteIV to the Collision Box <round to avoid smoothing errors>
        double spriteX = colBox.getX() + spriteOffsetX;
        spriteIV.setX(Math.round(spriteAnim.flipped ? spriteX + spriteFlipOffset : spriteX)); // Apply flip offset if needed
        spriteIV.setY(Math.round(colBox.getY() + spriteOffsetY));
        
        // Transfer variables to PhysicsObject class
        phys.moveRightInput = moveRightInput;
        phys.enableDebug = enableDebug;
        phys.movementSpeed = isWalking ? walkMovementSpeed : runMovementSpeed;
        
        if(wantsJump){ // Attempt to jump if wantsJump
            phys.jump(jumpIntensity);
        }
        
        // Sprite Animation handling
        if(playMovementAnimations){
            switch(phys.currentMovementState){
                case IDLE:
                    spriteAnim.changeSprite(spriteIV, idleAnimInfo.filePath, idleAnimInfo.totalCols, idleAnimInfo.totalRows, idleAnimInfo.totalFrames,
                    idleAnimInfo.frameWidth, idleAnimInfo.frameHeight, idleAnimInfo.spriteScale, idleAnimInfo.framesPerSecond);
                    break;
                case RUN:
                    if(((!isWalking && walkAnimInfo != null) || (isWalking && walkAnimInfo == null))){ // Use runAnim if walkAnim is null
                        if(runAnimInfo != null){
                           spriteAnim.changeSprite(spriteIV, runAnimInfo.filePath, runAnimInfo.totalCols, runAnimInfo.totalRows, runAnimInfo.totalFrames,
                           runAnimInfo.frameWidth, runAnimInfo.frameHeight, runAnimInfo.spriteScale, runAnimInfo.framesPerSecond); 
                        }
                    } else { // walkAnim
                        spriteAnim.changeSprite(spriteIV, walkAnimInfo.filePath, walkAnimInfo.totalCols, walkAnimInfo.totalRows, walkAnimInfo.totalFrames,
                        walkAnimInfo.frameWidth, walkAnimInfo.frameHeight, walkAnimInfo.spriteScale, walkAnimInfo.framesPerSecond);
                    }
                    break;
                case FALL:
                    if(fallAnimInfo != null){
                          spriteAnim.changeSprite(spriteIV, fallAnimInfo.filePath, fallAnimInfo.totalCols, fallAnimInfo.totalRows, fallAnimInfo.totalFrames,
                            fallAnimInfo.frameWidth, fallAnimInfo.frameHeight, fallAnimInfo.spriteScale, fallAnimInfo.framesPerSecond);
                            spriteAnim.loop = false;
                    }
                    break;
                case JUMP:
                    if(jumpAnimInfo != null){
                        spriteAnim.changeSprite(spriteIV, jumpAnimInfo.filePath, jumpAnimInfo.totalCols, jumpAnimInfo.totalRows, jumpAnimInfo.totalFrames,
                        jumpAnimInfo.frameWidth, jumpAnimInfo.frameHeight, jumpAnimInfo.spriteScale, jumpAnimInfo.framesPerSecond);
                        spriteAnim.loop = false;
                    }
                    break;
            }
        }
        
        // Flip sprite if character is moving left
        if(moveRightInput == -1){
            spriteAnim.flipped = true;
        } else if(moveRightInput == 1){
            spriteAnim.flipped = false;
        }
        
    }
}
