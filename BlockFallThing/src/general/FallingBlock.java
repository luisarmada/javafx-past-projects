package general;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class FallingBlock {
    
    public Rectangle collision;
    public Text text;
    boolean isDisabled;
    
    int fontSize = 45;
    
    FallingBlock(int x, int winX, String displayText){
        
        double desiredWidth = (fontSize*displayText.length()) + 60;
        
        if(x + desiredWidth > winX)
            x -= desiredWidth;
        
        
        collision = new Rectangle(x, -70, desiredWidth, 70);
        collision.setFill(Color.web("#456990"));
        text = new Text(displayText);
        text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, fontSize));
        text.setFill(Color.web("#D7CDCC"));
    }
    
    
}
