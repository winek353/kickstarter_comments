package launcher;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommentPanes {
    private VBox parent;
    private ScrollPane scrollPane;
    private List<AnchorPane> commentsPanesList;
    List<Comment> comments;
    int maxDiplayedCommentsCount = 20;
    int firstDisplayedCommentIndex = 0;
    int lastDisplayedCommentIndex = maxDiplayedCommentsCount;

    private void setAllCommentsPosition() {
//        for(int i=1;i<100;i++){
//            commentsPanesList.get(i).setLayoutY(commentsPanesList.get(i-1).getBoundsInParent().getMinY()+commentsPanesList.get(i-1).getHeight());
//        }
    }

    CommentPanes(VBox parent, ScrollPane scrollPane) {
        this.parent = parent;
        this.scrollPane = scrollPane;
        commentsPanesList = new ArrayList<>();
    }

    public void setParentPane(VBox parent) {
        this.parent = parent;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public List<AnchorPane> getCommentsPanesList() {
        return commentsPanesList;
    }

    private double getLastCommentPosition() {
        return commentsPanesList.get(commentsPanesList.size() - 1).getBoundsInParent().getMaxY();
    }

    private double getFirstCommentPosition() {
        return commentsPanesList.get(0).getBoundsInParent().getMinY();
    }


    private void removeTopPanes() {
        commentsPanesList.remove(0);
        parent.getChildren().remove(0);
    }

    private void removeBottomPanes() {
        commentsPanesList.remove(maxDiplayedCommentsCount - 1);
        parent.getChildren().remove(maxDiplayedCommentsCount - 1);
    }


    private boolean shouldAddPanesOnBottom() {
        double maxSize = parent.getHeight();
        double scrollBarPosition = scrollPane.getVvalue();
        if (scrollBarPosition > 1. - 20 / scrollPane.getHeight() && comments.size() != lastDisplayedCommentIndex) {
            return true;
        }
        return false;
    }

    private boolean shouldAddPanesOnTop() {
        double scrollBarPosition = scrollPane.getVvalue();
        if (scrollBarPosition < 20 / scrollPane.getHeight() && lastDisplayedCommentIndex > maxDiplayedCommentsCount) {
            return true;
        }
        return false;
    }

    private void addPanesOnBottom() {
        addCommentPaneToList(comments.get(lastDisplayedCommentIndex++), getLastCommentPosition());
    }

    private void addPanesOnTop() {
        AnchorPane a = createCommentToDisplay(comments.get(lastDisplayedCommentIndex - maxDiplayedCommentsCount - 1));
        commentsPanesList.add(0, a);
        parent.getChildren().add(0, a);
        a.applyCss();
        a.layout();
        lastDisplayedCommentIndex--;
    }

    public void loadVisiblePanes() {
        if (shouldAddPanesOnBottom()) {
            addPanesOnBottom();
            removeTopPanes();
            parent.applyCss();
            parent.layout();
            scrollPane.setVvalue(scrollPane.getVvalue() - 50 / parent.getHeight());
        }
        if (shouldAddPanesOnTop()) {
            addPanesOnTop();
            removeBottomPanes();
            parent.applyCss();
            parent.layout();
            scrollPane.setVvalue(scrollPane.getVvalue() + 50 / parent.getHeight());
//            scrollPane.setVvalue(commentsPanesList.get(1).getLayoutY() / parent.getHeight());
        }
    }

    public void loadInitialComments(String url) {
        commentsPanesList = new ArrayList<>();
        comments = getAllComments(url);
        parent.getChildren().removeAll();

        addCommentPaneToList(comments.get(0), 0);
        for (int i = 1; i < maxDiplayedCommentsCount && i < comments.size(); i++) {
            AnchorPane commentPane = commentsPanesList.get(i - 1);
            commentPane.applyCss();
            commentPane.layout();
            addCommentPaneToList(comments.get(i), commentPane.getBoundsInParent().getMaxY() + commentPane.getHeight());
        }
    }

    private List<Comment> getAllCommentsFromFile(String file) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");

        List<Comment> comments = null;

        comments = commentsService.getAllCommentsFromJsonFile(file);

        System.out.println("comments.size()");
        System.out.println(comments.size());
        System.out.println("comments.size()");
        return comments;
    }


    private List<Comment> getAllCommentsFromUrl(String url) {
        List<Comment> comments = null;
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService =
                (CommentsService) ctx.getBean("commentsService");

        try {
            comments = commentsService.getAllComments(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("comments.size()");
        System.out.println(comments.size());
        System.out.println("comments.size()");
        return comments;
    }

    private List<Comment> getAllCommentsToFile(String url, String file) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");

        List<Comment> comments = null;
        try {
            commentsService.getAllCommentsToJsonFile(url, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("comments.size()");
        System.out.println(comments.size());
        System.out.println("comments.size()");
        comments = commentsService.getAllCommentsFromJsonFile(file);
        System.out.println("comments.size()");
        System.out.println(comments.size());
        System.out.println("comments.size()");
        return comments;
    }

    private List<Comment> getAllComments(String url) {
        if (url.isEmpty())
            return getAllCommentsFromFile("comments");
        else return getAllCommentsFromUrl(url);

    }

    private void addCommentPaneToList(Comment comment, double posY) {
        AnchorPane a = createCommentToDisplay(comment);
        commentsPanesList.add(a);
        parent.getChildren().add(a);
    }

    private AnchorPane createCommentToDisplay(Comment comment) {
        AnchorPane anchorPane = new AnchorPane();
        // anchorPane.prefHeight(10000);

        ImageView imageView = new ImageView();
        anchorPane.getChildren().add(imageView);

        Label label = createUsernameLAbel(comment.getAuthor());

        Label label2 = createCommentTimeLabel(comment.getDate());

        Label label3 = createCommentLabel(comment.getText());
        label3.prefWidthProperty().bind(scrollPane.widthProperty().subtract(100));
        anchorPane.getChildren().addAll(label, label2, label3);

        return anchorPane;
    }

    private Label createUsernameLAbel(String username) {
        Label label = new Label();
        label.setStyle("-fx-font-weight: bold");
        label.setLayoutX(45);
        label.setLayoutY(28);

        label.setPrefHeight(15);
        label.setPrefWidth(150);
        label.setText(username);
        return label;
    }

    private Label createCommentTimeLabel(String time) {
        Label label = new Label();
        label.setText(time);
        label.setLayoutX(200);
        label.setLayoutY(28);
        label.setPrefHeight(15);
        label.setPrefWidth(120);
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
