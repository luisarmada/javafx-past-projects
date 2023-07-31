package npcs;

import attacks.Fireball;
import attacks.PoisonSkull;
import engine.Main;
import engine.SpriteAnimation;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public final class MagicWisp extends AnimationTimer{
    
    private int anchorX, anchorY, desiredX, desiredY;
    
    private final ImageView sprite = new ImageView();
    private final SpriteAnimation spriteAnim;
    
    private final int spriteOffsetFlip = 24;
    
    public final ArrayList<String> abilityList;
    public final ArrayList<Double> abilityCooldowns;
    public final ArrayList<Boolean> isOnCooldown;
    public int selectedAbility;

    public MagicWisp(){
        abilityList = new ArrayList(); abilityCooldowns = new ArrayList(); isOnCooldown = new ArrayList();
        abilityList.add("Fireball"); abilityCooldowns.add(.3); isOnCooldown.add(false);
        abilityList.add("Poison Skull"); abilityCooldowns.add(3.0); isOnCooldown.add(false);
        abilityList.add("Ice Spike");  abilityCooldowns.add(8.0); isOnCooldown.add(false);
        abilityList.add("Electricity");  abilityCooldowns.add(6.0); isOnCooldown.add(false);
        
        spriteAnim = new SpriteAnimation(sprite, "/npcs/sprites/Wisp_idle.png", 4, 1, 4, 48, 48, 2, 8);
        spriteAnim.start();
        Main.SPELL_GRP.getChildren().add(sprite);
        
        setAbility(1);
    }
    
    @Override 
    public void handle(long now){
        Rectangle playerBox = Main.player.character.colBox;
        anchorX = (int) (playerBox.getX() - 50);
        anchorY = (int) (playerBox.getY() - (playerBox.getHeight() / 2) - 24);
        spriteAnim.flipped = !Main.player.character.spriteAnim.flipped;
        sprite.setX(spriteAnim.flipped? anchorX + spriteOffsetFlip : anchorX);
        sprite.setY(anchorY);
        
        if(Main.player.character.phys.xVelocity != 0){
            spriteAnim.changeSprite(sprite, "/npcs/sprites/Wisp_walk.png", 4, 1, 4, 48, 48, 2, 8);
        } else {
            spriteAnim.changeSprite(sprite, "/npcs/sprites/Wisp_idle.png", 4, 1, 4, 48, 48, 2, 8);
        }
        
        if(Main.input.mousePrimaryHeld){
            useAbility();
        }
        
    }
    
    public void useAbility(){
        if(isOnCooldown.get(selectedAbility - 1))
            return;
        
        switch(selectedAbility){
            case 1:
                Fireball fb = new Fireball(!spriteAnim.flipped ? sprite.getX() + spriteOffsetFlip + 36 : sprite.getX() + 36 , sprite.getY() + 54);
                fb.start();
                break;
            case 2:
                PoisonSkull pc = new PoisonSkull(!spriteAnim.flipped ? sprite.getX() + spriteOffsetFlip + 36 : sprite.getX() + 36 , sprite.getY() + 54);
                pc.start();
                break;
        }
        
        isOnCooldown.set(selectedAbility - 1, true);
        sprite.toFront();
    }
    
    public void setAbility(int slot){
        selectedAbility = slot;
    }
    
}
