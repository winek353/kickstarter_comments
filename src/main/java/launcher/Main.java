package launcher;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

//https://www.kickstarter.com/projects/gonab/tang-garden
//https://www.kickstarter.com/projects/1620645203/moonpod-a-zero-gravity-beanbag-for-all-day-deep-re
//https://www.kickstarter.com/projects/antsylabs/fidget-cube-a-vinyl-desk-toy
//^^30k
public class Main extends Application {
    private VBox vbox;
    private ScrollPane scrollPane;
    CommentPanesManager commentPanesManager;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Kickstarter comments");
        primaryStage.setScene(new Scene(root, 666, 605));
        primaryStage.show();
        Controller controller = (Controller) fxmlLoader.getController();
        controller.setMain(this);
        Scene scene = primaryStage.getScene();
        setButtonsActions(scene);
        asd(scene);
        primaryStage.show();

    }

    private void asd(Scene scene) {//dac jakas nazwe
        AnchorPane mainPane = (AnchorPane) scene.lookup("#mainPane");

        vbox = (VBox) scene.lookup("#vbox");
        scrollPane = (ScrollPane) scene.lookup("#scrollPane");
        vbox.prefWidthProperty().bind(mainPane.widthProperty());
        scrollPane.prefWidthProperty().bind(mainPane.widthProperty());

        scrollPane.prefHeightProperty().bind(mainPane.heightProperty().subtract(100));

        commentPanesManager = new CommentPanesManager(vbox, scrollPane);


        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                commentPanesManager.loadVisiblePanes();

            }
        });
    }

    private void setButtonsActions(Scene scene) {
        Button btn2 = (Button) scene.lookup("#button");

        Button btn = (Button) scene.lookup("#button");
        btn.setText("Get Comments from url");
        btn.setOnAction((x) -> {
            String url = ((TextField) scene.lookup("#URL")).getText().toString();
            // commentPanesManager.loadInitialComments(url);
        });
    }

    public List<Comment> getAllCommentsFromFile(String file) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");

        List<Comment> comments = null;


        comments = commentsService.getAllCommentsFromJsonFile(file);


        System.out.println("comments.size()");
        System.out.println(comments.size());
        System.out.println("comments.size()");
        return comments;
    }

    public List<Comment> getAllCommentsFromUrl(String url) {
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

    public List<Comment> getAllCommentsToFile(String url, String file) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService = (CommentsService) ctx.getBean("commentsService");

        List<Comment> comments = null;
        try {
            commentsService.getAllCommentsToJsonFile(url, file);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }


        comments = commentsService.getAllCommentsFromJsonFile(file);
        System.out.println("comments.size()");
        System.out.println(comments.size());
        System.out.println("comments.size()");
        return comments;
    }


    public static void main(String[] args) {

        launch(args);
    }
}
