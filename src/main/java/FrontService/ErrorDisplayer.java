package FrontService;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service("errorDisplayer")
public class ErrorDisplayer {
    public void display(String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText(contentText);

        alert.showAndWait();
    }
}
