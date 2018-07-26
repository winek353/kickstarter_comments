package front.Controller;

import back.model.Comment;
import front.service.ErrorDisplayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import back.service.CommentsService;
import back.service.ProjectsService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

public class AddNewProjectController {
    private ErrorDisplayer errorDisplayer = new ErrorDisplayer();

    @FXML
    private TextField inputUrl;

    @FXML
    private TextField inputName;

    @FXML
    private Button addProjectButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    void addProjectClicked(ActionEvent event) {
        String url = inputUrl.getText();
        String name = inputName.getText();
        ApplicationContext ctx = new AnnotationConfigApplicationContext("back/service");
        ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");
        CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");
        url = projectsService.fixUrl(url);
        try {
//            commentsService.getAllCommentsToJsonFile(url, name);
//            projectsService.saveProject(name, url);

            Task commentToJson = createAddNewProjectTask(url, name);
            commentToJson.setOnSucceeded(e->{
                Stage dialog = new Stage();
                dialog.initStyle(StageStyle.UTILITY);
                Scene scene = new Scene(new Group(new Text(25, 25, "Downloading completed!")),
                        190, 80);
                dialog.setScene(scene);
                dialog.showAndWait();
            });

            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(commentToJson.progressProperty());
            Thread thread = new Thread(commentToJson);
            thread.start();
//            thread.join();
//            projectsService.saveProject(name, url);

        }catch (UnknownHostException e){
            errorDisplayer.display("Cannot connect to " + e.getMessage());
        }catch (FileNotFoundException e){
            errorDisplayer.display("Cannot download comments from page: " + e.getMessage());
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Downloading completed");
//        alert.setHeaderText("Success2");
//        String s ="Downloading completed";
//        alert.setContentText(s);
//        alert.show();

    }

    private Task createAddNewProjectTask(String kickstarterProjectUrl, String jsonFileName) throws IOException, ParseException {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                ApplicationContext ctx = new AnnotationConfigApplicationContext("back/service");
                ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");
                CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");

                commentsService.getAllCommentsToJsonFile(kickstarterProjectUrl, jsonFileName,
                        (workDone, max) -> updateProgress(workDone, max));
                projectsService.saveProject(jsonFileName, kickstarterProjectUrl);

                return true;
            }
        };
    }


}
