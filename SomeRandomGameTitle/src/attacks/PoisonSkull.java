package attacks;

import engine.Main;
import engine.SpriteAnimation;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import level.LevelGenerator;

public class PoisonSkull extends AnimationTimer{
    
    public Rectangle colRect;
    
    private final ImageView sprite = new ImageView();
    private final SpriteAnimation spriteAnim;
    
    public final double speed = 4;
    public final double speedXMultiplier, speedYMultiplier;
    
    private double angle = 0;
    
    private boolean stopMovement = false;
    
    public PoisonSkull(double x, double y){
        double mouseX = Main.input.mouseX;
        double mouseY = Main.input.mouseY;
        angle = Math.toDegrees(Math.atan2(mouseY - y, mouseX - x));
        if(angle < 0){
            angle += 360;
        }
        
        
        
        speedXMultiplier = Math.cos(Math.toRadians(angle));
        speedYMultiplier = Math.sin(Math.toRadians(angle));
        
        colRect = new Rectangle(x - 10, y - 10, 20, 20);
        colRect.setFill(Color.BLUE);
        //Main.addToLevelGroup(colRect);
        
        sprite.setX(colRect.getX() - 44);
        sprite.setY(colRect.getY() - 42);
        spriteAnim = new SpriteAnimation(sprite, "/attacks/sprites/PoisonSkull.png", 4, 1, 4, 48, 48, 2, 12);
        spriteAnim.start();
        Main.SPELL_GRP.getChildren().add(sprite);
        if(angle < 270 && angle > 90){
            spriteAnim.flipped = true;
        }
    }

    @Override
    public void handle(long now) {
        
        if(!stopMovement){
            colRect.setX(colRect.getX() + (speed * speedXMultiplier));
            colRect.setY(colRect.getY() + (speed * speedYMultiplier));  
        } 

        sprite.setX(colRect.getX() - 48);
        sprite.setY(colRect.getY() - 42);
        sprite.setRotate(spriteAnim.flipped? angle - 180: angle);
        
        double sX = sprite.getX();
        double sY = sprite.getY();
        int offBounds = 100;
        if((spriteAnim.loopFinish && spriteAnim.animTag.equals("exp") || 
                sX < -offBounds || sX > Main.INIT_WINDOW_WIDTH + offBounds || // Destroy if it goes out of bounds
                sY < -offBounds || sY > Main.INIT_WINDOW_HEIGHT + offBounds)){
            this.stop();
            spriteAnim.stop();
            Main.SPELL_GRP.getChildren().remove(sprite);
        }
        
        for(Shape col : LevelGenerator.colShapeList){
            if(colRect.getBoundsInParent().intersects(col.getBoundsInParent())){
                stopMovement = true;
                sprite.setRotate(0);
                spriteAnim.changeSprite(sprite, "/attacks/sprites/PoisonAir.png", 6, 1, 6, 48, 48, 2, 12);
                spriteAnim.loop = false;
                spriteAnim.loopFinish = false;
                spriteAnim.flipped = false;
                spriteAnim.animTag = "exp";
                //Main.LV_GRP.getChildren().remove(sprite);
            }
        }
    }
    
}
