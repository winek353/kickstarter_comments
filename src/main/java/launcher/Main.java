package launcher;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//https://www.kickstarter.com/projects/gonab/tang-garden
//https://www.kickstarter.com/projects/1620645203/moonpod-a-zero-gravity-beanbag-for-all-day-deep-re
//https://www.kickstarter.com/projects/antsylabs/fidget-cube-a-vinyl-desk-toy
//^^30k
public class Main extends Application {
    private AnchorPane vbox;
    private ScrollPane scrollPane;
    CommentPanes commentPanes;
    private List<AnchorPane> commentsPanesList;
    List<Comment> comments;
    int maxDiplayedCommentsCount = 150;
    int firstDisplayedCommentIndex = 0;
    int lastDisplayedCommentIndex = maxDiplayedCommentsCount;
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

        vbox = (AnchorPane) scene.lookup("#vbox");
        scrollPane = (ScrollPane) scene.lookup("#scrollPane");
        vbox.prefWidthProperty().bind(mainPane.widthProperty());
        scrollPane.prefWidthProperty().bind(mainPane.widthProperty());

        scrollPane.prefHeightProperty().bind(mainPane.heightProperty().subtract(100));

        commentPanes = new CommentPanes(vbox,scrollPane);


        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                commentPanes.loadVisiblePanes();

            }
        });
    }
    private void setButtonsActions(Scene scene) {
        Button btn2 = (Button) scene.lookup("#button");

        Button btn = (Button) scene.lookup("#button");
        btn.setText("Get Comments from url");
        btn.setOnAction((x) -> {
            String url = ((TextField) scene.lookup("#URL")).getText().toString();
            commentPanes.loadInitialComments(url);
        });
    }

    public static void main(String[] args) {

        launch(args);
    }
}
