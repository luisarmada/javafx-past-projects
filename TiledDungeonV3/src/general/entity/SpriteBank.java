package general.entity;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;

public class SpriteBank {
    
    private final SpriteAnimation spriteAnim = new SpriteAnimation();
    public ArrayList<Image> playerIdleAnim, playerWalkAnim;
    public Image sword1;
    
    public SpriteBank(){
        
        // Player Animations
        playerIdleAnim = new ArrayList<>(Arrays.asList(spriteAnim.selectFromSheet("eliteknight_spritesheet", 0, 0, 32, 32),
            spriteAnim.selectFromSheet("eliteknight_spritesheet", 1, 0, 32, 32),
            spriteAnim.selectFromSheet("eliteknight_spritesheet", 2, 0, 32, 32),
            spriteAnim.selectFromSheet("eliteknight_spritesheet", 3, 0, 32, 32)));

        playerWalkAnim = new ArrayList<>(Arrays.asList(spriteAnim.selectFromSheet("eliteknight_spritesheet", 4, 0, 32, 32),
                spriteAnim.selectFromSheet("eliteknight_spritesheet", 5, 0, 32, 32),
                spriteAnim.selectFromSheet("eliteknight_spritesheet", 6, 0, 32, 32),
                spriteAnim.selectFromSheet("eliteknight_spritesheet", 7, 0, 32, 32)));
        
        sword1 = spriteAnim.selectFromSheet("swords_spritesheet2", 0, 0, 32, 32);
    }
    
}
