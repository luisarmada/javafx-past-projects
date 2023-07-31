package entities;

import static java.lang.Math.abs;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import level.LevelGenerator;

public class PhysicsManager {

    // The 'rectangles' which this class manages
    public static ArrayList<Rectangle> childList = new ArrayList();
    public static ArrayList<Boolean> isActive = new ArrayList();
    
    // Variables for each individual child at its index
    static ArrayList<Double> downVelocities = new ArrayList();
    public static ArrayList<Double> rightVelocities = new ArrayList();
    public static ArrayList<Double> maxRightVelocities = new ArrayList();
    
    static ArrayList<Boolean> applyGravity = new ArrayList();
    public static ArrayList<Boolean> movingLeft = new ArrayList();
    public static ArrayList<Boolean> movingRight = new ArrayList();
    public static ArrayList<Boolean> lockMovement = new ArrayList();
    public static ArrayList<Boolean> isOnPlatform = new ArrayList();
    public static ArrayList<Boolean> wantsJump = new ArrayList();
    static ArrayList<Boolean> isBeingKnockedBack = new ArrayList();

    public static ArrayList<Double> downwardsAcceleration = new ArrayList();
    public static ArrayList<Double> knockbackStrength = new ArrayList();
    
    static final double MAX_DOWN_VEL = 13000;
    final double LADDER_CLIMB_SPEED = 5;
    //static final double MAX_RIGHT_VEL = 5;
    //static double downwardsAcceleration = 0.5;
    static double jumpForce = 12;
    
    public static int addChild(Rectangle newChild, double movementSpeed){
        // Add variables to the array lists. Entites can access their individual variables using the returned index.
        childList.add(newChild);
        isActive.add(true);
        downVelocities.add(0.0);
        applyGravity.add(true);
        isOnPlatform.add(false);
        rightVelocities.add(movementSpeed);
        movingLeft.add(false);
        movingRight.add(false);
        wantsJump.add(false);
        maxRightVelocities.add(movementSpeed);
        isBeingKnockedBack.add(false);
        lockMovement.add(false);
        downwardsAcceleration.add(0.5);
        knockbackStrength.add(0.0);
        
        return childList.indexOf(newChild);
    }
    
    
    public static void update(){
        
        for(int i = 0; i < childList.size(); i++){ // Update for each child
            if(isActive.get(i)){
                
                if(maxRightVelocities.get(i) < 0){
                    maxRightVelocities.set(i, 0.0);
                }
                
                // GRAVITY APPLICATION
                if(applyGravity.get(i)){
                    // Add gravity with increasing acceleration.
                    childList.get(i).setY(childList.get(i).getY() + movableSpacesInDirection("VERTICAL", i)); // Checks movable spaces in direction
                    if((downVelocities.get(i) < MAX_DOWN_VEL)){
                        downVelocities.set(i, downVelocities.get(i) + downwardsAcceleration.get(i)); // Increase rate of fall until max down velocity.
                    }
                }
                
                if(isBeingKnockedBack.get(i)){
                    childList.get(i).setX(childList.get(i).getX() + movableSpacesInDirection("HORIZONTAL", i));
                    if(rightVelocities.get(i) != 0){
                        if(knockbackStrength.get(i) > 0){
                            if(rightVelocities.get(i) > 0){
                                rightVelocities.set(i, rightVelocities.get(i) - 0.1);
                            } else {
                                rightVelocities.set(i, 0.0);
                            }
                        } else {
                            if(rightVelocities.get(i) < 0){
                                rightVelocities.set(i, rightVelocities.get(i) + 0.1);
                            } else {
                                rightVelocities.set(i, 0.0);
                            }
                        }
                        
                    } else {
                        // Finish Knockback
                        isBeingKnockedBack.set(i, false);
                        lockMovement.set(i, false);
                    }
                }
                
                // LEFT AND RIGHT MOVEMENTS
                boolean isMovingLeft = movingLeft.get(i);
                boolean isMovingRight = movingRight.get(i);
                // Check if any movement input is being pressed
                if(!(isMovingLeft && isMovingRight)){
                    if(!isBeingKnockedBack.get(i)){
                        rightVelocities.set(i, 0.0);
                    }
                    if((isMovingLeft || isMovingRight) && !lockMovement.get(i)){
                        if(isMovingLeft){
                            rightVelocities.set(i, maxRightVelocities.get(i) * -1); // Move Left
                            childList.get(i).setX(childList.get(i).getX() + movableSpacesInDirection("HORIZONTAL", i));
                        } else {
                            rightVelocities.set(i, maxRightVelocities.get(i)); // Move Right
                            childList.get(i).setX(childList.get(i).getX() + movableSpacesInDirection("HORIZONTAL", i));
                        } 
                    }

                    if(wantsJump.get(i) && !lockMovement.get(i)){
                        if(isOnPlatform.get(i)){
                            downVelocities.set(i, -jumpForce);
                        }
                    }
                } else {
                    rightVelocities.set(i, 0.0);
                }
            }
        }
    }
    
