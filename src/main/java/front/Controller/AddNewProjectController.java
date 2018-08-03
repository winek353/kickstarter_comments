package front.Controller;

import back.service.CommentsService;
import back.service.ProjectsService;
import front.service.ErrorDisplayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    void addProjectClicked(ActionEvent event) {
        String url = inputUrl.getText();
        String name = inputName.getText();
        ApplicationContext ctx = new AnnotationConfigApplicationContext("back/service");
        ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");
        url = projectsService.fixUrl(url);
        try {
            Task commentToJsonTask = createAddNewProjectTask(url, name);
            commentToJsonTask.setOnSucceeded(e->{
                Stage dialog = new Stage();
                dialog.initStyle(StageStyle.UTILITY);
                Scene scene = new Scene(new Group(new Text(25, 25, "Downloading completed!")),
                        190, 80);
                dialog.setScene(scene);
                dialog.showAndWait();
            });
            progressBar.setVisible(true);
            progressBar.setManaged(true);
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(commentToJsonTask.progressProperty());

            progressLabel.setVisible(true);

            commentToJsonTask.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    progressLabel.setText(newValue);
                }
            });

            Thread thread = new Thread(commentToJsonTask);
            thread.start();

        }catch (UnknownHostException e){
            errorDisplayer.display("Cannot connect to " + e.getMessage());
        }catch (FileNotFoundException e){
            errorDisplayer.display("Cannot download comments from page: " + e.getMessage());
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private Task createAddNewProjectTask(String kickstarterProjectUrl, String jsonFileName) throws IOException, ParseException {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                ApplicationContext ctx = new AnnotationConfigApplicationContext("back/service");
                ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");
                CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");

                commentsService.getAllCommentsToJsonFile(kickstarterProjectUrl, jsonFileName,
                        (workDone, max) -> {
                            updateProgress(workDone, max);
                            updateMessage(workDone + "/" + max);
                        });
                projectsService.saveProject(jsonFileName, kickstarterProjectUrl);

                return true;
            }
        };
    }


}
