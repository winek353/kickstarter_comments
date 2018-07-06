package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

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

        VBox vbox = (VBox) scene.lookup("#vbox");
        vbox.getChildren().add(createAnchorPane());
        vbox.getChildren().add(createAnchorPane());
        vbox.getChildren().add(createAnchorPane());
        for(int i=0;i<100000;i++){

            vbox.getChildren().add(createAnchorPane());
        }

        primaryStage.show();
    }
    public AnchorPane createAnchorPane(){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefHeight(100);
        anchorPane.prefWidth(400);

        ImageView imageView = new ImageView();
        anchorPane.getChildren().add(imageView);

        Label label = createUsernameLAbel();

        Label label2 = createCommentTimeLabel();

        Label label3 = createCommentLabel();

        anchorPane.getChildren().addAll(label, label2, label3);

        return anchorPane;

    }

    public Label createUsernameLAbel(){
        Label label = new Label();
        label.setLayoutX(45);
        label.setLayoutY(20);
        label.setPrefHeight(15);
        label.setPrefWidth(150);
        label.setText("aaaaaa123");
        return label;
    }

    public  Label createCommentTimeLabel(){
        Label label = new Label();
        label.setText("3 hours ago");
        label.setLayoutX(160);
        label.setLayoutY(20);
        label.setPrefHeight(15);
        label.setPrefWidth(80);
        return label;
    }

    public  Label createCommentLabel(){
        Label label = new Label();
        label.setAlignment(Pos.TOP_LEFT);
        label.setText("sdfsdfsdf");
        label.setLayoutX(45);
        label.setLayoutY(70);
        label.setPrefHeight(200);
        label.setPrefWidth(200);
        label.setWrapText(true);
        label.setText("777sdfsdfsfdn  ffdtddh4dh hd54hd54d54dh45d hg54hgd45hdg5gh3 5d53dg54gdg3 3g54dhghsgn54dfsd");
        return label;
    }


    public static void main(String[] args) {

        launch(args);
    }
}
