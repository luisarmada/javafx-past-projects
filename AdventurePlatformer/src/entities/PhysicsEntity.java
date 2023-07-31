package entities;

import engine.MainClass;
import static java.lang.Math.abs;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import level.LevelGenerator;

public class PhysicsEntity {
    
    double downVelocity = 0;
    double rightVelocity = 0;
    final double MAX_DOWN_VEL = 13000;
    final double LADDER_CLIMB_SPEED = 5;
    final double MAX_RIGHT_VEL = 5;
    double downwardsAcceleration = 0.5;
    double jumpForce = 12;
    
    boolean canMoveUp, isOnPlatform, canMoveLeft, canMoveRight;
    
    Rectangle target;
    
    PhysicsEntity(Rectangle newTarget){
        target = newTarget;
    }
    
    public void addGravity(){
        target.setY(target.getY() + movableSpacesInDirection("VERTICAL")); 
        if((downVelocity < MAX_DOWN_VEL)){
            downVelocity += downwardsAcceleration; // Increase rate of fall until max down velocity.
        }
    }
        
    public void moveRight(int scale){
        rightVelocity = MAX_RIGHT_VEL * scale;
        target.setX(target.getX() + movableSpacesInDirection("HORIZONTAL"));
    }
    
    public void climbLadderDown(int scale){
        downVelocity = LADDER_CLIMB_SPEED * scale;
        
        double y = 0;
        if(scale > 0){ // Moving Down
            y = target.getY() + target.getHeight();
        } else {
            y = target.getY()-5;
        }
        Rectangle climbTest = new Rectangle(target.getX() - 5, y, target.getWidth() + 10, 5);
        //MainClass.addToRoot(climbTest);
        for(int i = 0; i < LevelGenerator.ladderArray.size(); i++){
            if(climbTest.getBoundsInParent().intersects(LevelGenerator.ladderArray.get(i).ladderCollision.getBoundsInParent())) {
                target.setY(target.getY() + movableSpacesInDirection("VERTICAL"));
                return;
            }
        }
    }
    
    public void jump(){
        downVelocity = -jumpForce;
    }
    
    public double movableSpacesInDirection(String dir){
        
        switch(dir){
            case "VERTICAL":
                if(downVelocity >= 0){ // Down collision tester and checks if player is on platform
                    Rectangle collisionTester = new Rectangle(target.getX(), target.getY() + target.getHeight(), target.getWidth(), downVelocity);
                    for(double i = collisionTester.getHeight(); i > 0; i -= downwardsAcceleration){
                        collisionTester.setHeight(i);
                        ArrayList overlapCollision = new ArrayList(); 
                        
                        for(Rectangle colBlocks : LevelGenerator.colBlockArray){
                            overlapCollision.add(colBlocks.getBoundsInParent().intersects(collisionTester.getBoundsInParent()));
                        }
                        if(!overlapCollision.contains(true)){
                            isOnPlatform = false;
                            return i;
                        }
                    }
                    
                    if(downVelocity != 0){ // To stop isOnPlatform from becoming true whilst player is at the peak of their jump
                        isOnPlatform = true;
                    }
                    
                    downVelocity = 0;
                    return 0;
                } else { // Upwards collision tester
                    Rectangle collisionTester = new Rectangle(target.getX(), target.getY() + downVelocity, target.getWidth(), abs(downVelocity));
                    for(double i = collisionTester.getHeight(); i > 0; i -= downwardsAcceleration){
                        collisionTester.setHeight(i);
                        collisionTester.setY(target.getY() - i);
                        ArrayList overlapCollision = new ArrayList(); 
                        
                        for(Rectangle colBlocks : LevelGenerator.colBlockArray){
                            overlapCollision.add(colBlocks.getBoundsInParent().intersects(collisionTester.getBoundsInParent()));
                        }
                        if(!overlapCollision.contains(true)){
                            isOnPlatform = false;
                            return -i;
                        }
                    }
                    downVelocity = 0;
                    return 0;
                }
            case "HORIZONTAL":
                if(rightVelocity > 0){ // Right collision test
                    Rectangle collisionTester = new Rectangle(target.getX() + target.getWidth(), target.getY(), rightVelocity, target.getHeight());
                    for(double i = collisionTester.getWidth(); i > 0; i -= 1){
                        collisionTester.setWidth(i);
                        ArrayList overlapCollision = new ArrayList(); 
                        
                        for(Rectangle colBlocks : LevelGenerator.colBlockArray){
                            overlapCollision.add(colBlocks.getBoundsInParent().intersects(collisionTester.getBoundsInParent()));
                        }
                        if(!overlapCollision.contains(true)){
                            return i;
                        }
                    }
                    rightVelocity = 0;
                    return 0;
                } else if(rightVelocity < 0){ // Left collision test
                    Rectangle collisionTester = new Rectangle(target.getX() - rightVelocity, target.getY(), abs(rightVelocity), target.getHeight());
                    for(double i = collisionTester.getWidth(); i > 0; i -= 1){
                        collisionTester.setWidth(i);
                        collisionTester.setX(target.getX() - i);
                        ArrayList overlapCollision = new ArrayList(); 
                        
                        for(Rectangle colBlocks : LevelGenerator.colBlockArray){
                            overlapCollision.add(colBlocks.getBoundsInParent().intersects(collisionTester.getBoundsInParent()));
                        }
                        if(!overlapCollision.contains(true)){
                            return -i;
                        }
                    }
                    rightVelocity = 0;
                    return 0;
                } else { return 0; }
        }
            
        return 0;
    }
    

}

