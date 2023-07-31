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
public class Obstacles {
    int x,y, dy, xSize, ySize;
    String texture = "BlueSquare.png";
    ImageView ivView;
    Image imgOb;
    
    Obstacles(int gaps, int n){
        y = BallGameFX.windowHeight - 100;
        dy = 5;
        ySize = 80;
        
        switch(gaps){
            case 1:
                oneGap(n);
                break;
            case 2:
                twoGap(n);
                break;
        }
        ivView = new ImageView(imgOb);
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
    
    public void oneGap(int n){
        switch(n){
            case 1:
                x = (ObstacleSpawner.x) - 40 - 800;
                imgOb = new Image(getClass().getResourceAsStream(texture), 800, ySize,false,false);
                break;
            case 2:
                x = (ObstacleSpawner.x) + 120;
                imgOb = new Image(getClass().getResourceAsStream(texture), 800, ySize,false,false);
                break;
        }
        
    }
    
    public void twoGap(int n){
        switch(n){
            case 1:
                x = (ObstacleSpawner.x) - 180 - 800;
                imgOb = new Image(getClass().getResourceAsStream(texture), 800, ySize,false,false);
                break;
            case 2:
                x = (ObstacleSpawner.x) + 260;
                imgOb = new Image(getClass().getResourceAsStream(texture), 800, ySize,false,false);
                break;
            case 3:
                x = (ObstacleSpawner.x) - 40;
                imgOb = new Image(getClass().getResourceAsStream(texture), 160, ySize,false,false);
                break;
        }
    }
}
