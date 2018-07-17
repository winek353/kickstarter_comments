package launcher;

import FrontService.ErrorDisplayer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;
import service.ProjectsService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import static java.lang.Thread.sleep;

public class AddNewProjectWindow {

    private ErrorDisplayer errorDisplayer = new ErrorDisplayer();

    Scene scene;
    Stage stage;

    private void initializeButtons(){
        Button btn = (javafx.scene.control.Button) scene.lookup("#addProjectButton");
        btn.setText("Get project from url");
        btn.setOnAction((x) -> {
            scene.setCursor(Cursor.WAIT); //???

            String url = ((TextField) scene.lookup("#inputURL")).getText().toString();
            String name = ((TextField) scene.lookup("#projectNameInput")).getText().toString();
            ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
            ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");
            CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");
            url = projectsService.fixUrl(url);
            try {
                commentsService.getAllCommentsToJsonFile(url, name);
                projectsService.saveProject(name, url);
            }catch (UnknownHostException e){
                errorDisplayer.display("Cannot connect to " + e.getMessage());
            }catch (FileNotFoundException e){
                errorDisplayer.display("Cannot download comments from page: " + e.getMessage());
            }
            catch (IOException | ParseException e) {
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
