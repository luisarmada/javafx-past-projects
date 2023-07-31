/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballgamefx;

import static ballgamefx.BallGameFX.gameOver;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Luis Armada
 */
public class ObstacleSpawner {
    static int x, y, dx, dy;
    ImageView ivView;
    ArrayList obstacles;
    ArrayList pBoxes;
    Ball ball;
    private long lastUpdate = 0;
    Random rand = new Random();
        
    ObstacleSpawner() throws InterruptedException{
        obstacles = new ArrayList();
        pBoxes = new ArrayList();
        ball = BallGameFX.ball;
        
        x = 300;
        y = BallGameFX.windowHeight + 100;
        dx = 15;
        Image imgBall = new Image(getClass().getResourceAsStream("blank.png"), 80, 80,false,false);
        ivView = new ImageView(imgBall);
        ivView.setLayoutX(x);
        ivView.setLayoutY(y);
        

        new AnimationTimer(){
            @Override
            public void handle(long now){
                    if(gameOver == false){
                        move();
                        if (now - lastUpdate >= 2100_999_999 && BallGameFX.gameOver == false) { //milliseconds
                            spawnObjects(BallGameFX.g);
                            lastUpdate = now ;
                        }

                        for (int i = 0; i < obstacles.size(); i++) { //COLLISION DETECTION
                            Obstacles o = (Obstacles)obstacles.get(i);
                            if(o.getImageView().getBoundsInParent().intersects(
                                ball.getImageView().getBoundsInParent())){
                                BallGameFX.gameOver = true;
                                System.out.println("SET,");
                                break;
                            }
                        }
                        
                        for (int i = 0; i < pBoxes.size(); i++) { //COLLISION DETECTION
                            PointBox pB = (PointBox)pBoxes.get(i);
                            if(pB.getImageView().getBoundsInParent().intersects(
                                ball.getImageView().getBoundsInParent())){
                                BallGameFX.playerScore++;
                                pBoxes.remove(pB);
                                break;
                            }
                        }
                        
                    }
                }
        }.start();
        
    }
    
    public ImageView getImageView(){
        return ivView;
    }
    
    public void move(){
        if  (x < 20){
            dx = - dx;
        }
        if(x > (BallGameFX.windowLength - 100)){
            dx = -dx;
        }
        x =x - dx;
        ivView.setLayoutX(x);
    }
    
    public void spawnObjects(Group g){
        spawnObstacles(g, rand.nextInt(2) + 1);
        PointBox pBox = new PointBox();
        g.getChildren().add(pBox.getImageView());
        pBoxes.add(pBox);
    }
    
    public void spawnObstacles(Group g, int gaps){
        Obstacles obstacle1;
        Obstacles obstacle2;
        Obstacles obstacle3;
        switch(gaps){
            case 1:
                obstacle1 = new Obstacles(1, 1);
                obstacle2 = new Obstacles(1, 2);
                g.getChildren().add(obstacle1.getImageView());
                g.getChildren().add(obstacle2.getImageView());
                obstacles.add(obstacle1);
                obstacles.add(obstacle2);
                break;
            case 2:
                obstacle1 = new Obstacles(2, 1);
                obstacle2 = new Obstacles(2, 2);
                obstacle3 = new Obstacles(2, 3);
                g.getChildren().add(obstacle1.getImageView());
                g.getChildren().add(obstacle2.getImageView());
                g.getChildren().add(obstacle3.getImageView());
                obstacles.add(obstacle1);
                obstacles.add(obstacle2);
                obstacles.add(obstacle3);
                break;
        }
    }
}
