package general;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

    Group ROOT = new Group();
    final int WINX = 1600;
    final int WINY = 900;
    
    @Override
    public void start(Stage stage) throws IOException {
        
        Ground ground = new Ground(WINX, WINY, 80);
        ROOT.getChildren().add(ground.getCollision());
        
        InputText iText = new InputText(ROOT);
        
        Text score = new Text("123");
        score.setFont(Font.font("Verdana", 35));
        score.setLayoutX(1500);
        score.setLayoutY(875);
        score.setFill(Color.web("#77ba99"));
        score.setTextAlignment(TextAlignment.RIGHT);
        ROOT.getChildren().add(score);
        
        KanaConverter kanaConverter = new KanaConverter();
        
        BlockGenerator bGen = new BlockGenerator(WINX, ROOT, ground.getCollision(), iText.inputField, kanaConverter, score, "D:\\Downloads\\JLPTWordList.csv");
        bGen.start();
        
        
        // Window Setup
        Scene scene = new Scene(ROOT, WINX, WINY);
        scene.setFill(Color.web("#D7CDCC"));
        stage.setTitle("Falling Nihongo");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
