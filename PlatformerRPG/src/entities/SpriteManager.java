package entities;

import engine.*;
import java.util.ArrayList;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// The sprite manager manages the animations of each sprite

public class SpriteManager {
    
    static ArrayList<ImageView> spriteList = new ArrayList();
    static ArrayList<ArrayList<String>> spriteSet = new ArrayList();
    static ArrayList<Integer> currentFrame = new ArrayList();
    static ArrayList<Boolean> looping = new ArrayList();
    static ArrayList<Rectangle> attachRect = new ArrayList();
    static ArrayList<Long> lastUpdate = new ArrayList();
    static ArrayList<Integer> spriteDelay = new ArrayList(); // nanoseconds
    public static ArrayList<Boolean> flipped = new ArrayList();
    static ArrayList<Integer> flippedOffset = new ArrayList();
    
    static ArrayList<Double> xOffset = new ArrayList();
    static ArrayList<Double> yOffset = new ArrayList();
    static ArrayList<Double> width = new ArrayList();
    static ArrayList<Double> height = new ArrayList();
    
    static ArrayList<Boolean> justChangedSet = new ArrayList();
    
    public static ArrayList<Boolean> playingActionAnimation = new ArrayList();
    
    public static int createNewSprite(Rectangle targetRect, double offsetX, double offsetY, double sizeX, double sizeY, int... fOffset /* flipped offset */){
        ImageView ivSprite = new ImageView(new Image(SpriteManager.class.getResourceAsStream("/art/sprites/player/idle-00.png"), sizeY, sizeX, true, true));
        spriteList.add(ivSprite);
        spriteSet.add(new ArrayList());
        currentFrame.add(0);
        looping.add(true);
        attachRect.add(targetRect);
        lastUpdate.add(Animation.TimerNow);
        spriteDelay.add(100_000_000);
        flipped.add(false);
        playingActionAnimation.add(false);
        justChangedSet.add(false);
        
        if(fOffset.length > 0){
            flippedOffset.add(fOffset[0]);
        } else {
            flippedOffset.add(0);
        }
        
        
        xOffset.add(offsetX);
        yOffset.add(offsetY);
        width.add(sizeX);
        height.add(sizeY);
        
        return spriteList.indexOf(ivSprite);
    }
    
    public static void setSpriteSet(int childIndex, String folderName, String setName, int lastFrame, boolean... loop ){
        String filePath = "/art/sprites/" + folderName + "/" + setName + "-";
        
        if(spriteSet.get(childIndex).contains(filePath + "00.png") || playingActionAnimation.get(childIndex)){
            return;
        }
        
        spriteSet.get(childIndex).clear();
        lastUpdate.set(childIndex, Animation.TimerNow);
        
        int startFrame = 0;
        currentFrame.set(childIndex, startFrame);
        
        if(loop.length > 0){
            looping.set(childIndex, loop[0]);
        } else {
            looping.set(childIndex, true);
        }
        
        for(int i = startFrame; i <= lastFrame; i++){
            String frameIndex;
            if(i < 10){
                frameIndex = "0" + i;
            } else {
                frameIndex = "" + i;
            }
            String spritePath = filePath + frameIndex + ".png";
            spriteSet.get(childIndex).add(spritePath);
        }
        
        justChangedSet.set(childIndex, true);
    }
    
    private static void nextSprite(int childIndex){
        
        int nextFrame;
        
        if(justChangedSet.get(childIndex)){
            nextFrame = currentFrame.get(childIndex);
            justChangedSet.set(childIndex, false);
        } else {
            nextFrame = currentFrame.get(childIndex) + 1;
        }
        
        
        if(nextFrame == spriteSet.get(childIndex).size()){
            if(playingActionAnimation.get(childIndex)){
                playingActionAnimation.set(childIndex, false);
                return;
            }
            if(!looping.get(childIndex)){
                return;
            }
            nextFrame = 0;
        }
        
        currentFrame.set(childIndex, nextFrame);
        RenderEngine.removeFromGroup("LV", spriteList.get(childIndex));
        ImageView ivSprite = new ImageView(new Image(SpriteManager.class.getResourceAsStream(spriteSet.get(childIndex).get(nextFrame)), height.get(childIndex), width.get(childIndex), true, false));
        
        updateIV(childIndex);
        if(flipped.get(childIndex)){
                spriteList.get(childIndex).setScaleX(-1);
            }
        RenderEngine.addToGroup("LV", ivSprite);
        spriteList.set(childIndex, ivSprite);
    }
    
    public static void update(){
        for(int i = 0; i < spriteList.size(); i++){
            
            if(Animation.TimerNow - lastUpdate.get(i) >= spriteDelay.get(i)){
                lastUpdate.set(i, Animation.TimerNow);
                nextSprite(i);
            }
            updateIV(i);
            
            if(flipped.get(i)){
                spriteList.get(i).setScaleX(-1);
            }
        }
    }

    private static void updateIV(int childIndex){
        if(flipped.get(childIndex)){
            spriteList.get(childIndex).setLayoutX(attachRect.get(childIndex).getX() + xOffset.get(childIndex) + flippedOffset.get(childIndex));
        } else {
            spriteList.get(childIndex).setLayoutX(attachRect.get(childIndex).getX() + xOffset.get(childIndex));
        }
        spriteList.get(childIndex).setLayoutY(attachRect.get(childIndex).getY() + yOffset.get(childIndex));
        spriteList.get(childIndex).setRotate(attachRect.get(childIndex).getRotate());
    }
    
}
