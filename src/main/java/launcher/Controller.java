package launcher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.Comment;
import model.Project;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;
import service.ProjectsService;
import java.io.IOException;
import java.text.ParseException;
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
                //main.commentPanesManager.loadInitialComments(url);;
                stage.close();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNewProject() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/addNewProjectWindow.fxml"));

            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add new project");
            stage.setScene(new Scene(root2));
            stage.show();
            Scene scene = stage.getScene();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void manageProjects() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/manageProjectsWindow.fxml"));

            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add new project");
            stage.setScene(new Scene(root2));
            stage.show();
            Scene scene = stage.getScene();
            VBox vbox = ((VBox) scene.lookup("#vbox"));


            ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
            ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");

           // ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
            CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");

           // List<Comment> comments = null;

            System.out.println("");

            List<Project> projectsList = projectsService.getProjects();


            Button updateAllButton = ((Button) scene.lookup("#updateAllButton"));
            updateAllButton.setOnMouseClicked(a->{
                projectsList.forEach(x-> {
                    try {
                        commentsService.updateCommentsInFile(x.getUrl(), x.getName());
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                });
            });

            Button updateSelectedButton = ((Button) scene.lookup("#updateSelectedButton"));
            updateSelectedButton.setOnMouseClicked(a->{

            });
            Button deleteSelectedButton = ((Button) scene.lookup("#deleteSelectedButton"));
            deleteSelectedButton.setOnMouseClicked(a->{

            });
            Button addButton = ((Button) scene.lookup("#addButton"));
            addButton.setOnMouseClicked(a->{

            });



            if(projectsList != null)
            projectsList.forEach(x -> {
                Label label = new Label();
                label.setText(x.getName());
                vbox.getChildren().add(label);
                label.setOnMouseClicked(a -> {
                    if (a.getClickCount() == 2) {
                        List<Comment> comments = main.getAllCommentsFromFile(label.getText());
                        main.commentPanesManager.setComments(comments);
                        main.commentPanesManager.loadInitialComments();
                        stage.close();
                    }
                    else{
                        if(!label.getTextFill().equals(Paint.valueOf("#0076a3")))
                          label.setTextFill(Paint.valueOf("#0076a3"));
                        else
                            label.setTextFill(Paint.valueOf("#000000"));
                    }
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane createProjectPane(String name, String url){
        AnchorPane anchorPane = new AnchorPane();
        Label label = new Label();
        label.setStyle("-fx-font-weight: bold");
        label.setLayoutX(0);
        label.setLayoutY(0);
        label.setPrefHeight(15);
       // label.setPrefWidth(100);
        label.setText(name);

        Label label2 = new Label();
        label.setStyle("-fx-font-weight: bold");
        label.setLayoutX(0);
        label.setLayoutY(25);
        label.setPrefHeight(15);
        // label.setPrefWidth(100);
        label.setText(url);

        return anchorPane;
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
