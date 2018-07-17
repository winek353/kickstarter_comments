package launcher;

import FrontService.ErrorDisplayer;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Comment;
import model.Project;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;
import service.ProjectsService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProjectsWindow {
    private ErrorDisplayer errorDisplayer = new ErrorDisplayer();

    VBox vBox;
    Scene scene;
    Stage stage;
    List<Project> projectsList;
    List<Boolean> selectedProjects;
    ProjectsService projectsService;
    CommentsService commentsService;
    Main main;

    public void getData(){
        projectsList = projectsService.getProjects();
    }

    public AnchorPane createProjectPane(String name, String url) {
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

    private void addProjectsToVBox() {
        if (projectsList == null) return;
        projectsList.forEach(x -> {
            AnchorPane projectPane = createProjectPane(x.getName(), x.getUrl());
            vBox.getChildren().add(projectPane);
            projectPane.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    List<Comment> comments = main.getAllCommentsFromFile(x.getName());
                    main.commentPanesManager.setComments(comments);
                    main.commentPanesManager.loadInitialComments();
                    stage.close();
                } else {
                    int i = projectsList.indexOf(x);
                    if (!selectedProjects.get(i)) {
                        projectPane.setBackground(new Background(new BackgroundFill(Color.web("#999999"), CornerRadii.EMPTY, Insets.EMPTY)));
                        selectedProjects.set(i, true);
                    } else {
                        projectPane.setBackground(Background.EMPTY);
                        selectedProjects.set(i, false);
                    }
                }
            });
        });
    }

    public void initializeButtons(){
        Button updateAllButton = ((Button) scene.lookup("#updateAllButton"));
        updateAllButton.setOnMouseClicked(a -> {
            projectsList.forEach(x -> {
                try {
                    commentsService.updateCommentsInFile(x.getUrl(), x.getName());
                } catch (UnknownHostException e){
                    errorDisplayer.display("Cannot connect to " + e.getMessage());
                }catch (FileNotFoundException e){
                    errorDisplayer.display("Cannot download comments from page: " + e.getMessage());
                }catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            });
        });

        Button updateSelectedButton = ((Button) scene.lookup("#updateSelectedButton"));
        updateSelectedButton.setOnMouseClicked(a -> {
            for (int i = 0; i < projectsList.size(); i++) {
                if (selectedProjects.get(i)) {
                    try {
                        commentsService.updateCommentsInFile(projectsList.get(i).getUrl(), projectsList.get(i).getName());
                    } catch (UnknownHostException e){
                        errorDisplayer.display("Cannot connect to " + e.getMessage());
                    }catch (FileNotFoundException e){
                        errorDisplayer.display("Cannot download comments from page: " + e.getMessage());
                    }catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button deleteSelectedButton = ((Button) scene.lookup("#deleteSelectedButton"));
        deleteSelectedButton.setOnMouseClicked(a -> {
            for (int i = projectsList.size() - 1; i >= 0; i--) {
                if (selectedProjects.get(i)) {
                    projectsService.deleteProject(projectsList.get(i).getName());
                    vBox.getChildren().remove(i);
                    projectsList.remove(i);
                    selectedProjects.remove(i);
                }
            }
        });

        Button addButton = ((Button) scene.lookup("#addButton"));
        addButton.setOnMouseClicked(a -> {
            AddNewProjectWindow addNewProjectWindow = new AddNewProjectWindow();
            addNewProjectWindow.create();
            refreshWindow();
        });
    }

    private void refreshWindow(){//nie działa
        vBox.getChildren().clear();
        getData();
        addProjectsToVBox();
    }

    public void createWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/projectsWindow.fxml"));

        Parent root2 = null;
        try {
            root2 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.setTitle("Projects");
        stage.setScene(new Scene(root2));
        stage.show();
        scene = stage.getScene();
        vBox = ((VBox) scene.lookup("#vbox"));
        vBox.setSpacing(20);
    }


    public void create(Main main){
        this.main = main;
        createWindow();

        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        projectsService = (ProjectsService) ctx.getBean("projectsService");
        commentsService = (CommentsService) ctx.getBean("commentsService");

        getData();

        selectedProjects = new ArrayList<Boolean>();
        if(projectsList != null)
            while (selectedProjects.size() < projectsList.size()) selectedProjects.add(false);

        addProjectsToVBox();
        initializeButtons();
    }

}
