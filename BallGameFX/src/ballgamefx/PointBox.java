/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballgamefx;

import static ballgamefx.BallGameFX.gameOver;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Luis Armada
 */
public class PointBox {
    int x, y, dy;
    ImageView ivView;
    Image imgBox;
    
    
    PointBox(){
        x = -50;
        y = BallGameFX.windowHeight - 20;;
        dy = 5;
        Image imgBox = new Image(getClass().getResourceAsStream("blank.png"), 800, 80,false,false);
        ivView = new ImageView(imgBox);
        ivView.setLayoutX(x);
        ivView.setLayoutY(y);
        
        new AnimationTimer(){
            @Override
            public void handle(long now){
                if(gameOver == false){
                    move();
                }
            }
        }.start();
    }
    
     public ImageView getImageView(){
        return ivView;
    }

    public void move(){
        y = y - dy;
        ivView.setLayoutY(y);
    }
}

