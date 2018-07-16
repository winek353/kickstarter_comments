package launcher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Comment;
import model.Project;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;
import service.ProjectsService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class Controller {
    Main main;

    @FXML
    private MenuBar menuBar;

    @FXML
    private VBox vbox;


    public void
    setMain(Main main) {/////////////tmp
        this.main = main;
    }

    @FXML
    private void exit() {
        Platform.exit();
    }

    @FXML
    private void openProjectFromURL() {
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
                 main.getAllCommentsFromUrl(url);
                stage.close();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNewProject() {
        AddNewProjectWindow addNewProjectWindow = new AddNewProjectWindow();
        addNewProjectWindow.create();
    }

    @FXML
    private void Projects() {
       ProjectsWindow projectsWindow = new ProjectsWindow();
       projectsWindow.create(main);
    }



}
