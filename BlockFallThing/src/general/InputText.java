package general;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class InputText {
    
    public TextField inputField;
    
    InputText(Group grp){
        
        inputField = new TextField();
        grp.getChildren().add(inputField);
        inputField.setLayoutX(460);
        inputField.setLayoutY(815);//262730
        inputField.setStyle("-fx-background-color: transparent; -fx-text-fill: #D7CDCC; -fx-accent: #456990 ;");
        inputField.setAlignment(Pos.CENTER);
        inputField.setFont(Font.font("Verdana", FontWeight.MEDIUM, 45));
        
        inputField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (inputField.getText().length() > 19) {
                    String s = inputField.getText().substring(0, 19);
                    inputField.setText(s);
                }
            }
        });
    }
    
}
