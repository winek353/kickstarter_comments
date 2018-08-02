package front.service;

import back.model.Comment;
import back.model.Project;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class CommentDisplayerService {
    private VBox parent;
    private ScrollPane scrollPane;
    private List<AnchorPane> commentsPanesList;
    private Project project;
    private List<Comment> comments;
    private int maxDiplayedCommentsCount = 200;

    private int lastDisplayedCommentIndex = 0;
    private int firstDisplayedCommentIndex = 0;

    private int updatedCommentsIncommentsPanesList = 0;

    public CommentDisplayerService(VBox parent, ScrollPane scrollPane) {
        this.parent = parent;
        this.scrollPane = scrollPane;
    }

    private void removeTopPanes() {
        commentsPanesList.remove(0);
        parent.getChildren().remove(0);
    }

    private void removeBottomPanes() {
        commentsPanesList.remove(commentsPanesList.size() -1 /*currentMaxDiplayedCommentsCount - 1*/);
        parent.getChildren().remove(commentsPanesList.size() -1);
    }

    private boolean shouldAddPanesOnBottom() {
        double scrollBarPosition = scrollPane.getVvalue();

        return scrollBarPosition > 1. - 20 / scrollPane.getHeight() && comments.size() != lastDisplayedCommentIndex;
    }

    private boolean shouldAddPanesOnTop() {
        double scrollBarPosition = scrollPane.getVvalue();
        System.out.println(firstDisplayedCommentIndex);
        return (commentsPanesList.size()<8 || scrollBarPosition < 3 / scrollPane.getHeight() )
                && firstDisplayedCommentIndex > 0;
    }

    private void addPanesOnBottom() {
        addCommentPaneToList(comments.get(lastDisplayedCommentIndex++));
        firstDisplayedCommentIndex++;
        parent.applyCss();
        parent.layout();
    }

    private void addPanesOnTop() {
        Comment comment = comments.get(firstDisplayedCommentIndex - 1);
        AnchorPane a = createCommentToDisplay(comment);
        commentsPanesList.add(0, a);
        parent.getChildren().add(0, a);
        a.applyCss();
        a.layout();
        lastDisplayedCommentIndex--;
        firstDisplayedCommentIndex--;
        parent.applyCss();
        parent.layout();
    }

    private int countUpdatedCommentsNumber(){
        return toIntExact(comments.stream().filter(c->c.getId().compareTo( project.getFirstUpdatedCommentId()) >= 0 ).count());
    }

    public void loadFirstPanes(){
        loadInitialComments(countUpdatedCommentsNumber());
    }

    //working only when the comments are already loaded to main window!
    private double computeUpdatedCommentsHeight(){
        return commentsPanesList
                .stream()
                .limit(updatedCommentsIncommentsPanesList)
                .mapToDouble(Region::getHeight)
                .sum();
    }
    private double computeAllCommentsHeight(){
        return commentsPanesList
                .stream()
                .mapToDouble(Region::getHeight)
                .sum();
    }


    public void setScrollBarOnOldestUpdatedComment(){
        if(commentsPanesList != null && !commentsPanesList.isEmpty()){
            double position = computeUpdatedCommentsHeight() / computeAllCommentsHeight();

            scrollPane.setVvalue(position - 0.02);
        }
    }

    public void loadVisiblePanes() {
        if (shouldAddPanesOnBottom()) {
            addPanesOnBottom();
            if(commentsPanesList.size()> maxDiplayedCommentsCount)
                removeTopPanes();
            scrollPane.setVvalue(scrollPane.getVvalue() - 20 / parent.getHeight());//poprawić
        }
        if (shouldAddPanesOnTop()) {
            addPanesOnTop();
            if(commentsPanesList.size()> maxDiplayedCommentsCount)
                removeBottomPanes();
            scrollPane.setVvalue(scrollPane.getVvalue() + 20 / parent.getHeight());
        }
    }

    private void loadInitialComments(int position) {
        firstDisplayedCommentIndex = position-10;
        if(firstDisplayedCommentIndex < 0)
            firstDisplayedCommentIndex = 0;
        commentsPanesList = new ArrayList<>();
        lastDisplayedCommentIndex = Math.min(comments.size(), maxDiplayedCommentsCount + firstDisplayedCommentIndex);
        parent.getChildren().clear();
        for (int i = firstDisplayedCommentIndex; i < lastDisplayedCommentIndex; i++) {
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
        if(comment.getId().compareTo( project.getFirstUpdatedCommentId()) >= 0 )
            updatedCommentsIncommentsPanesList++;
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
        hBox.setLayoutY(10);
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
        label.setText(text+"\n"+"\n");
        label.setLayoutX(45);
        label.setLayoutY(28);
        label.setPrefWidth(450);
        label.setWrapText(true);
        return label;
    }

}
