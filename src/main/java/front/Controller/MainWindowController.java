package front.Controller;

import back.model.Project;
import front.service.CommentDisplayerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import back.model.Comment;

import java.io.IOException;
import java.util.List;

public class MainWindowController {


   @FXML
   private ScrollPane scrollPane;

   @FXML
   private VBox vBox;

   private CommentDisplayerService commentDisplayerService;//spring?

    @FXML
    private void initialize() {
        commentDisplayerService = new CommentDisplayerService(vBox, scrollPane);

        //loading comments when scroll bar reaches right position
        scrollPane.vvalueProperty().addListener((obs, oldValue, newValue) ->
                commentDisplayerService.loadVisiblePanes());
    }

    @FXML
    void addNewProjectClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addNewProjectWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add project");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            System.out.println("could not open add new project window");
            e.printStackTrace();
        }
    }

    @FXML
    void ProjectsClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/projectsWindow.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Projects");
            stage.setOnCloseRequest(e -> commentDisplayerService.setScrollBarOnOldestUpdatedComment());
            stage.setScene(new Scene(root1));
            stage.show();

            ProjectsWindowController projectsWindowController = fxmlLoader.getController();
            projectsWindowController.setMainWindowController(this);
        } catch (IOException e) {
            System.out.println("could not open add projects window");
            e.printStackTrace();
        }
    }

    void displayComments(List<Comment> comments, Project project){
        commentDisplayerService.setComments(comments);
        commentDisplayerService.setProject(project);
        commentDisplayerService.loadFirstPanes();
    }

}
