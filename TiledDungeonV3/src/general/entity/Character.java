package general.entity;

import general.Main;
import general.rendering.Tile;
import general.entity.SpriteAnimation;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Character extends AnimationTimer{
    
    private final Rectangle COLBOX;
    public double xPos, yPos;
    public double midXPos, midYPos;
    public final int cbWidth, cbHeight;
    public Tile currentTile;
    
    public boolean isMoving = false;
    private final Rectangle xMoveCheck, yMoveCheck;
    public double moveAngle = 0;
    private final int moveSpeed;
    
    public boolean destroyOnCollision = false;
    
    public Group grp;
    
    public SpriteAnimation spriteAnim = new SpriteAnimation();
    private ImageView ivSprite = new ImageView();
    private ArrayList<Image> idleSprites, walkSprites;
    private int idleSpriteDelay, walkSpriteDelay;
    private int spriteXOffset, spriteYOffset, flipXOffset;
    
    Character(Group grp, double x, double y, int w, int h, int speed){
        this.grp = grp;
        moveSpeed = speed;
        
        cbWidth = w;
        cbHeight = h;
        COLBOX = new Rectangle(x, y, w, h);
        updateMoveVars();
        COLBOX.setFill(Color.web("#7D3C98"));
        
        xMoveCheck = new Rectangle();
        yMoveCheck = new Rectangle();
    }
    
    @Override
    public void handle(long now) {
        
        updateMoveVars();
        ivSprite.setX(ivSprite.getScaleX() == 1 ? xPos + spriteXOffset : xPos + flipXOffset);
        ivSprite.setY(yPos + spriteYOffset);
        
        if(isMoving){

            spriteAnim.setSpriteAnim(ivSprite, walkSprites, walkSpriteDelay);
            
            // Move Character
            double speedXMultiplier = Math.cos(Math.toRadians(moveAngle));
            double speedYMultiplier = Math.sin(Math.toRadians(moveAngle));
            double xMoveBy = tryMoveX((int) Math.round(moveSpeed * speedXMultiplier));
            double yMoveBy = tryMoveY((int) Math.round(moveSpeed * speedYMultiplier));
            COLBOX.setX(xPos + xMoveBy);
            COLBOX.setY(yPos + yMoveBy);
            if(destroyOnCollision && (xMoveBy == 0 && yMoveBy == 0)){
                destroy();
            }
            if(xMoveBy != 0){
               if(xMoveBy < 0){
                    ivSprite.setScaleX(-1);
                } else {
                    ivSprite.setScaleX(1);
                } 
            }
            
        } else {
            spriteAnim.setSpriteAnim(ivSprite, idleSprites, idleSpriteDelay);
        }
        
    }
    
    public void setupSprites(ArrayList<Image> idleSprites, int idleDelay,  ArrayList<Image> walkSprites, int walkDelay, int scale, int xOffset, int yOffset, int flipXOffset){
        this.idleSprites = idleSprites;
        this.walkSprites = walkSprites;
        idleSpriteDelay = idleDelay;
        walkSpriteDelay = walkDelay;
        spriteXOffset = xOffset;
        spriteYOffset = yOffset;
        this.flipXOffset = flipXOffset;
        
        ivSprite.setFitWidth(32 * scale);
        ivSprite.setFitHeight(32 * scale);
        ivSprite.setPreserveRatio(true);
        grp.getChildren().add(ivSprite);
        spriteAnim.start();
    }
    
    private void updateMoveVars(){
        xPos = COLBOX.getX();
        yPos = COLBOX.getY();
        midXPos = xPos + (COLBOX.getWidth()/2);
        midYPos = yPos + (COLBOX.getHeight()/2);
        
        // Calculate Current Tile
        int lvW = Main.lGen.wTiles * Main.lGen.T_SIZE;
        int lvH = Main.lGen.hTiles * Main.lGen.T_SIZE;
        currentTile = Main.lGen.getTileAt((int)Math.round((xPos/lvW) * Main.lGen.wTiles),
                (int)Math.round((yPos/lvH) * Main.lGen.hTiles));
    }
    
    private int tryMoveX(int desired){
        if(desired == 0){return 0;}
        
        boolean isPositive = desired < 0;
        int absDesired = Math.abs(desired);
        xMoveCheck.setX(isPositive ? COLBOX.getX() - absDesired : COLBOX.getX() + COLBOX.getWidth());
        xMoveCheck.setY(COLBOX.getY());
        xMoveCheck.setWidth(absDesired);
        xMoveCheck.setHeight(COLBOX.getHeight());
        
       
        for (int i = currentTile.gx - 1; i < currentTile.gx + 1; i++) {
            for (int j = currentTile.gy - 1; j < currentTile.gy + 1; j++) {
                Tile checkTile = Main.lGen.getTileAt(i, j);
                if(checkTile != null){
                    if(checkTile.tState == 1){
                        if(xMoveCheck.getBoundsInParent().intersects(checkTile.tileBox.getBoundsInParent())){
                            if(destroyOnCollision){
                                destroy();
                            }
                            return 0;
                        }
                    }
                }
            }
        }
        return desired;
    }
    
    private int tryMoveY(int desired){
        if(desired == 0){return 0;}
        
        boolean isPositive = desired < 0;
        int absDesired = Math.abs(desired);
        yMoveCheck.setY(isPositive ? COLBOX.getY() - absDesired : COLBOX.getY() + COLBOX.getHeight());
        yMoveCheck.setX(COLBOX.getX());
        yMoveCheck.setHeight(absDesired);
        yMoveCheck.setWidth(COLBOX.getWidth());
        
        for (int i = currentTile.gx - 1; i < currentTile.gx + 1; i++) {
            for (int j = currentTile.gy - 1; j < currentTile.gy + 1; j++) {
                    Tile checkTile = Main.lGen.getTileAt(i, j);
                    if(checkTile != null){
                        if(checkTile.tState == 1){
                            if(yMoveCheck.getBoundsInParent().intersects(checkTile.tileBox.getBoundsInParent())){
                                if(destroyOnCollision){
                                    destroy();
                                }
                                return 0;
                            }
                        }
                    }
            }
        }
        return desired;
    }
    
    public void destroy(){
        COLBOX.setFill(Color.TRANSPARENT);
        grp.getChildren().remove(COLBOX);
        spriteAnim.stop();
        stop();
    }
    
    public void setPos(double x, double y){
        xPos = x;
        yPos = y;
        COLBOX.setX(xPos);
        COLBOX.setY(yPos);
    }
}
