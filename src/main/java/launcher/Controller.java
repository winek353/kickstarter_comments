package launcher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {
    Main main;

    @FXML
    private MenuBar menuBar;

    @FXML
    private VBox vbox;


    public void
    setMain(Main main){/////////////tmp
        this.main = main;
    }
    @FXML
    private void exit() {
        Platform.exit();
    }

    @FXML
    private void openURL() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/tmp.fxml"));

            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("dsfsdfsdfsd");
            stage.setScene(new Scene(root2));
            stage.show();
            Scene scene = stage.getScene();
            Button btn = (javafx.scene.control.Button) scene.lookup("#button2");
            btn.setText("Get Comments from url");
            btn.setOnAction((x) -> {
                String url = ((TextField) scene.lookup("#inputURL")).getText().toString();
                main.commentPanes.loadInitialComments(url);;
                stage.close();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.getCode() == KeyCode.A) {

            }
        }
    }

}
