package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("kickstarter comments");
        primaryStage.setScene(new Scene(root, 600, 605));
        primaryStage.show();

        Scene scene = primaryStage.getScene();
        setButtonsActions(scene);
        asd(scene);


        primaryStage.show();
    }

    private void asd(Scene scene){//dac jakas nazwe
        AnchorPane mainPane = (AnchorPane) scene.lookup("#mainPane");

        VBox vbox = (VBox) scene.lookup("#vbox");
        ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
        vbox.setSpacing(50);
        vbox.prefWidthProperty().bind(mainPane.widthProperty());
        scrollPane.prefWidthProperty().bind(mainPane.widthProperty());
        scrollPane.prefHeightProperty().bind(mainPane.heightProperty().subtract(100));
    }

    private void setButtonsActions(Scene scene){
        Button btn = (Button)scene.lookup("#button");
        btn.setText("Get Comments from url");
        btn.setOnAction((x)->{
            String url = ((TextField)scene.lookup("#URL")).getText().toString();
            VBox vbox = (VBox) scene.lookup("#vbox");
            ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollPane");
            vbox.setSpacing(50);
            showComments(url, vbox, scrollPane);
        });
    }

    public AnchorPane createAnchorPane(Comment comment, ScrollPane scrollPane){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefHeight(100);
        // anchorPane.prefWidth(400);

        ImageView imageView = new ImageView();
        anchorPane.getChildren().add(imageView);

        Label label = createUsernameLAbel(comment.getAuthor());

        Label label2 = createCommentTimeLabel("");

        Label label3 = createCommentLabel(comment.getText());
       // label3.prefWidthProperty().bind(vbox.widthProperty());
        label3.prefWidthProperty().bind(scrollPane.widthProperty().subtract(100));
        anchorPane.getChildren().addAll(label, label2, label3);

        return anchorPane;

    }

    private List<Comment> getAllComments(String url){
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService =
                (CommentsService) ctx.getBean("commentsService");
        List<Comment> comments = null;
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

    private void showComments(String url, VBox vbox, ScrollPane scrollPane){
        List<Comment> comments = getAllComments(url);
        comments.forEach(x->addComment(x,vbox, scrollPane));
    }

    private void addComment(Comment comment, VBox vbox, ScrollPane scrollPane){
        AnchorPane a = createAnchorPane(comment, scrollPane);
        vbox.getChildren().add(a);
    }

    public Label createUsernameLAbel(String username){
        Label label = new Label();
        label.setStyle("-fx-font-weight: bold");
        label.setLayoutX(45);
        label.setLayoutY(28);
        label.setPrefHeight(15);
        label.setPrefWidth(150);
        label.setText(username);
        return label;
    }

    public  Label createCommentTimeLabel(String time){
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
