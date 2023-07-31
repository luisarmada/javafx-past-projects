package engine;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import level.LevelGenerator;

public class PhysicsObject extends AnimationTimer {
    
    private final Rectangle colBox;
    public final Rectangle xMoveCheck; // A rectangle used to check how many spaces horizontally this object can move
    public final Rectangle yMoveCheck; // A rectangle used to check how many spaces vertically this object can move
    private final Rectangle slopeMoveCheck;
    private final Rectangle onPlatformCollisionCheck;
    public boolean enableDebug = false;
    public boolean applyGravity = true;
    public boolean allowJumpWithoutGravity = false;
    
    public int movementSpeed = 5;
    public int moveRightInput = 0; // -1 = left, 0 = none, 1 = right
    
    public double yVelocity = 0;
    public double xVelocity = 0;
    private final double yAcceleration = 0.5; //0.5
    private final int maxVelY = 100; // Y Max Velocity
    public final int slopeClimbHeight = 10;
    
    public boolean canJump = false;
    public boolean ignorePlatforms = false;
    public boolean canGoUpSlopes = true;
    public boolean isGoingUpSlope = false;
    
    private double lastYCheckOnPlatform, lastXCheckOnPlatform;
    
    public enum movementState{
        IDLE, RUN, JUMP, FALL
    }
    public movementState currentMovementState = movementState.IDLE;
    
    public PhysicsObject(Rectangle collisionBox){
        colBox = collisionBox;
        colBox.setFill(Color.web("#8E44AD"));
        
        yMoveCheck = new Rectangle();
        yMoveCheck.setFill(Color.web("#F4D03F"));
        
        xMoveCheck = new Rectangle();
        xMoveCheck.setFill(Color.web("#F4D03F"));
        
        slopeMoveCheck = new Rectangle();
        slopeMoveCheck.setFill(Color.web("#3498DB"));
        
        onPlatformCollisionCheck = new Rectangle();
        onPlatformCollisionCheck.setFill(Color.web("#D35400"));
        
        Main.addToLevelGroup(colBox, xMoveCheck, yMoveCheck, slopeMoveCheck, onPlatformCollisionCheck);
    }
    
    @Override
    public void handle(long now){
        
        int collisionTesterOpacities = enableDebug ? 1 : 0;
        colBox.setOpacity(collisionTesterOpacities);
        xMoveCheck.setOpacity(collisionTesterOpacities);
        yMoveCheck.setOpacity(collisionTesterOpacities);
        slopeMoveCheck.setOpacity(collisionTesterOpacities);
        onPlatformCollisionCheck.setOpacity(collisionTesterOpacities);
        if(enableDebug){ // Un-needed location setting for display purposes
            xMoveCheck.setY(colBox.getY());
            slopeMoveCheck.setOpacity(colBox.getY());
            slopeMoveCheck.setY(xMoveCheck.getY() + xMoveCheck.getHeight());
            yMoveCheck.setX(colBox.getX());
        }
        
        // Becomes false whenever the player moves and onPlatformCheck() hasn't been run yet
        boolean onPlatformLastChecked = (yVelocity == 0 && lastXCheckOnPlatform == colBox.getX() && lastYCheckOnPlatform == colBox.getY());
        
        if((!isGoingUpSlope && applyGravity /*&& !onPlatformLastChecked*/) || ignorePlatforms){
            if(yVelocity != 0){
                addGravity();
            } else if(onPlatformCheck()){
                //lastXCheckOnPlatform = colBox.getX();
                //lastYCheckOnPlatform = colBox.getY();
                canJump = true;
            } else {
                addGravity();
            }
        }
        
        if(moveRightInput == 0){
            xVelocity = 0;
            if(canJump){
                currentMovementState = movementState.IDLE;
            }
        } else {
            if(canJump){
                currentMovementState = movementState.RUN;
            }
            if(moveRightInput > 0){
                xVelocity = movableSpacesX(movementSpeed);
                colBox.setX(colBox.getX() + xVelocity);
            } else {
                xVelocity = movableSpacesX(-movementSpeed);
                colBox.setX(colBox.getX() + xVelocity);
            }
            if(canGoUpSlopes && applyGravity){
                colBox.setY((xMoveCheck.getY() + xMoveCheck.getHeight() + slopeMoveCheck()) - colBox.getHeight());
            }
        }
        
    }
    
    private void addGravity(){
        yVelocity = movableSpacesY(yVelocity + yAcceleration);
        colBox.setY(colBox.getY() + yVelocity);
        canJump = false;
        if(yVelocity > 0.5){
            currentMovementState = movementState.FALL;
        } else if(yVelocity < -0.5){ // To stop the bounce from switching between jump and idle animations
            currentMovementState = movementState.JUMP;
        }
    }
    
    public void jump(double intensity){
        if((!applyGravity && allowJumpWithoutGravity)){
            yVelocity = movableSpacesY(-intensity);
            canJump = false;
        } else if(canJump && yVelocity == 0 && applyGravity){
            yVelocity = movableSpacesY(-intensity);
            canJump = false;
        }
    }
    
