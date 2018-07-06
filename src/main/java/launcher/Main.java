package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsParserService;
import service.CommentsService;
import service.JsonCommentsWriter;
import service.ServerConnectionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("kickstarter comments");
        primaryStage.setScene(new Scene(root, 600, 605));
        primaryStage.show();

        Scene scene = primaryStage.getScene();
        Button btn = (Button)scene.lookup("#button");
        btn.setText("sdfsdf");
        btn.setOnAction((x)->btn.setText("11111"));
        Text text = new Text();
        Text text2 = new Text();
        text.setText("1111");
        text2.setText("2222");

        AnchorPane mainPane = (AnchorPane) scene.lookup("#mainPane");

        VBox vbox = (VBox) scene.lookup("#vbox");
        ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
        vbox.setSpacing(50);
        showComments(vbox, scrollPane);
        vbox.prefWidthProperty().bind(mainPane.widthProperty());
        scrollPane.prefWidthProperty().bind(mainPane.widthProperty());
        scrollPane.prefHeightProperty().bind(mainPane.heightProperty());
//        vbox.getChildren().add(createAnchorPane());
//        vbox.getChildren().add(createAnchorPane());
//        vbox.getChildren().add(createAnchorPane());
//        vbox.getChildren().add(createAnchorPane());

        primaryStage.show();
    }
    public AnchorPane createAnchorPane(Comment comment, ScrollPane scrollPane){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefHeight(100);
        // anchorPane.prefWidth(400);

        ImageView imageView = new ImageView();
        anchorPane.getChildren().add(imageView);

        Label label = createUsernameLAbel();

        Label label2 = createCommentTimeLabel();

        Label label3 = createCommentLabel(comment.getText());
       // label3.prefWidthProperty().bind(vbox.widthProperty());
        label3.prefWidthProperty().bind(scrollPane.widthProperty().subtract(100));
        anchorPane.getChildren().addAll(label, label2, label3);

        return anchorPane;

    }

    private List<Comment> getAllComments(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService =
                (CommentsService) ctx.getBean("commentsService");
        List<Comment> comments = null;
        try {
            comments = commentsService.getAllComments("https://www.kickstarter.com/projects/steamforged/critical-role");

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("comments.size()");
        System.out.println(comments.size());
        System.out.println("comments.size()");
        return comments;
    }

    private void showComments(VBox vbox, ScrollPane scrollPane){
        List<Comment> comments = getAllComments();
        comments.forEach(x->addComment(x,vbox, scrollPane));
    }

    private void addComment(Comment comment, VBox vbox, ScrollPane scrollPane){
        AnchorPane a = createAnchorPane(comment, scrollPane);
        vbox.getChildren().add(a);
    }

    public Label createUsernameLAbel(){
        Label label = new Label();
        label.setLayoutX(45);
        label.setLayoutY(28);
        label.setPrefHeight(15);
        label.setPrefWidth(150);
        label.setText("aaaaaa123");
        return label;
    }

    public  Label createCommentTimeLabel(){
        Label label = new Label();
        label.setText("3 hours ago");
        label.setLayoutX(160);
        label.setLayoutY(28);
        label.setPrefHeight(15);
        label.setPrefWidth(80);
        return label;
    }

    public  Label createCommentLabel(String text){
        Label label = new Label();
        label.setAlignment(Pos.TOP_LEFT);
        label.setText(text);
        label.setLayoutX(45);
        label.setLayoutY(70);
        label.setPrefWidth(400);
        label.setWrapText(true);
        //label.setText("777sdfsdfsfdn  ffdtddh4dh hd54hd54d54dh45d hg54hgd45hdg5gh3 5d53dg54gdg3 3g54dhghsgn54dfsd");
        return label;
    }


    public static void main(String[] args) {

        launch(args);
    }
}
