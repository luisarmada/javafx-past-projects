package entities.enemies;

import entities.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Rectangle;
import level.LevelGenerator;

public class EnemyManager {
    
    public static ArrayList<Rectangle> enemyColBlocks = new ArrayList();
    public static ArrayList<Integer> sprManagersIndex = new ArrayList();
    public static ArrayList<Integer> physManagersIndex = new ArrayList();
    public static ArrayList<String> spriteFolder = new ArrayList();
    public static ArrayList<Double> knockbackStrengths = new ArrayList();
    
    public static ArrayList<Double> health = new ArrayList();
    public static ArrayList<Double> maxHealth = new ArrayList();
    
    public static ArrayList<Integer> attackDisMin = new ArrayList();
    public static ArrayList<Integer> attackDisMax = new ArrayList();
    
    public static ArrayList<List<Integer>> spriteSetLengths = new ArrayList();
    // Order: 0 Idle, 1 Move, 2 Hurt, 3 Die, 4 Attack
    
    static ArrayList<Integer> enemyWidths = new ArrayList();
    
    public enum enemyTypes{
        BLUE_SLIME, MINOTAUR
    }
    
    static int spawnX;
    static int spawnY;
    public static void spawnEnemy(enemyTypes type, int xLoc, int yLoc){
        Rectangle colRect = null;
        
        int thisIndex = enemyColBlocks.size();
        
        spawnX = xLoc; spawnY = (yLoc + LevelGenerator.BLOCK_SIZE);
        
        switch(type){
            case BLUE_SLIME:
                addSpriteLengths(3, //Idle
                        3, // Move
                        3, // Hurt
                        3, // Die
                        0, // Jump
                        0, // Fall
                        0, // Attack 1
                        0, // Attack 2
                        0, // Ability
                        0); // Patrol Idle
                
                newEnemy(50, // Width
                        30, // Height
                        3, // Movement Speed
                        6, // Knockback Taken
                        0, // Minimum Attack Distance
                        40, // Maximum Attack Distance
                        "blue_slime", // setFolder
                        0, // Sprite X Offset
                        -7, // Sprite Y Offset
                        0, // Sprite Flip Offset
                        50, // Sprite Size
                        100); // Max Health
                
                break;
            case MINOTAUR:
                addSpriteLengths(4, //Idle
                        5, // Move
                        3, // Hurt
                        7, // Die
                        0, // Jump
                        0, // Fall
                        0, // Attack 1
                        0, // Attack 2
                        0, // Ability
                        0); // Patrol Idle
                
                newEnemy(100, // Width
                        150, // Height
                        3, // Movement Speed
                        3, // Knockback Taken
                        0, // Minimum Attack Distance
                        100, // Maximum Attack Distance
                        "minotaur", // setFolder
                        -90, // Sprite X Offset
                        -46, // Sprite Y Offset
                        30, // Sprite Flip Offset
                        250, // Sprite Size
                        250); // Max Health
                break;
        }
        
        health.add(maxHealth.get(thisIndex));
        
        ;
    } 
    
    public static void newEnemy(int width, int height, int movementSpeed, double knockbackTaken,
            int minAttackRange, int maxAttackRange, 
            String setFolder, int sXoffset, int sYoffset, int flipOffset, int spriteSize, 
            double mHealth){
        
        int thisIndex = enemyColBlocks.size();
        
        // Create Collision & Physics
        Rectangle colRect = new Rectangle(spawnX, spawnY - height, width, height);
        enemyColBlocks.add(colRect);
        enemyWidths.add(width);
        physManagersIndex.add(PhysicsManager.addChild(colRect, 3));
        knockbackStrengths.add(knockbackTaken);
        attackDisMin.add(minAttackRange);
        attackDisMax.add(maxAttackRange);
        
        //colRect.setFill(Color.BLACK);
        //RenderEngine.addToGroup("LV", colRect);

        // Create Sprite
        spriteFolder.add(setFolder);
        //spriteSetLengths.set(thisIndex, Arrays.asList(3, 3, 3, 3));
        sprManagersIndex.add(SpriteManager.createNewSprite(colRect, sXoffset, sYoffset, spriteSize, spriteSize, flipOffset));
        SpriteManager.setSpriteSet(sprManagersIndex.get(thisIndex), spriteFolder.get(thisIndex), "idle", 3);

        // Create Enemy Variables
        maxHealth.add(mHealth);
    }
    
    public static void addSpriteLengths(int... lengths){
        spriteSetLengths.add(new ArrayList());
        for(int i = 0; i < lengths.length; i++){
            spriteSetLengths.get(spriteSetLengths.size() - 1).add(lengths[i]);
        }
    }
    
    public static void update(){
        for(int i = 0; i < enemyColBlocks.size(); i++){
            
            if(health.get(i) <= 0){
                PhysicsManager.isActive.set(physManagersIndex.get(i), false);
                SpriteManager.setSpriteSet(EnemyManager.sprManagersIndex.get(i), EnemyManager.spriteFolder.get(i), "die", EnemyManager.spriteSetLengths.get(i).get(3), false);
                continue;
            }
            
            double xLoc = enemyColBlocks.get(i).getX() + enemyWidths.get(i)/2;
            double pLoc = Player.collisionBox.getX() + (Player.collisionBox.getWidth()/2);
            
            if(xLoc < pLoc){
                if(xLoc < pLoc - attackDisMax.get(i)){
                    PhysicsManager.movingRight.set(physManagersIndex.get(i), true);
                    PhysicsManager.movingLeft.set(physManagersIndex.get(i), false);
                    SpriteManager.flipped.set(sprManagersIndex.get(i), true);
                } else if(xLoc > pLoc - attackDisMin.get(i)){
                    PhysicsManager.movingRight.set(physManagersIndex.get(i), false);
                    PhysicsManager.movingLeft.set(physManagersIndex.get(i), true);
                    SpriteManager.flipped.set(sprManagersIndex.get(i), false);
                } else {
                    PhysicsManager.movingRight.set(physManagersIndex.get(i), false);
                    PhysicsManager.movingLeft.set(physManagersIndex.get(i), false);
                    SpriteManager.flipped.set(sprManagersIndex.get(i), true);
                }
            } else {
                if(xLoc > pLoc + attackDisMax.get(i)){
                    PhysicsManager.movingRight.set(physManagersIndex.get(i), false);
                    PhysicsManager.movingLeft.set(physManagersIndex.get(i), true);
                    SpriteManager.flipped.set(sprManagersIndex.get(i), false);
                } else if(xLoc < pLoc + attackDisMin.get(i)){
                    PhysicsManager.movingRight.set(physManagersIndex.get(i), true);
                    PhysicsManager.movingLeft.set(physManagersIndex.get(i), false);
                    SpriteManager.flipped.set(sprManagersIndex.get(i), true);
                } else {
                    PhysicsManager.movingRight.set(physManagersIndex.get(i), false);
                    PhysicsManager.movingLeft.set(physManagersIndex.get(i), false);
                    SpriteManager.flipped.set(sprManagersIndex.get(i), false);
                }
            }
          
            if(PhysicsManager.rightVelocities.get(physManagersIndex.get(i)) != 0){
                SpriteManager.setSpriteSet(EnemyManager.sprManagersIndex.get(i), EnemyManager.spriteFolder.get(i), "move", EnemyManager.spriteSetLengths.get(i).get(1));
            } else {
                SpriteManager.setSpriteSet(EnemyManager.sprManagersIndex.get(i), EnemyManager.spriteFolder.get(i), "idle", EnemyManager.spriteSetLengths.get(i).get(0));
            }
        }
    }
    
}