    public double movableSpacesY(double desiredSpaces){
        if(desiredSpaces == 0){return 0;}
        
        double desSpacesAbs = Math.abs(desiredSpaces);
        boolean downwardCheck = desiredSpaces > 0;
        yMoveCheck.setX(colBox.getX());
        yMoveCheck.setWidth(colBox.getWidth());
        
        yMoveCheck.setHeight(desSpacesAbs < maxVelY ? desSpacesAbs : maxVelY);
        yMoveCheck.setY(downwardCheck ? colBox.getY() + colBox.getHeight() : colBox.getY() - yMoveCheck.getHeight());
        
        boolean yCheckOverlap = false;
        boolean hit = false;
        do {
            for(Shape colShape : LevelGenerator.colShapeList){
                yCheckOverlap = yMoveCheck.getBoundsInParent().intersects(colShape.getBoundsInParent());
                if(yCheckOverlap){
                   hit = true;
                   if(downwardCheck){
                       canJump = true;
                   }
                }
            }
            
            if(downwardCheck && !ignorePlatforms){
                for(Shape platformShape : LevelGenerator.platformShapeList){
                    yCheckOverlap = yMoveCheck.getBoundsInParent().intersects(platformShape.getBoundsInParent());
                    if(yCheckOverlap){
                        hit = true;
                        canJump = true;
                    }
                }
            }
                
            if(hit){
                yMoveCheck.setHeight(yMoveCheck.getHeight() - 1);
                if(!downwardCheck){
                    yMoveCheck.setY(colBox.getY() - yMoveCheck.getHeight());
                }
            }
        } while(hit && yMoveCheck.getHeight() > 0);
        
        return downwardCheck ? yMoveCheck.getHeight() : -yMoveCheck.getHeight();
    }
    
    private double slopeMoveCheck(){
        
        boolean downwardCheck = true;
        boolean rightCheck = xVelocity > 0;
        slopeMoveCheck.setX(xMoveCheck.getX());
        slopeMoveCheck.setWidth(xMoveCheck.getWidth());
        
        slopeMoveCheck.setHeight(slopeClimbHeight);
        slopeMoveCheck.setY(xMoveCheck.getY() + xMoveCheck.getHeight());
        
        boolean slopeCheckOverlap = false;
        boolean hit = false;
        do {
            for(Shape colShape : LevelGenerator.colShapeList){
                slopeCheckOverlap = slopeMoveCheck.getBoundsInParent().intersects(colShape.getBoundsInParent());
                if(slopeCheckOverlap){
                   hit = true;
                }
            }
            if(hit){
                slopeMoveCheck.setHeight(slopeMoveCheck.getHeight() - 1);
            }
        } while(hit && slopeMoveCheck.getHeight() > 0);
        
        isGoingUpSlope = (slopeMoveCheck.getHeight() < slopeClimbHeight);
        return slopeMoveCheck.getHeight();
    }
    
    public double movableSpacesX(double desiredSpaces){
        if(desiredSpaces == 0){return 0;}
        
        double desSpacesAbs = Math.abs(desiredSpaces);
        boolean rightCheck = desiredSpaces > 0;
        
        xMoveCheck.setY(colBox.getY());
        xMoveCheck.setHeight(canGoUpSlopes ? colBox.getHeight() - slopeClimbHeight : colBox.getHeight());
        
        xMoveCheck.setWidth(desSpacesAbs);
        xMoveCheck.setX(rightCheck ? colBox.getX() + colBox.getWidth() : colBox.getX() - xMoveCheck.getWidth());
        
        boolean xCheckOverlap = false;
        boolean hit = false;
        do {
            
            for(Shape colShape : LevelGenerator.colShapeList){
                xCheckOverlap = xMoveCheck.getBoundsInParent().intersects(colShape.getBoundsInParent());
                if(xCheckOverlap){
                    hit = true;
                }
            }
            if(hit){
                xMoveCheck.setWidth(xMoveCheck.getWidth() - 1);
                boolean collisionRetest = false;
                for(Shape colShape : LevelGenerator.colShapeList){
                    xCheckOverlap = xMoveCheck.getBoundsInParent().intersects(colShape.getBoundsInParent());
                    if(xCheckOverlap){
                        collisionRetest = true;
                    }
                }
                hit = collisionRetest;
                if(!rightCheck){
                    xMoveCheck.setX(colBox.getX() - xMoveCheck.getWidth());
                }
            }
        } while(hit && xMoveCheck.getWidth() > 0);
        
        return rightCheck ? xMoveCheck.getWidth() : -xMoveCheck.getWidth();
    }
    
    public boolean onPlatformCheck(){
        onPlatformCollisionCheck.setX(colBox.getX());
        onPlatformCollisionCheck.setY(colBox.getY() + colBox.getHeight()); // Underneath collision box
        onPlatformCollisionCheck.setWidth(colBox.getWidth());
        onPlatformCollisionCheck.setHeight(1);
        
        if(LevelGenerator.colShapeList.stream().anyMatch((colShape) -> (onPlatformCollisionCheck.getBoundsInParent().intersects(colShape.getBoundsInParent())))) {
            return true;
        } else if(ignorePlatforms){
            return false;
        } else {
            return LevelGenerator.platformShapeList.stream().anyMatch((platShape) -> (onPlatformCollisionCheck.getBoundsInParent().intersects(platShape.getBoundsInParent()))); //lastYCheckOnPlatform = colBox.getY();
        }
    }
    
}
