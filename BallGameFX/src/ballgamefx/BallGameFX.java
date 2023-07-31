/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballgamefx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Luis Armada
 */
public class BallGameFX extends Application {
    
    
    static int windowLength = 700;
    static int windowHeight = 800;
    public static Group g = new Group();
    static Ball ball;
    
    static int playerScore = 0;
    Label scoreCounter = new Label("" + playerScore);
    
    static boolean gameOver = false;
    boolean showedGameOverScreen = false;
    boolean startGame = false;
    
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        
        Scene scene = new Scene(g, windowLength, windowHeight);
        
        showMenu();
        primaryStage.setScene(scene);
        primaryStage.show();
        
        scene.setOnKeyPressed((KeyEvent t) -> {
            if(gameOver == false){
                switch (t.getCode()){
                    case A: case LEFT:
                        Ball.ballVelocity = -Ball.dx;
                        break;
                    case D: case RIGHT:
                        Ball.ballVelocity = Ball.dx;
                        break;
                }
            }
        });
        
        scene.setOnKeyReleased((KeyEvent t) -> {
            if(gameOver == false){
                switch (t.getCode()){
                    case A: case LEFT: case D: case RIGHT:
                        Ball.ballVelocity = 0;
                        break;
                }
            }
        });
        
        new AnimationTimer(){
            @Override
            public void handle(long now){
                if (gameOver == true) {
                    if (showedGameOverScreen == false) {
                        showGameOver();
                    }
                }
                scoreCounter.setText("" + playerScore);
                scoreCounter.toFront();
            }
        }.start();
        
    }
    
    
    public void showMenu(){
        Label lblText = new Label("Ball Game");
        lblText.setLayoutX(315);
        lblText.setLayoutY(100);
        lblText.setScaleX(5);
        lblText.setScaleY(5);
        g.getChildren().add(lblText);
        
        Button start = new Button("Start Game");
        start.setLayoutX(300);
        start.setLayoutY(300);
        start.setScaleX(2);
        start.setScaleY(2);
        start.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            try {
                startGame();
            } catch (InterruptedException ex) {
                Logger.getLogger(BallGameFX.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        });
        
        g.getChildren().add(start);
    }
    public static void createSpawner() throws InterruptedException{
        ObstacleSpawner spawner;
        spawner = new ObstacleSpawner();
        g.getChildren().add(spawner.getImageView());
    }

    public void startGame() throws InterruptedException{
        g.getChildren().clear();
        ball = new Ball();
        g.getChildren().add(ball.getImageView());
        createSpawner();
        gameOver = false;
        showedGameOverScreen = false;
        
        scoreCounter.setLayoutX(15);
        scoreCounter.setLayoutY(15);
        scoreCounter.setScaleX(5);
        scoreCounter.setScaleY(5);
        g.getChildren().add(scoreCounter);
    }
       
    public void showGameOver(){
        showedGameOverScreen = true;
        Rectangle grey = new Rectangle(0,0,1000,1000);
        grey.setFill(Color.web("#000000"));
        grey.setOpacity(0.3);
        g.getChildren().add(grey);
        Label lblText = new Label("GAME OVER");
        lblText.setLayoutX(315);
        lblText.setLayoutY(400);
        lblText.setScaleX(5);
        lblText.setScaleY(5);
        g.getChildren().add(lblText);
        
        
        Button retry = new Button("Retry");
        retry.setLayoutX(315); retry.setLayoutY(400);
        retry.setScaleX(2); retry.setScaleY(2);
        retry.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                try {
                startGame();
                } catch (InterruptedException ex) {
                Logger.getLogger(BallGameFX.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        });
         g.getChildren().add(retry);
    }
    
    public static void main(String[] args) throws InterruptedException{
        launch(args);
    }
}