    public static double movableSpacesInDirection(String dir, int childIndex){
        
        Rectangle target = childList.get(childIndex); 
        
        double rightVelocity = rightVelocities.get(childIndex);
           
        switch(dir){
            case "VERTICAL":
                if(downVelocities.get(childIndex) >= 0){ // Down collision tester and checks if player is on platform
                    Rectangle collisionTester = new Rectangle(target.getX(), target.getY() + target.getHeight(), target.getWidth(), downVelocities.get(childIndex));
                    for(double i = collisionTester.getHeight(); i > 0; i -= downwardsAcceleration.get(childIndex)){
                        collisionTester.setHeight(i);
                        ArrayList overlapCollision = new ArrayList(); 
                        
                        for(Rectangle colBlocks : LevelGenerator.colBlockArray){
                            overlapCollision.add(colBlocks.getBoundsInParent().intersects(collisionTester.getBoundsInParent()));
                        }
                        if(!overlapCollision.contains(true)){
                            isOnPlatform.set(childIndex, false);
                            return i;
                        } else {
                            isOnPlatform.set(childIndex, true);
                        }
                    }
                    
                    if(downVelocities.get(childIndex) != 0){ //<- This check is to stop isOnPlatform from becoming true whilst player is at the peak of their jump
                        isOnPlatform.set(childIndex, true);
                    }
                    
                    downVelocities.set(childIndex, 0.0);
                    return 0;
                } else { // Upwards collision tester
                    Rectangle collisionTester = new Rectangle(target.getX(), target.getY() + downVelocities.get(childIndex), target.getWidth(), abs(downVelocities.get(childIndex)));
                    for(double i = collisionTester.getHeight(); i > 0; i -= downwardsAcceleration.get(childIndex)){
                        collisionTester.setHeight(i);
                        collisionTester.setY(target.getY() - i);
                        ArrayList overlapCollision = new ArrayList(); 
                        
                        for(Rectangle colBlocks : LevelGenerator.colBlockArray){
                            overlapCollision.add(colBlocks.getBoundsInParent().intersects(collisionTester.getBoundsInParent()));
                        }
                        if(!overlapCollision.contains(true)){
                            isOnPlatform.set(childIndex, false);
                            return -i;
                        }
                    }
                    downVelocities.set(childIndex, 0.0);
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
                    rightVelocities.set(childIndex, 0.0);
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
                    rightVelocities.set(childIndex, 0.0);
                    return 0;
                } else { 
                    return 0; 
                }
                
        }
            
        return 0;
    }
    
    public static void addKnockback(int childIndex, double kbStrength, boolean toTheRight){
        isBeingKnockedBack.set(childIndex, true);
        lockMovement.set(childIndex, true);
       
        
        if(toTheRight){
            rightVelocities.set(childIndex, kbStrength);
            knockbackStrength.set(childIndex, kbStrength);
        } else {
            rightVelocities.set(childIndex, -kbStrength);
            knockbackStrength.set(childIndex, -kbStrength);
        }
        
    }
}   
