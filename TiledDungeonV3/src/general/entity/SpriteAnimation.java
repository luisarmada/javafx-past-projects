package general.entity;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class SpriteAnimation extends AnimationTimer{
    
    ImageView ivSprite;
    ArrayList<Image> spriteAnimImages;
    int currentSpriteIndex;
    long spriteDelay;
    long lastUpdate;
    
    public Image selectFromSheet(String spritePath, int sX, int sY, int sWidth, int sHeight){
        int zoom = 4;
        Image imgOriginal = new Image("/sprites/" + spritePath + ".png");
        Image newImg = new Image("/sprites/" + spritePath + ".png", imgOriginal.getWidth() * zoom, imgOriginal.getHeight() * zoom, true, false);
        PixelReader pReader = newImg.getPixelReader();
        Image currentFrame = new WritableImage(pReader, (sX * (sWidth * zoom)), (sY * (sHeight * zoom)), sWidth * zoom, sHeight * zoom);
        ImageView ivFrame = new ImageView(currentFrame);
        ivFrame.setFitWidth(32);
        ivFrame.setFitHeight(32);
        return currentFrame;
    }

    public void setSpriteAnim(ImageView iv, ArrayList<Image> images, int spriteDelay){
        if(spriteAnimImages == images){
            return;
        }
        ivSprite = iv;
        spriteAnimImages = images;
        this.spriteDelay = new Long(spriteDelay);
        currentSpriteIndex = 0;
    }
    
    @Override
    public void handle(long now) {
        if(ivSprite != null && !spriteAnimImages.isEmpty()){
            if(now - lastUpdate >= spriteDelay){
                lastUpdate = now;
                if(currentSpriteIndex + 1 > spriteAnimImages.size() - 1){
                    currentSpriteIndex = 0;
                } else {
                    currentSpriteIndex++;
                }
                ivSprite.setImage(spriteAnimImages.get(currentSpriteIndex));
            }
        } else {
            lastUpdate = now;
        }
    }
    
}
