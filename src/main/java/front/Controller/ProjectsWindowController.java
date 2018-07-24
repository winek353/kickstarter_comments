package front.Controller;

import front.service.ErrorDisplayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

public class ProjectsWindowController {

    @FXML
    private VBox vBox;

    private ErrorDisplayer errorDisplayer = new ErrorDisplayer();

    private List<Project> projectsList;
    private List<Boolean> selectedProjects;

    private ProjectsService projectsService;
    private CommentsService commentsService;

    private MainWindowController mainWindowController;


    @FXML
    private void initialize() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        projectsService = (ProjectsService) ctx.getBean("projectsService");
        commentsService = (CommentsService) ctx.getBean("commentsService");

        projectsList = projectsService.getProjects();

        selectedProjects = new ArrayList<Boolean>();
        if(projectsList != null)
            while (selectedProjects.size() < projectsList.size()) selectedProjects.add(false);

        addProjectsToVBox();
    }

    @FXML
    void updateAllClicked(ActionEvent event) {
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
    }

    @FXML
    void addNewClicked(ActionEvent event) {
//        AddNewProjectWindow addNewProjectWindow = new AddNewProjectWindow();
//        addNewProjectWindow.create();
//        refreshWindow(); do zrobienia
    }

    @FXML
    void updateSelectedClicked(ActionEvent event) {
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
    }

    @FXML
    void deleteSelectedClicked(ActionEvent event) {
        for (int i = projectsList.size() - 1; i >= 0; i--) {
            if (selectedProjects.get(i)) {
                projectsService.deleteProject(projectsList.get(i).getName());
                vBox.getChildren().remove(i);
                projectsList.remove(i);
                selectedProjects.remove(i);
            }
        }
    }

    private void addProjectsToVBox() {
        if (projectsList == null) return;
        projectsList.forEach(x -> {
            AnchorPane projectPane = createProjectPane(x.getName(), x.getUrl());
            vBox.getChildren().add(projectPane);
            projectPane.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    List<Comment> comments = commentsService.getAllCommentsFromJsonFile(x.getName());
                    mainWindowController.displayComments(comments);
                    Stage stage = (Stage) vBox
                            .getScene().getWindow();
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

    private AnchorPane createProjectPane(String name, String url) {
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

    private void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}
