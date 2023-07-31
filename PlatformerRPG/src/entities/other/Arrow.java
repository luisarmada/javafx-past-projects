package entities.other;

import engine.*;
import entities.PhysicsManager;
import entities.SpriteManager;
import entities.enemies.EnemyManager;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public class Arrow {
    
    static ArrayList<Rectangle> arrowList = new ArrayList();
    static ArrayList<Integer> physManagerIndex = new ArrayList();
    static ArrayList<Integer> sprManagerIndex = new ArrayList();
    static ArrayList<Boolean> travellingRight = new ArrayList();
    static ArrayList<Boolean> isActive = new ArrayList();
    static ArrayList<ArrayList<Rectangle>> arrowHits = new ArrayList();
    
    public static void createArrow(double x, double y, boolean rightDir, double damage){
        int thisIndex = arrowList.size();
        Rectangle collisionBox = new Rectangle(x, y, 35, 8);
        //RenderEngine.addToGroup("LV", collisionBox);
        arrowList.add(collisionBox);
        travellingRight.add(rightDir);
        isActive.add(true);
        arrowHits.add(new ArrayList());
        
        physManagerIndex.add(PhysicsManager.addChild(collisionBox, 10));
        PhysicsManager.downwardsAcceleration.set(physManagerIndex.get(thisIndex), 0.04);
        if(rightDir){
            PhysicsManager.movingRight.set(physManagerIndex.get(thisIndex), true);
        } else {
            PhysicsManager.movingLeft.set(physManagerIndex.get(thisIndex), true);
        }
        
        sprManagerIndex.add(SpriteManager.createNewSprite(collisionBox, 5, 0, 40, 40, -14));
        SpriteManager.flipped.set(sprManagerIndex.get(thisIndex), !rightDir);
        SpriteManager.setSpriteSet(sprManagerIndex.get(thisIndex), "player_attacks", "arrow", 0, false);
    }
    
    public static void update(){
        for(int i = 0; i < arrowList.size(); i++){
            if(PhysicsManager.rightVelocities.get(physManagerIndex.get(i)) == 0){
                PhysicsManager.isActive.set(physManagerIndex.get(i), false);
                isActive.set(i, false);
            } else {
                if(PhysicsManager.isOnPlatform.get(physManagerIndex.get(i))){
                    int impaleGroundRotation = 9;
                    if((travellingRight.get(i) && arrowList.get(i).getRotate() > impaleGroundRotation) ||
                            (!travellingRight.get(i) && arrowList.get(i).getRotate() < -impaleGroundRotation)){
                        PhysicsManager.isActive.set(physManagerIndex.get(i), false);
                        isActive.set(i, false);
                    } else {
                        arrowList.get(i).setRotate(0);
                        PhysicsManager.maxRightVelocities.set(physManagerIndex.get(i), PhysicsManager.maxRightVelocities.get(physManagerIndex.get(i)) - 0.25);
                    }
                    
                } else {
                    int maxArrowRotate = 90;
                    if((arrowList.get(i).getRotate() > -maxArrowRotate && arrowList.get(i).getRotate() < maxArrowRotate)){
                        if(travellingRight.get(i)){
                            arrowList.get(i).setRotate(arrowList.get(i).getRotate() + 0.3);
                        } else {
                            arrowList.get(i).setRotate(arrowList.get(i).getRotate() - 0.3);
                        }
                    }
                }
                
            }
            
            if(isActive.get(i)){
                for(int j = 0; j < EnemyManager.enemyColBlocks.size(); j++){
                    if(arrowList.get(i).getBoundsInParent().intersects(
                        EnemyManager.enemyColBlocks.get(j).getBoundsInParent())){
                        if(!arrowHits.get(i).contains(EnemyManager.enemyColBlocks.get(j)) && EnemyManager.health.get(j) > 0){
                            arrowHits.get(i).add(EnemyManager.enemyColBlocks.get(j));
                            
                            // Add knockback to enemy
                            PhysicsManager.addKnockback(EnemyManager.physManagersIndex.get(j),
                                    EnemyManager.knockbackStrengths.get(j), //Knockback strength
                                    arrowList.get(i).getX() < EnemyManager.enemyColBlocks.get(j).getX());
                            
                            EnemyManager.health.set(j, EnemyManager.health.get(j) - 25);
                            
                            SpriteManager.setSpriteSet(EnemyManager.sprManagersIndex.get(j), EnemyManager.spriteFolder.get(j), "hurt", EnemyManager.spriteSetLengths.get(j).get(2));
                            SpriteManager.playingActionAnimation.set(EnemyManager.sprManagersIndex.get(j), true);
                            
                        }
                    }
                }
            }
            
        }
    }
    
}
