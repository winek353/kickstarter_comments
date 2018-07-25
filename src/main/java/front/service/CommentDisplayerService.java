package front.service;

import back.model.Project;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import back.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentDisplayerService {
    private VBox parent;
    private ScrollPane scrollPane;
    private List<AnchorPane> commentsPanesList;
    private Project project;
    private List<Comment> comments;
    private int maxDiplayedCommentsCount = 200;
    private int currentMaxDiplayedCommentsCount = 0;
    private int lastDisplayedCommentIndex = 0;

    public CommentDisplayerService(VBox parent, ScrollPane scrollPane) {
        this.parent = parent;
        this.scrollPane = scrollPane;
    }

    private void removeTopPanes() {
        commentsPanesList.remove(0);
        parent.getChildren().remove(0);
    }

    private void removeBottomPanes() {
        commentsPanesList.remove(currentMaxDiplayedCommentsCount - 1);
        parent.getChildren().remove(currentMaxDiplayedCommentsCount - 1);
    }

    private boolean shouldAddPanesOnBottom() {
        double scrollBarPosition = scrollPane.getVvalue();
        if (scrollBarPosition > 1. - 20 / scrollPane.getHeight() && comments.size() != lastDisplayedCommentIndex) {
            return true;
        }
        return false;
    }

    private boolean shouldAddPanesOnTop() {
        double scrollBarPosition = scrollPane.getVvalue();
        if (scrollBarPosition < 20 / scrollPane.getHeight() && lastDisplayedCommentIndex > currentMaxDiplayedCommentsCount) {
            return true;
        }
        return false;
    }

    private void addPanesOnBottom() {
        addCommentPaneToList(comments.get(lastDisplayedCommentIndex++));
        parent.applyCss();
        parent.layout();
    }

    private void addPanesOnTop() {
        Comment comment = comments.get(lastDisplayedCommentIndex - currentMaxDiplayedCommentsCount - 1);
        AnchorPane a = createCommentToDisplay(comment);
        commentsPanesList.add(0, a);
        parent.getChildren().add(0, a);
        a.applyCss();
        a.layout();
        lastDisplayedCommentIndex--;
        parent.applyCss();
        parent.layout();
    }

    public void loadVisiblePanes() {

        if (commentsPanesList == null) {
            loadInitialComments();
        }
        if (shouldAddPanesOnBottom()) {
            addPanesOnBottom();
            removeTopPanes();
            scrollPane.setVvalue(scrollPane.getVvalue() - 20 / parent.getHeight());//poprawić
        }
        if (shouldAddPanesOnTop()) {
            addPanesOnTop();
            removeBottomPanes();
            scrollPane.setVvalue(scrollPane.getVvalue() + 20 / parent.getHeight());
        }
    }

    private void loadInitialComments() {
        commentsPanesList = new ArrayList<>();
        currentMaxDiplayedCommentsCount = Math.min(comments.size(), maxDiplayedCommentsCount);
        lastDisplayedCommentIndex = currentMaxDiplayedCommentsCount;
        parent.getChildren().clear();
        for (int i = 0; i < currentMaxDiplayedCommentsCount && i < comments.size(); i++) {
            addCommentPaneToList(comments.get(i));
        }
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    private void addCommentPaneToList(Comment comment) {
        AnchorPane a = createCommentToDisplay(comment);
        commentsPanesList.add(a);
        parent.getChildren().add(a);
    }

    private List<Label> createBadgesLabels(List<String> badges) {
        List<Label> badgesLabels = new ArrayList<>();
        badges.forEach(x -> {
            Label label = new Label();
            label.setText(x);
            label.setStyle("-fx-font-style: italic");
            badgesLabels.add(label);
        });
        return badgesLabels;
    }

    private void assignColorToCommentPane(AnchorPane anchorPane, Comment comment){
        if(comment.getId().compareTo( project.getFirstUpdatedCommentId()) >= 0)//green color displaying
            anchorPane.setStyle("-fx-background-color: #32CD32");
    }

    private AnchorPane createCommentToDisplay(Comment comment) {
        AnchorPane anchorPane = new AnchorPane();

        assignColorToCommentPane(anchorPane, comment);

        ImageView imageView = new ImageView();
        anchorPane.getChildren().add(imageView);
        HBox hBox = new HBox();
        hBox.setLayoutX(45);
        hBox.setLayoutY(28);
        hBox.setSpacing(6);
        Label label = createUsernameLAbel(comment.getAuthor());

        Label label2 = createCommentTimeLabel(comment.getDate());
        hBox.getChildren().add(label);
        hBox.getChildren().addAll(createBadgesLabels(comment.getBadges()));
        hBox.getChildren().addAll(label2);
        Label label3 = createCommentLabel(comment.getText());
        label3.prefWidthProperty().bind(scrollPane.widthProperty().subtract(100));
        anchorPane.getChildren().addAll(hBox, label3);

        return anchorPane;
    }

    private Label createUsernameLAbel(String username) {
        Label label = new Label();
        label.setStyle("-fx-font-weight: bold");
        // label.setLayoutX(45);
        //  label.setLayoutY(28);

        // label.setPrefHeight(15);
        //  label.setPrefWidth(150);
        label.setText(username);
        return label;
    }

    private Label createCommentTimeLabel(String time) {
        Label label = new Label();
        label.setText(time);
//        label.setLayoutX(200);
//        label.setLayoutY(28);
//        label.setPrefHeight(15);
//        label.setPrefWidth(170);
        return label;
    }

    private Label createCommentLabel(String text) {
        Label label = new Label();
        label.setAlignment(Pos.TOP_LEFT);
        label.setText(text);
        label.setLayoutX(45);
        label.setLayoutY(70);
        label.setPrefWidth(400);
        label.setWrapText(true);
        return label;
    }

}
