package front.Controller;

import front.service.ErrorDisplayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import back.service.CommentsService;
import back.service.ProjectsService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class AddNewProjectController {
    private ErrorDisplayer errorDisplayer = new ErrorDisplayer();

    @FXML
    private TextField inputUrl;

    @FXML
    private TextField inputName;

    @FXML
    private Button addProjectButton;

    @FXML
    void addProjectClicked(ActionEvent event) {
        String url = inputUrl.getText();
        String name = inputName.getText();
        ApplicationContext ctx = new AnnotationConfigApplicationContext("back/service");
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
        Stage stage = (Stage) addProjectButton
                .getScene().getWindow();
        stage.close();
    }
}
