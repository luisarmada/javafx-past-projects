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
public class Ball {
    static int x, y, dx;
    ImageView ivBall;
    static int ballVelocity = 0;
    
    Ball(){
        x = 300;
        y = 150;
        dx = 5;
        Image imgBall = new Image(getClass().getResourceAsStream("pinkCircle.png"), 80, 80,false,false);
        ivBall = new ImageView(imgBall);
        ivBall.setLayoutX(x);
        ivBall.setLayoutY(y);
        
        new AnimationTimer(){
            @Override
            public void handle(long now){
                if(gameOver == false){
                    ivBall.setLayoutX(ivBall.getLayoutX() + ballVelocity);
                }
            }
        }.start();
    }
    
    void moveLeft(){
        if(x>20){
            x = x - dx;
            ivBall.setLayoutX(x);
        }
    }
    
    void moveRight(){
        if(x<(BallGameFX.windowLength - 100)){
            x = x + dx;
            ivBall.setLayoutX(x);
        }
    }
    
    public ImageView getImageView(){
        return ivBall;
    }
}
