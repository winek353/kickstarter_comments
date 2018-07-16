package launcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;
import service.ProjectsService;

import java.io.IOException;
import java.text.ParseException;

public class AddNewProjectWindow {
    Scene scene;
    Stage stage;
    private void initializeButtons(){
        Button btn = (javafx.scene.control.Button) scene.lookup("#addProjectButton");
        btn.setText("Get project from url");
        btn.setOnAction((x) -> {
            String url = ((TextField) scene.lookup("#inputURL")).getText().toString();
            String name = ((TextField) scene.lookup("#projectNameInput")).getText().toString();
            ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
            ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");
            CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");
            projectsService.saveProject(name, url);
            try {
                commentsService.getAllCommentsToJsonFile(url, name);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            stage.close();
        });
    }

    private void createWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/addNewProjectWindow.fxml"));

        Parent root2 = null;
        try {
            root2 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.setTitle("Add new project");
        stage.setScene(new Scene(root2));
        stage.show();
        scene = stage.getScene();


    }

    public void create(){
        createWindow();
        initializeButtons();
    }
}
