package engine;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UserInterface extends AnimationTimer{
    
    private final int abIconStartX = 50;
    private final int abIconStartY = 620;
    private final int abIconOffset = 50;
    private final int abIconImgOffset = 3;
    private final int abIconTextOffset = 6;
    
    private final ArrayList<ImageView> ivList = new ArrayList();
    private final ArrayList<SpriteAnimation> spriteAnimList = new ArrayList();
    
    private int currentAbility = 0;
    
    private final ImageView abilityIndicator;
    
    public UserInterface(){
        ArrayList<String> abilityList = Main.player.mWisp.abilityList;
        
        abilityIndicator = new ImageView(new Image("/misc/sprites/downSlotArrow.png", 36, 18, true, false));
        abilityIndicator.setY(abIconStartY);
        
        for(int i = 0; i < abilityList.size(); i++){
            Image iconHolder = new Image("/misc/sprites/IconHolder.png", 72, 72, true, false);
            ImageView iconHolderIV = new ImageView(iconHolder);
            iconHolderIV.setX(abIconStartX + ((36 + abIconOffset) * i));
            iconHolderIV.setY(abIconStartY);
            
            Image icon = new Image("/misc/sprites/icon_" + abilityList.get(i) + ".png", 64, 64, true, false);
            ImageView iconIV = new ImageView(icon);
            iconIV.setX(abIconStartX + ((36 + abIconOffset) * i) + abIconImgOffset);
            iconIV.setY(abIconStartY + abIconImgOffset + 1);
            
            Text hotbarSlot = new Text("" + (i + 1));
            hotbarSlot.setFont(Font.loadFont(this.getClass().getResource("/misc/fonts/Pixellari.ttf").toExternalForm(), 18));
            hotbarSlot.setX(abIconStartX + ((36 + abIconOffset) * i) + abIconTextOffset);
            hotbarSlot.setY(abIconStartY + abIconTextOffset + 14);
            hotbarSlot.setFill(Color.WHITE);
            
            ImageView sprite = new ImageView();
            
            sprite.setX(abIconStartX + ((36 + abIconOffset) * i) + abIconImgOffset);
            sprite.setY(abIconStartY + abIconImgOffset + 1);
            ivList.add(sprite);
            SpriteAnimation spriteAnim = null;
            spriteAnimList.add(spriteAnim);
            
            Main.UI_GRP.getChildren().addAll(iconHolderIV, iconIV, hotbarSlot, abilityIndicator);
        }
        
    }
    
    @Override
    public void handle(long now) {
        for(int i = 0; i < Main.player.mWisp.isOnCooldown.size(); i++){
            if(Main.player.mWisp.isOnCooldown.get(i)){
                if(Main.UI_GRP.getChildren().contains(ivList.get(i)) && spriteAnimList.get(i).currentCol >= 8){
                    Main.UI_GRP.getChildren().remove(ivList.get(i));
                    spriteAnimList.get(i).stop();
                    Main.player.mWisp.isOnCooldown.set(i, false);
                } else if(!Main.UI_GRP.getChildren().contains(ivList.get(i))){
                    spriteAnimList.set(i, new SpriteAnimation(ivList.get(i), "/misc/sprites/countdownReal.png", 9, 1, 9, 64, 64, 1, 9/Main.player.mWisp.abilityCooldowns.get(i)));
                    spriteAnimList.get(i).loop = false;
                    spriteAnimList.get(i).desiredOpacity = 0.6;
                    spriteAnimList.get(i).start();
                    
                    Main.UI_GRP.getChildren().add(ivList.get(i));
                }
            }
        }
        
        if(currentAbility != Main.player.mWisp.selectedAbility){
            currentAbility = Main.player.mWisp.selectedAbility;
            abilityIndicator.setX(abIconStartX + ((36 + abIconOffset) * currentAbility));
        }
        
    }
    
}
