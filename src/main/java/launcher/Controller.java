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
               // main.commentPanesManager.loadInitialComments(url);;
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
    private void Projects() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/projectsWindow.fxml"));

            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Projects");
            stage.setScene(new Scene(root2));
            stage.show();
            Scene scene = stage.getScene();
            VBox vbox = ((VBox) scene.lookup("#vbox"));
            vbox.setSpacing(20);

            ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
            ProjectsService projectsService = (ProjectsService) ctx.getBean("projectsService");
            CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");


            List<Project> projectsList = projectsService.getProjects();

            List<Boolean> selectedProjects = new ArrayList<Boolean>();
            while(selectedProjects.size() < projectsList.size()) selectedProjects.add(false);
            addProjectsToVBox(projectsList, selectedProjects, vbox, stage);
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
                for(int i=0;i<projectsList.size();i++){
                    if(selectedProjects.get(i)){
                        try {
                            commentsService.updateCommentsInFile(projectsList.get(i).getUrl(), projectsList.get(i).getName());
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            Button deleteSelectedButton = ((Button) scene.lookup("#deleteSelectedButton"));
            deleteSelectedButton.setOnMouseClicked(a->{
                for(int i=projectsList.size()-1;i>=0;i--){
                    if(selectedProjects.get(i)){
                        projectsService.deleteProject(projectsList.get(i).getName());
                        vbox.getChildren().remove(i);
                        projectsList.remove(i);
                        selectedProjects.remove(i);
                    }
                }
            });
            Button addButton = ((Button) scene.lookup("#addButton"));
            addButton.setOnMouseClicked(a->{
                addNewProject();
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addProjectsToVBox(List<Project> projectsList, List<Boolean> selectedProjects, VBox vBox, Stage stage){
        if(projectsList == null) return;

        projectsList.forEach(x -> {
            AnchorPane projectPane = createProjectPane(x.getName(), x.getUrl());
            vBox.getChildren().add(projectPane);
            projectPane.setOnMouseClicked(e->{
                if (e.getClickCount() == 2) {
                    List<Comment> comments = main.getAllCommentsFromFile(x.getName());
                    main.commentPanesManager.setComments(comments);
                    main.commentPanesManager.loadInitialComments();
                    stage.close();
                }
                else{
                    int i = projectsList.indexOf(x);
                    if(!selectedProjects.get(i)) {
                        projectPane.setBackground(new Background(new BackgroundFill(Color.web("#999999"), CornerRadii.EMPTY, Insets.EMPTY)));
                        selectedProjects.set(i, true);
                    }
                     else {
                        projectPane.setBackground(Background.EMPTY);
                        selectedProjects.set(i, false);
                    }
                }
            });
        });
    }

    public AnchorPane createProjectPane(String name, String url){
        AnchorPane anchorPane = new AnchorPane();
        Label label = new Label();
        label.setStyle("-fx-font-weight: bold");
        label.setLayoutX(0);
        label.setPrefHeight(15);
        label.setText(name);

        Label label2 = new Label();
        label2.setLayoutY(20);
        label2.setPrefHeight(15);
        label2.setText(url);
        anchorPane.getChildren().addAll(label, label2);
        return anchorPane;
    }

}
