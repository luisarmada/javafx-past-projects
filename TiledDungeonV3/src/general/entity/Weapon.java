package general.entity;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Weapon{
    
    public ImageView ivSprite = new ImageView();
    
    Weapon(Group grp, Image sprite){
        ivSprite.setImage(sprite);
        ivSprite.setFitHeight(32);
        ivSprite.setFitWidth(32);
        ivSprite.setPreserveRatio(true);
        grp.getChildren().add(ivSprite);
    }
    
    public void updatePosition(double cx, double cy, double desiredAngle){
        ivSprite.setX(cx);
        ivSprite.setY(cy);
        
        double currentAngle = ivSprite.getRotate();
        double interpolation = 0.18;
        
        double shortestAngle = ((((desiredAngle - currentAngle) % 360) + 540) % 360) - 180;
        double newAngle = (currentAngle + shortestAngle * interpolation) % 360;
        
        ivSprite.setRotate(newAngle);
    }
    
}
